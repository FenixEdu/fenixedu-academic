package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

/**
 * This renderer provides a basic presentation for an object. The object
 * is presented in a table that contains it's details. The table will have 
 * two columns and a row for each one of the object's slots. In the left column
 * of each row the slot's label will be placed. In the right column the 
 * slot's value will be presented.
 * 
 * <p>
 * Example:
 * <table border="1">
 *  <tr>
 *      <th>Name</th>
 *      <td>Jane Doe</td>
 *  </tr>
 *  <tr>
 *      <th>Age</th>
 *      <td>20</td>
 *  </tr>
 *  <tr>
 *      <th>Gender</th>
 *      <td>Female</td>
 *  </tr>
 * </table>
 * 
 * @author cfgi
 */
public class StandardObjectRenderer extends OutputRenderer {
    private String caption;

    private String rowClasses;

    private String columnClasses;

    private String labelTerminator;
    
    public String getCaption() {
        return caption;
    }

    /**
     * The caption of the generated table.
     * 
     * @property
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getColumnClasses() {
        return columnClasses;
    }

    /**
     * The classes to be used for each column in the generated table.
     * See {@link CollectionRenderer#setColumnClasses(String)}.
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
     * The classes to be used for each row in the table.
     * See {@link CollectionRenderer#setRowClasses(String)}.
     * 
     * @property
     */
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    public String getLabelTerminator() {
        return this.labelTerminator;
    }

    /**
     * Chooses the suffix to be added to each label. If the label already contains
     * that suffix then nothing will be added.
     * <p>
     * For example <code>labelTerminator=":"</code> would generate rows like
     * <table border="1">
     *  <tr>
     *      <th>Name:</th>
     *      <td>Jane Doe</td>
     *  </tr>
     * </table>
     * 
     * @property
     */
    public void setLabelTerminator(String labelTerminator) {
        this.labelTerminator = labelTerminator;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new ObjectTabularLayout(getContext().getMetaObject());
    }

    class ObjectTabularLayout extends TabularLayout {
        private MetaObject object;

        public ObjectTabularLayout(MetaObject object) {
            this.object = object;
        }

        @Override
        protected int getNumberOfColumns() {
            return 2;
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
            if (columnIndex == 0) {
                MetaSlot slot = this.object.getSlots().get(rowIndex);
                return new HtmlText(addLabelTerminator(slot.getLabel()), false);
            }
            else {
                return renderSlot(this.object.getSlots().get(rowIndex));
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
