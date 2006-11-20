package net.sourceforge.fenixedu.renderers;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLabel;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
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
 *      <th>Name</th>
 *      <td><input type="text"/></td>
 *      <td>An empty name is not valid.</td>
 *  </tr>
 *  <tr>
 *      <th>Age</th>
 *      <td><input type="text" value="20"/></td>
 *      <td></td>
 *  </tr>
 *  <tr>
 *      <th>Gender</th>
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
    
    private String labelTerminator;
    
    private boolean displayLabel = Boolean.TRUE;
    
    
    public boolean isDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(boolean displayLabel) {
        this.displayLabel = displayLabel;
    }

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

    public String getLabelTerminator() {
        return this.labelTerminator;
    }
    
    /**
     * Chooses the suffix to be added to each label. If the label already contains
     * that suffix then nothing will be added. See {@link StandardObjectRenderer#setLabelTerminator(String)}.
     *
     * @property
     */
    public void setLabelTerminator(String labelTerminator) {
        this.labelTerminator = labelTerminator;
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
        protected boolean isHeader(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            HtmlComponent component;

            switch (columnIndex) {
            case 0:
                MetaSlot slot = this.object.getSlots().get(rowIndex);
                if(displayLabel) {

                    if (slot.isReadOnly()) {
                        component = new HtmlText(addLabelTerminator(slot.getLabel()), false); 
                    }
                    else {
                        HtmlLabel label = new HtmlLabel();
                        label.setFor(slot.getKey().toString());
                        label.setText(addLabelTerminator(slot.getLabel()));
                    
                        component = label;
                    }
                }
                else {
                    component = null;
                }
                break;
            case 1:
                slot = this.object.getSlots().get(rowIndex);
                
                component = renderSlot(slot);
                
                if (! slot.isReadOnly()) {
                    Validatable validatable = findValidatableComponent(component);
                    
                    if (validatable != null) {
                        HtmlFormComponent formComponent = (HtmlFormComponent) validatable;
                        if (formComponent.getId() == null) {
                            formComponent.setId(slot.getKey().toString());
                        }
                        
                        inputComponents.put(rowIndex, validatable);
                    }
                }
                
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
        
        @Override
        protected void costumizeCell(HtmlTableCell cell, int rowIndex, int columnIndex) {
            super.costumizeCell(cell, rowIndex, columnIndex);
            
            if (columnIndex == 0) {
                cell.setScope("row");
            }
        }

        // duplicated code id=standard-renderer.label.addTerminator
        protected String addLabelTerminator(String label) {
            if (getLabelTerminator() == null) {
                return label;
            }
            
            if (label == null) {
                return null;
            }
            
            if (label.endsWith(getLabelTerminator())) {
                return label;
            }
            
            return label + getLabelTerminator();
        }
    }

}
