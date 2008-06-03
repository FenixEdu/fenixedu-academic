package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.CollectionRenderer.TableLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class CompetenceCourseVersionList extends OutputRenderer {

    private String approvedClass;

    private String draftClass;

    private String publishedClass;

    private String tableClasses;

    private String scientificAreaNameClasses;

    private String groupNameClasses;

    private String courseNameClasses;

    private String filterBy;

    private Map<String, TableLink> links;

    private List<TableLink> sortedLinks;

    private boolean groupLinks;

    private String linkGroupSeparator;

    public String getFilterBy() {
	return filterBy;
    }

    public void setFilterBy(String filterBy) {
	this.filterBy = filterBy;
    }

    public String getCourseNameClasses() {
	return courseNameClasses;
    }

    public void setCourseNameClasses(String courseNameClasses) {
	this.courseNameClasses = courseNameClasses;
    }

    public String getScientificAreaNameClasses() {
	return scientificAreaNameClasses;
    }

    public void setScientificAreaNameClasses(String scientificAreaNameClasses) {
	this.scientificAreaNameClasses = scientificAreaNameClasses;
    }

    public String getTableClasses() {
	return tableClasses;
    }

    public void setTableClasses(String tableClasses) {
	this.tableClasses = tableClasses;
    }

    public String getApprovedClass() {
	return approvedClass;
    }

    public void setApprovedClass(String approvedClass) {
	this.approvedClass = approvedClass;
    }

    public String getDraftClass() {
	return draftClass;
    }

    public void setDraftClass(String draftClass) {
	this.draftClass = draftClass;
    }

    public String getPublishedClass() {
	return publishedClass;
    }

    public void setPublishedClass(String publishedClass) {
	this.publishedClass = publishedClass;
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
         * The link property indicates the page to were the control link will
         * point. All params will be appended to this link.
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
         * By default the link property will be mapped to the current module.
         * You can override that with this property.
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
         * The <code>param</code> property allows you to indicate will values
         * of the object shown in a given row should be used to configure the
         * link specified.
         * 
         * <p>
         * Imagine you want to add and link that sends you to a page were you
         * can edit the object. The link forwards to an action and that action
         * needs to know which is the object you want ot edit. Supposing that
         * each object as an <code>id</code> you may have a configuration
         * similar to:
         * 
         * <pre>
         *   link(edit)  = &quot;/edit.do&quot;
         *   param(edit) = &quot;id&quot;
         * </pre>
         * 
         * The result will be a link that will point to
         * <code>&lt;module&gt;/edit.do?id=&lt;object id&gt:</code> were the
         * id param will be different for each object shown in the table.
         * 
         * <p>
         * The <code>param</code> property supports two more features. It
         * allows you to choose the name of the link parameter and explicitly
         * give new parameters. You can specify several parameters by separating
         * the with a comma. The full syntax of the <code>param</code>
         * property is:
         * 
         * <pre>
         *   &lt;slot&gt;[/&lt;name&gt;]?[=&lt;value&gt;]?
         * </pre>
         * 
         * <dl>
         * <dt><code>slot</code></dt>
         * <dd> specifies the name of the object's slot from were the value will
         * be retrieved. In the example above each object needed to have a
         * <code>getId()</code> method.</dd>
         * 
         * <dt><code>name</code></dt>
         * <dd>specifies the name of the parameters that will appended to the
         * link. If this parts is not given the slot name will be used.</dd>
         * 
         * <dt><code>value</code></dt>
         * <dd>allows you to override the value of the parameters. If you
         * specify this part then <code>slot</code> does not need to be a real
         * slot of the object.</dd>
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
         * The resource key that will be used to find the link name, that is,
         * the name that will appear in the table.
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
         * specifying the text to appear directly. Oviously this approach does
         * not work well with internationalized interfaces.
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
         * As the container make no guarantees about the order properties are
         * set in the implementation we can't rely on the order links appear
         * defined in the page. You can use this attribute to explicitly
         * indicate the order link should appear. The value is not important and
         * it's only used for an alfabethic comparison. Both the following
         * examples indicate that the links should appear in the order: a, c, b.
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
         * This property allows you to exclude a control link from appearing in
         * the first line of the generated table.
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
         * Specifies the name of the property that will consulted to determine
         * if the link should be visible or not. By default all links are
         * visible.
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
         * This property allows you to exclude a control link from appearing in
         * the last line of the generated table.
         * 
         * @property
         */
    public void setExcludedFromLast(String name, String value) {
	getTableLink(name).setExcludeFromLast(new Boolean(value));
    }

    public boolean isGroupLinks() {
	return groupLinks;
    }

    /**
         * Specifies if the control links ares grouped in a single cell of the
         * table. The linkGroupSeparator will be used to separate the control
         * links.
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

    public String getLinkFormat(String name) {
	return getTableLink(name).getLinkFormat();
    }

    /**
         * The linkFormat property indicates the format of the control link. The
         * params should be inserted in format. When this property is set, the
         * link and param properties are ignored
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
         * If this property is specified all the others are ignored. This
         * property allows you to specify the exact content to show as a link.
         * Then content will be parsed with the same rules that apply to
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
         * The contextRelative property indicates if the specified link is
         * relative to the current context or is an external link (e.g.
         * https://anotherserver.com/anotherScript)
         * 
         * @property
         */
    public void setContextRelative(String name, String value) {
	getTableLink(name).setContextRelative(Boolean.parseBoolean(value));
    }

    public CompetenceCourseVersionList() {
	this.links = new Hashtable<String, TableLink>();
	this.sortedLinks = new ArrayList<TableLink>();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new ComputerCourseVersionLayout();
    }

    private class ComputerCourseVersionLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    Department department = (Department) object;
	    HtmlBlockContainer container = new HtmlBlockContainer();

	    for (ScientificAreaUnit scientificArea : department.getDepartmentUnit()
		    .getScientificAreaUnits()) {

		HtmlText areaName = new HtmlText(scientificArea.getNameI18n().getContent());
		if (getScientificAreaNameClasses() != null) {
		    areaName.setClasses(getScientificAreaNameClasses());
		}
		container.addChild(areaName);
		HtmlList list = new HtmlList();

		CurricularStage stage = null;

		if (getFilterBy() != null) {
		    stage = CurricularStage.valueOf(getFilterBy());
		}
		for (CompetenceCourseGroupUnit group : scientificArea.getCompetenceCourseGroupUnits()) {

		    HtmlListItem item = list.createItem();
		    HtmlBlockContainer courseContainer = new HtmlBlockContainer();
		    HtmlText groupName = new HtmlText(group.getPresentationName());
		    if (getGroupNameClasses() != null) {
			groupName.setClasses(getGroupNameClasses());
		    }
		    courseContainer.addChild(groupName);
		    HtmlTable table = new HtmlTable();
		    for (CompetenceCourse course : group.getCompetenceCourses()) {
			if (course.getCurricularStage().equals(stage)) {
			    HtmlTableRow courseRow = table.createRow();
			    HtmlComponent coursePresentation = getCoursePresentation(course);
			    if (getCourseNameClasses() != null) {
				coursePresentation.setClasses(getCourseNameClasses());
			    }
			    courseRow.createCell().setBody(coursePresentation);
			    HtmlTableCell cell = courseRow.createCell();
			    cell.setBody(getLinks(course));
			    cell.setClasses("aright");
			}
		    }
		    courseContainer.addChild(table);
		    if (getTableClasses() != null) {
			table.setClasses(getTableClasses());
		    }

		    item.setBody(courseContainer);
		}
		container.addChild(list);
	    }

	    return container;
	}

	protected HtmlComponent getLinks(CompetenceCourse course) {
	    HtmlInlineContainer container = new HtmlInlineContainer();
	    int total = sortedLinks.size();
	    for (int i = 0; i < total; i++) {
		TableLink tableLink = getTableLink(i);
		container.addChild(tableLink.generateLink(course));
		if (i + 1 < total) {
		    container.addChild(new HtmlText(getLinkGroupSeparator()));
		}
	    }
	    container.setIndented(false);
	    return container;

	}

	private HtmlComponent getCoursePresentation(CompetenceCourse course) {
	    HtmlInlineContainer container = new HtmlInlineContainer();
	    container.addChild(new HtmlText(course.getName()));
	    container.addChild(new HtmlText(" ("));
	    container.addChild(new HtmlText(course.getAcronym()));
	    container.addChild(new HtmlText(") "));
	    container.addChild(getStage(course.getCurricularStage()));
	    container.setIndented(false);
	    return container;
	}

	private HtmlComponent getStage(CurricularStage curricularStage) {
	    HtmlText text = new HtmlText(RenderUtils.getEnumString(curricularStage));

	    switch (curricularStage) {
	    case DRAFT:
		text.setClasses(getDraftClass());
		break;
	    case PUBLISHED:
		text.setClasses(getPublishedClass());
		break;
	    case APPROVED:
		text.setClasses(getApprovedClass());
		break;
	    }

	    return text;
	}

    }

    public String getGroupNameClasses() {
	return groupNameClasses;
    }

    public void setGroupNameClasses(String groupNameClasses) {
	this.groupNameClasses = groupNameClasses;
    }

}
