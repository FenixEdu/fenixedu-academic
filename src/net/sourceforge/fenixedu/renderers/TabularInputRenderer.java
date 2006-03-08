package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

/**
 * This renderer allows to edit all objects of a collection at the same time.
 * The presentation is very similar to the one of {@link net.sourceforge.fenixedu.renderers.CollectionRenderer}
 * but instead of presentig the slot's value in each cell, the slot's editor
 * is shown.
 * 
 * <p>
 * Example:
 * <table border="1">
 *  <thead>
 *      <th>Name</th>
 *      <th>Age</th>
 *      <th>Gender</th>
 *  </thead>
 *  <tr>
 *      <td><input type="text" value="Name A"/></td>
 *      <td><input type="text" value="20"/></td>
 *      <td>
 *          <select>
 *              <option>-- Please Select --</option>
 *              <option selected="selected">Female</option>
 *              <option>Male</option>
 *          </select>
 *      </td>
 *  </tr>
 *  <tr>
 *      <td><input type="text" value="Name B"/></td>
 *      <td><input type="text" value="22"/></td>
 *      <td>
 *          <select>
 *              <option>-- Please Select --</option>
 *              <option>Female</option>
 *              <option selected="selected">Male</option>
 *          </select>
 *      </td>
 *  </tr>
 *  <tr>
 *      <td><input type="text" value="Name C"/></td>
 *      <td><input type="text" value="21"/></td>
 *      <td>
 *          <select>
 *              <option>-- Please Select --</option>
 *              <option selected="selected">Female</option>
 *              <option>Male</option>
 *          </select>
 *      </td>
 *  </tr>
 * </table>
 * 
 * @author cfgi
 */
public class TabularInputRenderer extends InputRenderer {

    private CollectionRenderer collectionRenderer;
    
    public TabularInputRenderer() {
        this.collectionRenderer = new CollectionRenderer() {

            @Override
            protected HtmlComponent renderSlot(MetaSlot slot) {
                return TabularInputRenderer.this.renderSlot(slot);
            }
            
        };
    }
    
    public String getClasses() {
        return this.collectionRenderer.getClasses();
    }
    
    public String getStyle() {
        return this.collectionRenderer.getStyle();
    }
    
    public String getTitle() {
        return this.collectionRenderer.getTitle();
    }

    public String getCaption() {
        return this.collectionRenderer.getCaption();
    }
    
    public String getHeaderClasses() {
        return this.collectionRenderer.getHeaderClasses();
    }

    public String getColumnClasses() {
        return this.collectionRenderer.getColumnClasses();
    }
    
    public String getRowClasses() {
        return this.collectionRenderer.getRowClasses();
    }

    public String getPrefixes() {
        return this.collectionRenderer.getPrefixes();
    }

    public String getSuffixes() {
        return this.collectionRenderer.getSuffixes();
    }

    /**
     * @property
     */
    public void setCaption(String caption) {
        this.collectionRenderer.setCaption(caption);
    }

    /**
     * {@inheritDoc}
     * 
     * @property
     */
    public void setClasses(String classes) {
        this.collectionRenderer.setClasses(classes);
    }

    /**
     * {@inheritDoc}
     * 
     * @property
     */
    public void setStyle(String style) {
        this.collectionRenderer.setStyle(style);
    }

    /**
     * {@inheritDoc}
     * 
     * @property
     */
    public void setTitle(String title) {
        this.collectionRenderer.setTitle(title);
    }

    /**
     * The classes to be used in each header cell. See
     * {@link CollectionRenderer#setHeaderClasses(String)}.
     * 
     * @property
     */
    public void setHeaderClasses(String headerClasses) {
        this.collectionRenderer.setHeaderClasses(headerClasses);
    }

    /**
     * The classes to be used in each row's cell. See
     * {@link CollectionRenderer#setColumnClasses(String)}.
     * @property
     */
    public void setColumnClasses(String columnClasses) {
        this.collectionRenderer.setColumnClasses(columnClasses);
    }

    /**
     * The classes to be used in each row. See
     * {@link CollectionRenderer#setRowClasses(String)}.
     *
     * @property
     */
    public void setRowClasses(String rowClasses) {
        this.collectionRenderer.setRowClasses(rowClasses);
    }

    /**
     * The text to be added as a prefix to each cell.
     * 
     * @property
     */
    public void setPrefixes(String prefixes) {
        this.collectionRenderer.setPrefixes(prefixes);
    }

    /**
     * The text to be added as a suffix of each cell.
     * 
     * @property
     */
    public void setSuffixes(String suffixes) {
        this.collectionRenderer.setSuffixes(suffixes);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        this.collectionRenderer.setContext(getContext());

        return this.collectionRenderer.new CollectionTabularLayout((Collection) object);
    }
}
