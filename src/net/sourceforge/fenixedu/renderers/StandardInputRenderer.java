package net.sourceforge.fenixedu.renderers;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

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

    private String validatorClasses;
    
    private boolean hideValidators;
    
    public StandardInputRenderer() {
        super();
        
        this.hideValidators = false;
    }

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
    
    public boolean isHideValidators() {
        return this.hideValidators;
    }

    /**
     * Allows you to suppress the inclusion of the validator messages in the 
     * standard layout. This is specilly usefull if you want to show all messages
     * in one place in the page.
     * 
     * @property
     */
    public void setHideValidators(boolean hideValidators) {
        this.hideValidators = hideValidators;
    }

    public String getValidatorClasses() {
        return this.validatorClasses;
    }

    /**
     * Configure the html classes to apply to the validator messages.
     * 
     * @property
     */
    public void setValidatorClasses(String validatorClasses) {
        this.validatorClasses = validatorClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new ObjectInputTabularLayout(getContext().getMetaObject());
    }

    class ObjectInputTabularLayout extends TabularLayout {
        public Logger logger = Logger.getLogger(ObjectInputTabularLayout.class);

        protected Map<Integer, Validatable> inputComponents;

        private MetaObject object;

        public ObjectInputTabularLayout(MetaObject object) {
            this.object = object;
            this.inputComponents = new HashMap<Integer, Validatable>();
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
                if (isHideValidators()) {
                    component = new HtmlText();
                }
                else {
                    Validatable inputComponent = inputComponents.get(rowIndex);
    
                    if (inputComponent != null) {
                        component = getValidator(inputComponent, this.object.getSlots().get(rowIndex));
    
                        if (component != null) {
                            component.setClasses(getValidatorClasses());
                        }
                        else {
                            component = new HtmlText();
                        }
                    } else {
                        component = new HtmlText();
                    }
                }
                
                break;
            default:
                component = new HtmlText();
                break;
            }

            return component;
        }
    }

}
