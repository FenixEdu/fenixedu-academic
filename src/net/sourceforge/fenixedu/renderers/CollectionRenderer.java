package net.sourceforge.fenixedu.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectCollection;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This renderer provides a way of presenting a collection in a table. The table
 * will have as many rows as there are objects in the collection beeing
 * presented. The table will have as many columns as the slots defined in the
 * given schema.
 * 
 * <p>
 * If no schema is given then a default schema is created for the first object
 * in the collection and then used for the remaining objects.
 * 
 * <p>
 * The header of each column will contain the slot's label. The table can also
 * be extended with control links in each row. That allows you to start an
 * action configured with data from the object in the row.
 * 
 * Example: <table border="1"> <thead>
 *          <th>Name</th>
 *          <th>Age</th>
 *          <th>Gender</th>
 *          <th></th>
 *          <th></th>
 *      </thead>
 *      <tr>
 *          <td>Name A</td>
 *          <td>20</td>
 *          <td>Female</td>
 *          <td><a href="#">Edit</a></td>
 *          <td><a href="#">Delete</a></td>
 *      </tr>
 *      <tr>
 *          <td>Name B</td>
 *          <td>22</td>
 *          <td>Male</td>
 *          <td><a href="#">Edit</a></td>
 *          <td><a href="#">Delete</a></td>
 *      </tr>
 *      <tr>
 *          <td>Name C</td>
 *          <td>21</td>
 *          <td>Female</td>
 *          <td><a href="#">Edit</a></td>
 *          <td><a href="#">Delete</a></td>
 *      </tr>
 *  </table>
 * 
 * <p>
 * Control links are an advanced feature of the collection renderer. A link can
 * be added and configured through a set of properties.
 * 
 * <ul>
 *  <li>{@linkplain #setLink(String, String) link}</li>
 *  <li>{@linkplain #setModule(String, String) module}</li>
 *  <li>{@linkplain #setParam(String, String) param}</li>
 *  <li>{@linkplain #setKey(String, String) key}</li>
 *  <li>{@linkplain #setBundle(String, String) bundle}</li>
 *  <li>{@linkplain #setText(String, String) text}</li>
 *  <li>{@linkplain #setOrder(String, String) order}</li>
 * </ul>
 * 
 * As you may want to specify a different value for each property and for each
 * link you need, for example, to distinguish between the <code>key</code>
 * property of the Edit control link, and the <code>key</code> property of the
 * Delete control link. That's why each property as the format
 * <em>&lt;property&gt;(String, String)</em>. It means that you can separate
 * properties by name and use <code>key(a)</code> and <code>bundle(a)</code>
 * to refer to the key and bundle properties of the link you named "a".
 * 
 * @author cfgi
 */
public class CollectionRenderer extends OutputRenderer {
    private String caption;

    private String rowClasses;

    private String columnClasses;

    private String headerClasses;

    private String prefixes;
    
    private String suffixes;
    
    private boolean checkable;
    
    private String checkboxName;
    
    private String checkboxValue;
    
    private boolean selectAllShown;
    
    private String selectAllLocation;
    
    private static final String LOCATION_BOTTOM = "bottom";
    private static final String LOCATION_TOP = "top";
    private static final String LOCATION_BOTH = "both";
    
    private Boolean displayHeaders = Boolean.TRUE;

    private Map<String, TableLink> links;
    
    private List<TableLink> sortedLinks;

    private String sortBy;
    
    private boolean groupLinks;
    
    private String linkGroupSeparator;
    
    private boolean rowForLinks;
    
    public CollectionRenderer() {
        super();
        
        this.links = new Hashtable<String, TableLink>();
        this.sortedLinks = new ArrayList<TableLink>();
        this.selectAllLocation = LOCATION_BOTTOM;
    }

    public String getCaption() {
        return caption;
    }

    /**
     * The table caption.
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
     * The classes to be used in the table columns. You can separate classes
     * with commas. The value "class1" means that all columns will have the
     * specified class. The value "class1,class2" means that columns will have
     * "class1" or "class2" as their class depending of their position. The
     * first column will have "class1", the second "class2", the third "class1",
     * etc. The only way to specify a class for the first and last column is to
     * give empty classes to all the middle columns. So if you have 3 columns, a
     * value of "class1,,class2" would put "class1" in the first column and
     * "class2" in the third and last column.
     * 
     * @property
     */
   public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public String getHeaderClasses() {
        return headerClasses;
    }

    /**
     * The value of the class attribute of each cell in the table's header.
     * There is no repetition as in
     * {@linkplain #setColumnClasses(String) columnClasses} or
     * {@linkplain #setRowClasses(String) rowClasses}.
     * 
     * @property
     */
    public void setHeaderClasses(String headerClasses) {
        this.headerClasses = headerClasses;
    }

    public String getRowClasses() {
        return rowClasses;
    }

    /**
     * With this property you can choose if you want to display headers or not
     * in the table
     * 
     * @property
     */
    public boolean getDisplayHeaders() {
        return displayHeaders;
    }

    public void setDisplayHeaders(boolean displayHeaders) {
        this.displayHeaders = displayHeaders;
    }

    /**
     * The classes to be used for each row in the table. In a similar way to
     * {@link #setColumnClasses(String) columnClasses} you can use a repeating
     * pattern for the classes. A value like "gray," could make each alternating
     * row be shaded.
     * 
     * @property
     */
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    public String getSuffixes() {
        return this.suffixes;
    }

    /**
     * This property allows you to specify a string to be included as a suffix
     * of each cell in the table. The suffix value can have a repeating pattern
     * as {@linkplain #setRowClasses(String) rowClasses}. This means that, for
     * the example above, a suffix of <code>"years old"</code> would produce
     * rows like:
     * 
     * <table border="1">
     *  <tr>
     *      <td>Name A</td>
     *      <td>20 years old</td>
     *      <td>Female</td>
     *      <td><a href="#">Edit</a></td>
     *      <td><a href="#">Delete</a></td>
     *  </tr>
     * </table>
     * 
     * @property
     */
    public void setSuffixes(String suffixes) {
        this.suffixes = suffixes;
    }
    
    public String getPrefixes() {
        return this.prefixes;
    }

    /**
     * This property is similar to {@linkplain #setSuffixes(String) suffixes}
     * with the difference that the text is inlcuded as prefix of each cell
     * content.
     *
     * @property
     */
    public void setPrefixes(String prefixes) {
        this.prefixes = prefixes;
    }

    private String getStringPart(String string, int index) {
        if (string == null) {
            return null;
        }
        
        String[] stringParts = string.split(",");
        return stringParts[index % stringParts.length];
    }
    
    private TableLink getTableLink(String name) {
        TableLink tableLink = this.links.get(name);
        
        if (tableLink == null) {
            tableLink = new TableLink(name);
    
            this.links.put(name, tableLink);
            this.sortedLinks.add(tableLink);
        }
        
        return tableLink;
    }
    
    private TableLink getTableLink(int order) {
        Collections.sort(this.sortedLinks);
        
        return this.sortedLinks.get(order);
    }

    public String getLink(String name) {
        return getTableLink(name).getLink();
    }
    
    /**
     * The link property indicates the page to were the control link will point.
     * All params will be appended to this link.
     * 
     * @property
     */
    public void setLink(String name, String value) {
        getTableLink(name).setLink(value);
    }
    
    public String getModule(String name) {
        return getTableLink(name).getModule();
    }
    
    /**
     * By default the link property will be mapped to the current module. You
     * can override that with this property.
     * 
     * @property
     */
    public void setModule(String name, String value) {
        getTableLink(name).setModule(value);
    }

    public String getParam(String name) {
        return getTableLink(name).getParam();
    }
    
    /**
     * The <code>param</code> property allows you to indicate will values of
     * the object shown in a given row should be used to configure the link
     * specified.
     * 
     * <p>
     * Imagine you want to add and link that sends you to a page were you can
     * edit the object. The link forwards to an action and that action needs to
     * know which is the object you want ot edit. Supposing that each object as
     * an <code>id</code> you may have a configuration similar to:
     * 
     * <pre>
     *   link(edit)  = &quot;/edit.do&quot;
     *   param(edit) = &quot;id&quot;
     * </pre> 
     * 
     * The result will be a link that will point to 
     * <code>&lt;module&gt;/edit.do?id=&lt;object id&gt:</code> were the id
     * param will be different for each object shown in the table.
     * 
     * <p>
     * The <code>param</code> property supports two more features. It allows
     * you to choose the name of the link parameter and explicitly give new
     * parameters. You can specify several parameters by separating the with a
     * comma. The full syntax of the <code>param</code> property is:
     * 
     * <pre>
     *   &lt;slot&gt;[/&lt;name&gt;]?[=&lt;value&gt;]?
     * </pre>
     * 
     * <dl>
     *  <dt><code>slot</code></dt>
     * <dd> specifies the name of the object's slot from were the value will be
     * retrieved. In the example above each object needed to have a
     * <code>getId()</code> method.</dd>
     * 
     *  <dt><code>name</code></dt>
     * <dd>specifies the name of the parameters that will appended to the link.
     * If this parts is not given the slot name will be used.</dd>
     * 
     *  <dt><code>value</code></dt>
     * <dd>allows you to override the value of the parameters. If you specify
     * this part then <code>slot</code> does not need to be a real slot of the
     * object.</dd>
     * </dl>
     * 
     * @property
     */
    public void setParam(String name, String value) {
        getTableLink(name).setParam(value);
    }

    public String getKey(String name) {
        return getTableLink(name).getKey();
    }
    
    /**
     * The resource key that will be used to find the link name, that is, the
     * name that will appear in the table.
     * 
     * @property
     */
   public void setKey(String name, String value) {
        getTableLink(name).setKey(value);
    }

    public String getBundle(String name) {
        return getTableLink(name).getBundle();
    }
    
    /**
     * If the module's default bundle is not to be used you can indicate the
     * alternative bundle with this property.
     * 
     * @property
     */
    public void setBundle(String name, String value) {
        getTableLink(name).setBundle(value);
    }

    public String getText(String name) {
        return getTableLink(name).getText();
    }
    
    /**
     * An alternative to the {@link #setKey(String, String) key} property is
     * specifying the text to appear directly. Oviously this approach does not
     * work well with internationalized interfaces.
     * 
     * @property
     */
    public void setText(String name, String value) {
        getTableLink(name).setText(value);
    }

    public String getOrder(String name) {
        return getTableLink(name).getOrder();
    }
    
    /**
     * As the container make no guarantees about the order properties are set in
     * the implementation we can't rely on the order links appear defined in the
     * page. You can use this attribute to explicitly indicate the order link
     * should appear. The value is not important and it's only used for an
     * alfabethic comparison. Both the following examples indicate that the
     * links should appear in the order: a, c, b.
     * <p>
     * Example 1:
     * 
     * <pre>
     *   order(a) = &quot;1&quot;
     *   order(b) = &quot;3&quot;
     *   order(c) = &quot;2&quot;
     * </pre>
     * 
     * <p>
     * Example 2:
     * 
     * <pre>
     *   order(a) = &quot;first&quot;
     *   order(b) = &quot;second&quot;
     *   order(c) = &quot;third&quot;
     * </pre> 
     * 
     * @property
     */
    public void setOrder(String name, String value) {
        getTableLink(name).setOrder(value);
    }

    public boolean isExcludedFromFirst(String name) {
        return getTableLink(name).isExcludeFromFirst();
    }
    
    /**
     * This property allows you to exclude a control link from appearing in the
     * first line of the generated table.
     * 
     * @property
     */
    public void setExcludedFromFirst(String name, String value) {
        getTableLink(name).setExcludeFromFirst(new Boolean(value));
    }
    
    public boolean isExcludedFromLast(String name) {
        return getTableLink(name).isExcludeFromLast();
    }

    /**
     * Specifies the name of the property that will consulted to determine if
     * the link should be visible or not. By default all links are visible.
     * <p>
     * This can be used to have some links that depend on domain logic. 
     * 
     * @property
     */
    public void setVisibleIf(String name, String value) {
        getTableLink(name).setVisibleIf(value);
    }
    
    public String getVisibleIf(String name) {
        return getTableLink(name).getVisibleIf();
    }
    
    /**
     * This property does the same work as visibleIf but does the opposite
     * logic. If <code>true</code> then the link will not be shown.
     * 
     * @property
     */
    public void setVisibleIfNot(String name, String value) {
        getTableLink(name).setVisibleIfNot(value);
    }
    
    public String getVisibleIfNot(String name) {
        return getTableLink(name).getVisibleIfNot();
    }

    /**
     * This property allows you to exclude a control link from appearing in the
     * last line of the generated table.
     * 
     * @property
     */
    public void setExcludedFromLast(String name, String value) {
        getTableLink(name).setExcludeFromLast(new Boolean(value));
    }
    
    public String getSortBy() {
        return this.sortBy;
    }

    /**
     * With this property you can set the criteria used to sort the collection
     * beeing presented. The accepted syntax for the criteria can be seen in
     * {@link RenderUtils#sortCollectionWithCriteria(Collection, String)}.
     * 
     * @property
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isGroupLinks() {
		return groupLinks;
	}

    /**
     * Specifies if the control links ares grouped in a single cell of the table.
     * The linkGroupSeparator will be used to separate the control links.
     * 
     * @property
     */
	public void setGroupLinks(boolean groupLinks) {
		this.groupLinks = groupLinks;
	}

	public String getLinkGroupSeparator() {
		return linkGroupSeparator;
	}

    /**
     * Specifies the separator between links when these are grouped
     * 
     * @property
     */
	public void setLinkGroupSeparator(String linkGroupSeparator) {
		this.linkGroupSeparator = linkGroupSeparator;
	}

	public boolean isRowForLinks() {
        return this.rowForLinks;
    }

    /**
     * Indicates that the control links should be generated in their own row.
     * This forces the links to be grouped and the
     * {@link #setLinkGroupSeparator(String) link group separator} to be used so
     * you should define that.
     * 
     * @property
     */
    public void setRowForLinks(boolean rowForLinks) {
        this.rowForLinks = rowForLinks;
        setGroupLinks(true);
    }

    public boolean isCheckable() {
        return this.checkable;
    }

    /**
     * This property indicates that each element wil be checkable. This means 
     * that a check box will be generated in each row allowing to select that
     * row. The user will be responsible for wrapping the whole table in a form
     * and submiting the form.
     * 
     * @property
     */
    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public String getCheckboxName() {
        return this.checkboxName;
    }

    /**
     * Selects the name of the checkbox when it's generated. 
     * 
     * @property
     */
    public void setCheckboxName(String checkboxName) {
        this.checkboxName = checkboxName;
    }

    public String getCheckboxValue() {
        return this.checkboxValue;
    }

    /**
     * The check box value is in fact a property of the object beeing displayed
     * in the row. This is usefull, for example, to have as value an identificer
     * of the object.
     * 
     * @property
     */
    public void setCheckboxValue(String checkboxValue) {
        this.checkboxValue = checkboxValue;
    }

    public boolean isSelectAllShown() {
        return this.selectAllShown;
    }

    /**
     * Indicates that a link to select all checkboxes should be included in the
     * presentation. This property is only valid when the
     * {@link #setCheckable(boolean) checkable} is <code>true</code>.
     * 
     * @property
     */
    public void setSelectAllShown(boolean selectAllShown) {
        this.selectAllShown = selectAllShown;
    }

    public String getSelectAllLocation() {
        return this.selectAllLocation;
    }

    /**
     * Chooses where the link should be placed. You can indicate the locations
     * as text and the options are <code>top</code>, <code>bottom</code>,
     * or <code>both</code>.
     * 
     * <p>
     * Example: <br/> <code>selectAllLocation=both</code> <br/>
     * <code>selectAllLocation=top, bottom</code> <br/>
     * <code>selectAllLocation=bottom</code> (the default)<br/>
     * 
     * @property
     */
    public void setSelectAllLocation(String selectAllLocation) {
        this.selectAllLocation = selectAllLocation;
        
        if (this.selectAllLocation != null) {
            this.selectAllLocation = this.selectAllLocation.toLowerCase();
        }
    }

    public String getLinkFormat(String name) {
        return getTableLink(name).getLinkFormat();
    }

    /**
     * The linkFormat property indicates the format of the control link. The
     * params should be inserted in format. When this property is set, the link
     * and param properties are ignored
     * 
     * <p>
     * Example 1:
     * 
     * <pre>
     *              someAction.do?method=viewDetails&amp;idInternal=${idInternal}
     * </pre>
     * 
     * <p>
     * Example 2:
     * 
     * <pre>
     *            someAction/${someProperty}/${otherProperty}
     * </pre>
     * 
     * @property
     */
    public void setLinkFormat(String name, String value) {
        getTableLink(name).setLinkFormat(value);
    }

    public String getCustomLink(String name) {
        return getTableLink(name).getCustom();
    }

    /**
     * If this property is specified all the others are ignored. This property
     * allows you to specify the exact content to show as a link. Then content
     * will be parsed with the same rules that apply to 
     * {@link #setLinkFormat(String, String) linkFormat}.
     * 
     * @property
     */
    public void setCustomLink(String name, String value) {
        getTableLink(name).setCustom(value);
    }
    
    public String getContextRelative(String name) {
        return Boolean.toString(getTableLink(name).isContextRelative());
    }

    /**
     * The contextRelative property indicates if the specified link is relative
     * to the current context or is an external link (e.g.
     * https://anotherserver.com/anotherScript)
     * 
     * @property
     */
    public void setContextRelative(String name, String value) {
        getTableLink(name).setContextRelative(Boolean.parseBoolean(value));
    }

    protected int getNumberOfLinkColumns() {
        if (isRowForLinks()) {
            return 0;
        }
        else if (isGroupLinks() && this.links.size() > 0) {
            return 1;
        } else {
            return this.links.size();
        }
    }
    
    protected int getNumberOfLinks() {
        return this.links.size();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        Collection sortedCollection = RenderUtils.sortCollectionWithCriteria((Collection) object,
                getSortBy());
        
        return new CollectionTabularLayout(sortedCollection);
    }
    
    public class CollectionTabularLayout extends TabularLayout {

        List<MetaObject> metaObjects;

        public CollectionTabularLayout(Collection object) {
            this.metaObjects = getMetaObjects(object);
        }

        private List<MetaObject> getMetaObjects(Collection collection) {
            List<MetaObject> metaObjects = new ArrayList<MetaObject>();
            
            MetaObject contextMetaObject = getContext().getMetaObject();
            if (contextMetaObject instanceof MetaObjectCollection) {
                // reuse meta objects
                MetaObjectCollection multipleMetaObject = (MetaObjectCollection) getContext()
                        .getMetaObject();
                
                for (Object object : collection) {
                    for (MetaObject metaObject : multipleMetaObject.getAllMetaObjects()) {
                        if (object.equals(metaObject.getObject())) {
                            metaObjects.add(metaObject);
                            break;
                        }
                    }
                }
            } else {
                Schema schema = RenderKit.getInstance().findSchema(getContext().getSchema());
                for (Object object : collection) {
                    metaObjects.add(MetaObjectFactory.createObject(object, schema));
                }
            }
            
            return metaObjects;
        }

        @Override
        public HtmlComponent createLayout(Object object, Class type) {
            HtmlComponent component = super.createLayout(object, type);
            
            if (isCheckable() && isSelectAllShown()) {
                HtmlBlockContainer container = new HtmlBlockContainer();
                
                HtmlComponent selectAllComponent = createInvertSelectionLink();
                
                if (String.valueOf(getSelectAllLocation()).contains(LOCATION_TOP)
                        || String.valueOf(getSelectAllLocation()).contains(LOCATION_BOTH)) {
                    container.addChild(selectAllComponent);
                }

                container.addChild(component);
                
                if (String.valueOf(getSelectAllLocation()).contains(LOCATION_BOTTOM)
                        || String.valueOf(getSelectAllLocation()).contains(LOCATION_BOTH)) {
                    container.addChild(selectAllComponent);
                }
                
                return container;
            } else {
                return component;
            }
        }

        private HtmlComponent createInvertSelectionLink() {
            HtmlInlineContainer container = new HtmlInlineContainer();
            HtmlScript script = new HtmlScript();
            
            script.setContentType("text/javascript");
            script.setConditional(true);
            script.setScript("function invertSelectionAll(n) {" + "    var allChecked = true;"
                    + "    var elements = document.getElementsByName(n);" + "\n"
                    + "    for (var index=0; index<elements.length; index++) {"
                    + "        var element = elements[index];" + "\n"
                    + "        if (! element.checked) {" + "            allChecked = false;"
                    + "            element.checked = true;" + "        }" + "    }" + "\n"
                    + "    if (allChecked) {"
                    + "        for (var index=0; index<elements.length; index++) {"
                    + "            elements[index].checked = false;" + "        }" + "    }" + "}");
            
            container.addChild(script);
            
            String selectAllScript = "invertSelectionAll('" + getCheckboxName() + "')"; 

            HtmlLink link = new HtmlLink();
            link.setContextRelative(false);
            link.setText(RenderUtils.getResourceString("renderers.table.selectAll"));
            link.setOnClick(selectAllScript);
            link.setOnDblClick(selectAllScript);
            container.addChild(link);
            
            return container;
        }

        @Override
        protected boolean hasHeader() {
            return (displayHeaders) ? this.metaObjects.size() > 0 : false;
        }
        
        @Override
        protected int getNumberOfColumns() {
            if (this.metaObjects.size() > 0) {
                MetaObject metaObject = this.metaObjects.get(0);
                return metaObject.getSlots().size() + getNumberOfLinkColumns() + (isCheckable() ? 1 : 0);
            } else {
                return 0;
            }
        }

        @Override
        protected int getNumberOfRows() {
            if (isRowForLinks()) {
                return this.metaObjects.size() * 2;
            }
            else {
                return this.metaObjects.size();
            }
        }

        protected MetaObject getObject(int index) {
            return this.metaObjects.get(index);
        }
        
        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            if (columnIndex == 0 && isCheckable()) {
                return new HtmlText();
            } else if (columnIndex < getNumberOfColumns() - getNumberOfLinkColumns()) {
                String slotLabel = getLabel(columnIndex);
                return new HtmlText(slotLabel, false);
            } else {
                return new HtmlText();
            }
        }

        protected String getLabel(int columnIndex) {
            int realIndex = columnIndex - (isCheckable() ? 1 : 0);
            
            if (realIndex >= 0) {
                return getObject(0).getSlots().get(realIndex).getLabel();
            } else {
                return "";
            }
        }

        @Override
        protected void costumizeCell(HtmlTableCell cell, int rowIndex, int columnIndex) {
            super.costumizeCell(cell, rowIndex, columnIndex);
            
            if (isRowForLinks() && rowIndex % 2 == 1) { // links in isolated row
                cell.setColspan(getNumberOfColumns());
            }
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            int objectRow = isRowForLinks() ? rowIndex / 2 : rowIndex;
            
            MetaObject object = getObject(objectRow);
            getContext().setMetaObject(object);

            if (isRowForLinks() && rowIndex % 2 == 1) { // links in isolated row
                return generateLinkComponent(object, rowIndex, columnIndex);
            }
            
            if (columnIndex == 0 && isCheckable()) {
                HtmlCheckBox checkBox = new HtmlCheckBox();
                checkBox.setName(getCheckboxName());
                
                if (getCheckboxValue() != null) {
                    Object realObject = object.getObject();

                    try {
                        String checkBoxValue = String.valueOf(PropertyUtils.getProperty(realObject,
                                getCheckboxValue()));

                        boolean checked = false;
                        String[] existingValues = getContext().getViewState().getRequest()
                                .getParameterValues(getCheckboxName());

                        if (existingValues != null) {
                            for (int i = 0; i < existingValues.length; i++) {
                                String value = existingValues[i];
                                
                                if (value.equals(checkBoxValue)) {
                                    checked = true;
                                    break;
                                }
                            }
                        }
                        
                        checkBox.setChecked(checked);
                        checkBox.setUserValue(checkBoxValue);
                    } catch (Exception e) {
                        throw new RuntimeException("could not set check box value by reading property '"
                                + getCheckboxValue() + "' from object " + realObject, e);
                    }
                }
                
                return checkBox;
            } else if (columnIndex < getNumberOfColumns() - getNumberOfLinkColumns()) {
                return generateObjectComponent(columnIndex - (isCheckable() ? 1 : 0), object);
            } else {
                return generateLinkComponent(object, rowIndex, columnIndex - (getNumberOfColumns() - getNumberOfLinkColumns()));
            }
        }

        protected HtmlComponent generateObjectComponent(int columnIndex, MetaObject object) {
            MetaSlot slot = getSlotUsingName(object, columnIndex);
            
            if (slot == null) {
                return new HtmlText();
            }
            
            HtmlComponent component = renderSlot(slot);
            component = wrapPrefixAndSuffix(component, columnIndex);
         
            return component;
        }
        
        /**
         * As the renderer uses the first object as a reference to build the
         * header and fecth the information from all the objects in the
         * collection we can have troubles when the collection contains objects
         * of different types.
         * 
         * In this case we can't assume that all objects have the same slots
         * (because of an unspecified schema). So we have to search slots by
         * name and hope that it makes sense in the table.
         */
        protected MetaSlot getSlotUsingName(MetaObject object, int columnIndex) {
            MetaObject referenceObject = getObject(0);
            MetaSlot referenceSlot = referenceObject.getSlots().get(columnIndex);
            
            MetaSlot directSlot = object.getSlots().get(columnIndex); // common
                                                                        // case
            if (directSlot.getName().equals(referenceSlot.getName())) {
                return directSlot;
            }
            
            for (MetaSlot slot : object.getSlots()) {
                if (slot.getName().equals(referenceSlot.getName())) {
                    return slot;
                }
            }
            
            return null;
        }

        protected HtmlComponent generateLinkComponent(MetaObject object, int rowIndex, int number) {
            if (isGroupLinks() || isRowForLinks()) {
                HtmlInlineContainer container = new HtmlInlineContainer();
                container.setIndented(false);

                boolean includeSeparator = false;
                for (int i = 0; i < getNumberOfLinks(); i++) {
                    HtmlComponent component = generateSingleLinkComponent(object, rowIndex, i);
                    if (component != null) {
                        if (includeSeparator) {
                            container.addChild(new HtmlText(getLinkGroupSeparator(), false));
                        }

                        container.addChild(component);
                        includeSeparator = true;
                    }
                }

                return container;
            } else {
                HtmlComponent component = generateSingleLinkComponent(object, rowIndex, number);
                return component == null ? new HtmlText() : component;
            }
        }
        
        protected HtmlComponent generateSingleLinkComponent(MetaObject object, int rowIndex, int number) {
            TableLink tableLink = getTableLink(number);
            
            if (rowIndex == 0 && tableLink.isExcludeFromFirst() ) {
                return null;
            }
            
            if (rowIndex == getNumberOfRows() - 1 && tableLink.isExcludeFromLast()) {
                return null;
            }
            
            Object realObject = object.getObject();

            if (tableLink.getVisibleIf() != null) {
                try {
                    Boolean visible = (Boolean) RendererPropertyUtils.getProperty(realObject, tableLink
                            .getVisibleIf(), false);
                    
                    if (visible != null && !visible) {
                        return null;
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
            
            if (tableLink.getVisibleIfNot() != null) {
                try {
                    Boolean notVisible = (Boolean) RendererPropertyUtils.getProperty(realObject,
                            tableLink.getVisibleIfNot(), false);
                    
                    if (notVisible != null && notVisible) {
                        return null;
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }

            if (tableLink.getCustom() != null) {
                return new HtmlText(RenderUtils
                        .getFormattedProperties(tableLink.getCustom(), realObject), false);
            } else {
                HtmlLink link = new HtmlLink();
    
                if (tableLink.isContextRelativeSet()) {
                    link.setContextRelative(tableLink.isContextRelative());
                }
    
                link.setText(getLinkText(tableLink));
                link.setModule(tableLink.getModule());
    
                if (tableLink.getLinkFormat() != null) {
                    link.setUrl(RenderUtils
                            .getFormattedProperties(tableLink.getLinkFormat(), realObject));
                } else {
                    link.setUrl(tableLink.getLink());
                    setLinkParameters(object, link, tableLink);
                }
    
                return link;
            }
        }

        protected String getLinkText(TableLink tableLink) {
            String text = tableLink.getText();
            
            if (text != null) {
                return text;
            }
            
            String key = tableLink.getKey();
            String bundle = tableLink.getBundle();
            
            if (key == null) {
                return tableLink.getName();
            }
            
            text = RenderUtils.getResourceString(bundle, key);
            
            if (text != null) {
                return text;
            }
            
            return tableLink.getName();
        }

        protected void setLinkParameters(MetaObject metaObject, HtmlLink link, TableLink tableLink) {
            String linkParam = tableLink.getParam();
            
            if (linkParam == null) {
                return;
            }
            
            Object object = metaObject.getObject();
            String parameters[] = tableLink.getParam().split(",");

            // "a", "a=b", "a/b", "a/b=c" 
            for (int i = 0; i < parameters.length; i++) {
                String name = parameters[i];
         
                String slotName;
                String realName;
                String customValue;
                
                String[] parameterParts = name.split("=", -1);
                if (parameterParts.length >= 1) {
                    String[] nameParts = parameterParts[0].split("/");
                    
                    slotName = nameParts[0];
                    
                    if (nameParts.length == 2) {
                        realName = nameParts[1];
                    } else {
                        realName = slotName;
                    }
                    
                    if (parameterParts.length > 1) {
                        customValue = parameterParts[1];
                    } else {
                        customValue = null;
                    }
                } else {
                    slotName = parameterParts[0];
                    realName = parameterParts[0];
                    customValue = null;
                }
                
                try {
                    String value = customValue != null ? customValue : String.valueOf(PropertyUtils
                            .getProperty(object, slotName));
                    link.setParameter(realName, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected HtmlComponent wrapPrefixAndSuffix(HtmlComponent component, int columnIndex) {
            HtmlInlineContainer container = null;
            
            String prefix = getStringPart(getPrefixes(), columnIndex);
            if (prefix != null) {
                container = new HtmlInlineContainer();
                container.addChild(new HtmlText(prefix));
                container.addChild(component);
            }
            
            String suffix = getStringPart(getSuffixes(), columnIndex);
            if (suffix != null) {
                if (container != null) {
                    container.addChild(new HtmlText(suffix));
                } else {
                    container = new HtmlInlineContainer();
                    container.addChild(component);
                    container.addChild(new HtmlText(suffix));
                }
            }
            
            return container != null ? container : component;
        }

    }
    
    protected class TableLink implements Comparable<TableLink> {

        private String name;
        private String link;
        private String module;
        private String param;
        private String text;
        private String key;
        private String bundle;
        private String order;
        private boolean excludeFromFirst;
        private boolean excludeFromLast;
        private String linkFormat;
        private Boolean contextRelative;
        private String custom;
        private String visibleIf;
        private String visibleIfNot;

        public TableLink() {
            super();
            
            this.excludeFromFirst = false;
            this.excludeFromLast = false;
        }

        public TableLink(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBundle() {
            return this.bundle;
        }
        
        public void setBundle(String bundle) {
            this.bundle = bundle;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public void setKey(String key) {
            this.key = key;
        }
        
        public String getLink() {
            return this.link;
        }
        
        public void setLink(String link) {
            this.link = link;
        }
        
        public String getModule() {
            return this.module;
        }
        
        public void setModule(String module) {
            this.module = module;
        }
        
        public String getParam() {
            return this.param;
        }
        
        public void setParam(String param) {
            this.param = param;
        }
        
        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getOrder() {
            return this.order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public boolean isExcludeFromFirst() {
            return this.excludeFromFirst;
        }

        public void setExcludeFromFirst(boolean excludeFromFirst) {
            this.excludeFromFirst = excludeFromFirst;
        }

        public boolean isExcludeFromLast() {
            return this.excludeFromLast;
        }

        public void setExcludeFromLast(boolean excludeFromLast) {
            this.excludeFromLast = excludeFromLast;
        }

        public String getLinkFormat() {
            return linkFormat;
        }

        public void setLinkFormat(String linkFormat) {
            this.linkFormat = linkFormat;
        }

        public String getVisibleIf() {
            return this.visibleIf;
        }

        public void setVisibleIf(String visibleIf) {
            this.visibleIf = visibleIf;
        }

        public String getVisibleIfNot() {
            return this.visibleIfNot;
        }

        public void setVisibleIfNot(String visibleIfNot) {
            this.visibleIfNot = visibleIfNot;
        }

        public Boolean isContextRelative() {
            return contextRelative;
        }

        public void setContextRelative(Boolean contextRelative) {
            this.contextRelative = contextRelative;
        }

        public boolean isContextRelativeSet() {
            return this.contextRelative != null;
        }

        public String getCustom() {
            return this.custom;
        }

        public void setCustom(String custom) {
            this.custom = custom;
        }

        public int compareTo(TableLink other) {
            if (getOrder() == null) {
                return 0;
            }
            
            if (other.getOrder() == null) {
                return 0;
            }
            
            return getOrder().compareTo(other.getOrder());
        }
    }
    
}
