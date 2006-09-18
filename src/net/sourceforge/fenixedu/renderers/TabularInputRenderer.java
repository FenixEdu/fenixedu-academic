package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.CollectionRenderer.CollectionTabularLayout;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInputComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

/**
 * This renderer allows to edit all objects of a collection at the same time.
 * The presentation is very similar to the one of
 * {@link net.sourceforge.fenixedu.renderers.CollectionRenderer} but instead of
 * presentig the slot's value in each cell, the slot's editor is shown.
 * 
 * <p>
 * Example: <table border="1"> <thead>
 * <th>Name</th>
 * <th>Age</th>
 * <th>Gender</th>
 * </thead>
 * <tr>
 * <td><input type="text" value="Name A"/></td>
 * <td><input type="text" value="20"/></td>
 * <td> <select> <option>-- Please Select --</option> <option
 * selected="selected">Female</option> <option>Male</option> </select> </td>
 * </tr>
 * <tr>
 * <td><input type="text" value="Name B"/></td>
 * <td><input type="text" value="22"/></td>
 * <td> <select> <option>-- Please Select --</option> <option>Female</option>
 * <option selected="selected">Male</option> </select> </td>
 * </tr>
 * <tr>
 * <td><input type="text" value="Name C"/></td>
 * <td><input type="text" value="21"/></td>
 * <td> <select> <option>-- Please Select --</option> <option
 * selected="selected">Female</option> <option>Male</option> </select> </td>
 * </tr>
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

    public String getBundle(String name) {
        return this.collectionRenderer.getBundle(name);
    }

    public String getKey(String name) {
        return this.collectionRenderer.getKey(name);
    }

    public String getLink(String name) {
        return this.collectionRenderer.getLink(name);
    }

    public String getModule(String name) {
        return this.collectionRenderer.getModule(name);
    }

    public String getOrder(String name) {
        return this.collectionRenderer.getOrder(name);
    }

    public String getParam(String name) {
        return this.collectionRenderer.getParam(name);
    }

    public String getText(String name) {
        return this.collectionRenderer.getText(name);
    }

    public boolean isExcludedFromFirst(String name) {
        return this.collectionRenderer.isExcludedFromFirst(name);
    }

    public boolean isExcludedFromLast(String name) {
        return this.collectionRenderer.isExcludedFromLast(name);
    }

    public String getVisibleIf(String name) {
        return this.collectionRenderer.getVisibleIf(name);
    }
    
    public String getVisibleIfNot(String name) {
	return this.collectionRenderer.getVisibleIfNot(name);
    }

    public String getLinkFormat(String name) {
        return this.collectionRenderer.getLinkFormat(name);
    }

    public String getContextRelative(String name) {
        return this.collectionRenderer.getContextRelative(name);
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
     * 
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

    /**
     * Allows you choose the order of the objects in the table. See
     * {@link CollectionRenderer#setSortBy(String)}.
     * 
     * @property
     */
    public void setSortBy(String sortBy) {
        this.collectionRenderer.setSortBy(sortBy);
    }

    /**
     * See {@link CollectionRenderer#setBundle(String, String)}.
     * 
     * @property
     */
    public void setBundle(String name, String value) {
        this.collectionRenderer.setBundle(name, value);
    }

    /**
     * See {@link CollectionRenderer#setKey(String, String)}.
     * 
     * @property
     */
    public void setKey(String name, String value) {
        this.collectionRenderer.setKey(name, value);
    }

    /**
     * See {@link CollectionRenderer#setLink(String, String)}.
     * 
     * @property
     */
    public void setLink(String name, String value) {
        this.collectionRenderer.setLink(name, value);
    }

    /**
     * See {@link CollectionRenderer#setModule(String, String)}.
     * 
     * @property
     */
    public void setModule(String name, String value) {
        this.collectionRenderer.setModule(name, value);
    }

    /**
     * See {@link CollectionRenderer#setOrder(String, String)}.
     * 
     * @property
     */
    public void setOrder(String name, String value) {
        this.collectionRenderer.setOrder(name, value);
    }

    /**
     * See {@link CollectionRenderer#setParam(String, String)}.
     * 
     * @property
     */
    public void setParam(String name, String value) {
        this.collectionRenderer.setParam(name, value);
    }

    /**
     * See {@link CollectionRenderer#setText(String, String)}.
     * 
     * @property
     */
    public void setText(String name, String value) {
        this.collectionRenderer.setText(name, value);
    }

    /**
     * See {@link CollectionRenderer#setExcludedFromFirst(String, String)}.
     * 
     * @property
     */
    public void setExcludedFromFirst(String name, String value) {
        this.collectionRenderer.setExcludedFromFirst(name, value);
    }

    /**
     * See {@link CollectionRenderer#setExcludedFromLast(String, String)}.
     * 
     * @property
     */
    public void setExcludedFromLast(String name, String value) {
        this.collectionRenderer.setExcludedFromLast(name, value);
    }

    /**
     * See {@link CollectionRenderer#setVisibleIf(String, String)}.
     * 
     * @property
     */
    public void setVisibleIf(String name, String value) {
        this.collectionRenderer.setVisibleIf(name, value);
    }
    
    /**
     * See {@link CollectionRenderer#setVisibleIfNot(String, String)}.
     * 
     * @property
     */
    public void setVisibleIfNot(String name, String value) {
	this.collectionRenderer.setVisibleIfNot(name, value);
    }

    /**
     * See {@link CollectionRenderer#setLinkFormat(String, String)}.
     * 
     * @property
     */
    public void setLinkFormat(String name, String value) {
        this.collectionRenderer.setLinkFormat(name, value);
    }

    /**
     * See {@link CollectionRenderer#setContextRelative(String, String)}.
     * 
     * @property
     */
    public void setContextRelative(String name, String value) {
        this.collectionRenderer.setContextRelative(name, value);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        this.collectionRenderer.setContext(getContext());

        final CollectionTabularLayout layout = (CollectionTabularLayout) this.collectionRenderer
                .getLayout(object, type);
        return new TabularLayout() {

            @Override
            protected boolean hasHeader() {
                return layout.hasHeader();
            }

            @Override
            protected int getNumberOfColumns() {
                return layout.getNumberOfColumns();
            }

            @Override
            protected int getNumberOfRows() {
                return layout.getNumberOfRows();
            }

            @Override
            protected HtmlComponent getHeaderComponent(int columnIndex) {
                return layout.getHeaderComponent(columnIndex);
            }

            @Override
            protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
                HtmlComponent component = layout.getComponent(rowIndex, columnIndex);

                Validatable validatable = findValidatableComponent(component);
                if (validatable instanceof HtmlInputComponent) {
                    HtmlInputComponent input = (HtmlInputComponent) validatable;

                    String label = layout.getLabel(columnIndex);

                    if (label != null && label.length() > 0) {
                        input.setAlternateText(label);
                    }
                }

                return component;
            }

        };
    }

}
