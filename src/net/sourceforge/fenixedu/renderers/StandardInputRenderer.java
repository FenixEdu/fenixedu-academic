package net.sourceforge.fenixedu.renderers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

import org.apache.log4j.Logger;

/**
 * This renderer provides a simple way of editing objects. A table
 * is used to organize the presentation. Each slot will have it's
 * corresponding row. Each row has three columns. In the left column
 * the slot's label will be presented. In the middle column the
 * editor for the slot's value will be presented. In the rightmost
 * column validation errors are presented.
 * 
 * <p>
 * Example:
 * <table border="1">
 *  <tr>
 *      <td>Name</td>
 *      <td><input type="text"/></td>
 *      <td>An empty name is not valid.</td>
 *  </tr>
 *  <tr>
 *      <td>Age</td>
 *      <td><input type="text" value="20"/></td>
 *      <td></td>
 *  </tr>
 *  <tr>
 *      <td>Gender</td>
 *      <td>
 *          <select>
 *              <option>-- Please Select --</option>
 *              <option>Female</option>
 *              <option>Male</option>
 *          </select>
 *      </td>
 *      <td>You must select a gender.</td>
 *  </tr>
 * </table>
 * 
 * @author cfgi
 */
public class StandardInputRenderer extends InputRenderer {
    private String rowClasses;

    private String columnClasses;

    public String getColumnClasses() {
        return columnClasses;
    }

    /**
     * The classes to be used in each column of the generated table.
     * See {@link CollectionRenderer#setColumnClasses(String)} for more details.
     * Remember that the first column contains labels, the second column
     * slot's editors, and the third the validator messages. 
     * 
     * @property
     */
    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public String getRowClasses() {
        return rowClasses;
    }

    /**
     * The classes to be used in each row of the table.
     * See {@link CollectionRenderer#setRowClasses(String)} for more details.
     * 
     * @property
     */
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        return new ObjectInputTabularLayout(getContext().getMetaObject());
    }

    class ObjectInputTabularLayout extends TabularLayout {
        private Logger logger = Logger.getLogger(ObjectInputTabularLayout.class);

        protected Map<Integer, HtmlSimpleValueComponent> inputComponents;

        private MetaObject object;

        public ObjectInputTabularLayout(MetaObject object) {
            this.object = object;
            this.inputComponents = new HashMap<Integer, HtmlSimpleValueComponent>();
        }

        @Override
        protected int getNumberOfColumns() {
            return 3;
        }

        @Override
        protected int getNumberOfRows() {
            return this.object.getSlots().size();
        }

        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            return new HtmlText();
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            HtmlComponent component;

            switch (columnIndex) {
            case 0:
                MetaSlot slot = this.object.getSlots().get(rowIndex);
                component = new HtmlText(slot.getLabel());
                break;
            case 1:
                component = renderSlot(this.object.getSlots().get(rowIndex));
                inputComponents.put(rowIndex, findValidatableComponent(component));

                break;
            case 2:
                HtmlSimpleValueComponent inputComponent = inputComponents.get(rowIndex);

                if (inputComponent != null) {
                    component = getValidator(inputComponent, this.object.getSlots().get(rowIndex));

                    if (component == null) {
                        component = new HtmlText();
                    }
                } else {
                    component = new HtmlText();
                }
                break;
            default:
                component = new HtmlText();
                break;
            }

            return component;
        }

        protected HtmlValidator getValidator(Validatable inputComponent,
                MetaSlot slot) {
            Class<HtmlValidator> validatorType = slot.getValidator();

            if (validatorType == null) {
                return null;
            }

            Constructor<HtmlValidator> constructor;
            try {
                constructor = validatorType.getConstructor(new Class[] { Validatable.class });

                HtmlValidator validator = constructor.newInstance(inputComponent);
                RenderUtils.setProperties(validator, slot.getValidatorProperties());
                
                return validator;
            } catch (Exception e) {
                logger.warn("could not create validator '" + validatorType.getName() + "' for slot '"
                        + slot.getName() + "': " + e);
                return null;
            }
        }
    }

}
