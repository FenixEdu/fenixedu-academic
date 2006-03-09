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
 *      <td>Name</td>
 *      <td>Jane Doe</td>
 *  </tr>
 *  <tr>
 *      <td>Age</td>
 *      <td>20</td>
 *  </tr>
 *  <tr>
 *      <td>Gender</td>
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
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                MetaSlot slot = this.object.getSlots().get(rowIndex);
                return new HtmlText(slot.getLabel());
            }
            else {
                return renderSlot(this.object.getSlots().get(rowIndex));
            }
        }
    }
}
