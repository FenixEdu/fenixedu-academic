/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.CollectionRenderer.TableLink;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.Face;
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

    private final Map<String, TableLink> links;

    private final List<TableLink> sortedLinks;

    private boolean groupLinks;

    private String linkGroupSeparator;

    private boolean showOldCompetenceCourses;

    private String messageClass;

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

    public String getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(String messageClass) {
        this.messageClass = messageClass;
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
     * The link property indicates the page to were the control link will point.
     * All params will be appended to this link.
     * 
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
     */
    public void setModule(String name, String value) {
        getTableLink(name).setModule(value);
    }

    public String getParam(String name) {
        return getTableLink(name).getParam();
    }

    /**
     * The <code>param</code> property allows you to indicate will values of the
     * object shown in a given row should be used to configure the link
     * specified.
     * 
     * <p>
     * Imagine you want to add and link that sends you to a page were you can edit the object. The link forwards to an action and
     * that action needs to know which is the object you want ot edit. Supposing that each object as an <code>id</code> you may
     * have a configuration similar to:
     * 
     * <pre>
     *   link(edit)  = &quot;/edit.do&quot;
     *   param(edit) = &quot;id&quot;
     * </pre>
     * 
     * The result will be a link that will point to <code>&lt;module&gt;/edit.do?id=&lt;object id&gt;</code> were the id param
     * will be different for each object shown in the table.
     * 
     * <p>
     * The <code>param</code> property supports two more features. It allows you to choose the name of the link parameter and
     * explicitly give new parameters. You can specify several parameters by separating the with a comma. The full syntax of the
     * <code>param</code> property is:
     * 
     * <pre>
     *   &lt;slot&gt;[/&lt;name&gt;]?[=&lt;value&gt;]?
     * </pre>
     * 
     * <dl>
     * <dt><code>slot</code></dt>
     * <dd>specifies the name of the object's slot from were the value will be retrieved. In the example above each object needed
     * to have a <code>getId()</code> method.</dd>
     * 
     * <dt><code>name</code></dt>
     * <dd>specifies the name of the parameters that will appended to the link. If this parts is not given the slot name will be
     * used.</dd>
     * 
     * <dt><code>value</code></dt>
     * <dd>allows you to override the value of the parameters. If you specify this part then <code>slot</code> does not need to be
     * a real slot of the object.</dd>
     * </dl>
     * 
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
     */
    public void setExcludedFromLast(String name, String value) {
        getTableLink(name).setExcludeFromLast(new Boolean(value));
    }

    public boolean isGroupLinks() {
        return groupLinks;
    }

    /**
     * Specifies if the control links ares grouped in a single cell of the
     * table. The linkGroupSeparator will be used to separate the control links.
     * 
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
     */
    public void setLinkGroupSeparator(String linkGroupSeparator) {
        this.linkGroupSeparator = linkGroupSeparator;
    }

    public boolean isShowOldCompetenceCourses() {
        return showOldCompetenceCourses;
    }

    /**
     * Indicates whether or not to show old competence courses
     * 
     */
    public void setShowOldCompetenceCourses(boolean showOldCompetenceCourses) {
        this.showOldCompetenceCourses = showOldCompetenceCourses;
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
     *              someAction.do?method=viewDetails&amp;externalId=${externalId}
     * </pre>
     * 
     * <p>
     * Example 2:
     * 
     * <pre>
     *            someAction/${someProperty}/${otherProperty}
     * </pre>
     * 
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
     * will be parsed with the same rules that apply to {@link #setLinkFormat(String, String) linkFormat}.
     * 
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

        private static final String CAPTION_CLASSES = "mtop15 mbottom05";

        private boolean futureDepartmentMessageShown = false;

        private boolean futureGroupMessageShown = false;

        private boolean futureTransferMessageShown = false;

        private boolean oldDepartmentMessageShown = false;

        private void setFutureDepartmentMessageShown(boolean futureDepartmentMessageShown) {
            this.futureDepartmentMessageShown = futureDepartmentMessageShown;
        }

        private boolean isFutureDepartmentMessageShown() {
            return futureDepartmentMessageShown;
        }

        private void setFutureGroupMessageShown(boolean futureGroupMessageShown) {
            this.futureGroupMessageShown = futureGroupMessageShown;
        }

        private boolean isFutureGroupMessageShown() {
            return futureGroupMessageShown;
        }

        private void setFutureTransferMessageShown(boolean futureTransferMessageShown) {
            this.futureTransferMessageShown = futureTransferMessageShown;
        }

        private boolean isFutureTransferMessageShown() {
            return futureTransferMessageShown;
        }

        private void setOldDepartmentMessageShown(boolean oldDepartmentMessageShown) {
            this.oldDepartmentMessageShown = oldDepartmentMessageShown;
        }

        private boolean isOldDepartmentMessageShown() {
            return oldDepartmentMessageShown;
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            Department department = (Department) object;
            HtmlBlockContainer listContainer = new HtmlBlockContainer();

//            final List<ScientificAreaUnit> scientificAreaUnits = department.getDepartmentUnit().getScientificAreaUnits();

            final List<Unit> scientificAreaUnits = department.getDepartmentUnit().getSubUnits().stream()
                    .filter(u -> u.isScientificAreaUnit()).sorted(Party.COMPARATOR_BY_NAME_AND_ID).collect(Collectors.toList());

            for (Unit scientificArea : scientificAreaUnits) {

                HtmlText areaName = new HtmlText(scientificArea.getNameI18n().getContent());
                if (getScientificAreaNameClasses() != null) {
                    areaName.setClasses(getScientificAreaNameClasses());
                }
                HtmlList list = new HtmlList();

                CurricularStage stage = null;

                if (getFilterBy() != null) {
                    stage = CurricularStage.valueOf(getFilterBy());
                }

//                List<CompetenceCourseGroupUnit> competenceCourseGroupUnits = scientificArea.getCompetenceCourseGroupUnits();
                final List<Unit> competenceCourseGroupUnits = scientificArea.getSubUnits().stream()
                        .filter(u -> u.isCompetenceCourseGroupUnit()).filter(u -> u instanceof CompetenceCourseGroupUnit) // double check of instance to avoid class cast exceptions ahead
                        .sorted(Party.COMPARATOR_BY_NAME_AND_ID).collect(Collectors.toList());

                for (Unit group : competenceCourseGroupUnits) {

                    HtmlListItem item = list.createItem();
                    HtmlBlockContainer courseContainer = new HtmlBlockContainer();
                    HtmlText groupName = new HtmlText(group.getPresentationName());
                    if (getGroupNameClasses() != null) {
                        groupName.setClasses(getGroupNameClasses());
                    }
                    courseContainer.addChild(groupName);
                    HtmlTable table = new HtmlTable();
                    for (CompetenceCourse course : getCurrentOrFutureCompetenceCourses((CompetenceCourseGroupUnit) group)) {
                        if (course.getCurricularStage().equals(stage)) {
                            HtmlTableRow courseRow = table.createRow();
                            HtmlComponent coursePresentation =
                                    getCurrentOrFutureCoursePresentation(course, group, department.getDepartmentUnit());
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
                listContainer.addChild(areaName);
                listContainer.addChild(list);
            }
            HtmlBlockContainer container = new HtmlBlockContainer();
            container.addChild(getCaptionPresentation());
            container.addChild(listContainer);

            return container;
        }

        private List<CompetenceCourse> getCurrentOrFutureCompetenceCourses(final CompetenceCourseGroupUnit unit) {
            final SortedSet<CompetenceCourse> result =
                    new TreeSet<CompetenceCourse>(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
            for (CompetenceCourseInformation competenceInformation : unit.getCompetenceCourseInformationsSet()) {
                if (competenceInformation.getCompetenceCourse().getCompetenceCourseGroupUnit() == unit) {
                    result.add(competenceInformation.getCompetenceCourse());
                }
                if (competenceInformation.getCompetenceCourse()
                        .getCompetenceCourseGroupUnit(ExecutionInterval.findLastChild()) == unit) {
                    result.add(competenceInformation.getCompetenceCourse());
                }
            }
            return new ArrayList<CompetenceCourse>(result);
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

        private HtmlComponent getCurrentOrFutureCoursePresentation(CompetenceCourse course, Unit group,
                DepartmentUnit department) {
            HtmlInlineContainer container = new HtmlInlineContainer();
            if (!StringUtils.isEmpty(course.getCode())) {
                container.addChild(new HtmlText(course.getCode()));
                container.addChild(new HtmlText(" - "));
            }
            container.addChild(new HtmlText(course.getName()));
            container.addChild(new HtmlText(" ("));
            container.addChild(new HtmlText(course.getAcronym()));
            container.addChild(new HtmlText(") "));
            container.addChild(getStage(course.getCurricularStage()));
            if (course.getCompetenceCourseGroupUnit() != group) {
                container.addChild(new HtmlText(" ("));
                container.addChild(getFutureTransferMessage(course));
                container.addChild(new HtmlText(") "));
            } else if (course.getDepartmentUnit(ExecutionInterval.findLastChild()) != department) {
                container.addChild(new HtmlText(" ("));
                container.addChild(getFutureDepartmentMessage(course));
                container.addChild(new HtmlText(") "));
            } else if (course.getCompetenceCourseGroupUnit(ExecutionInterval.findLastChild()) != group) {
                container.addChild(new HtmlText(" ("));
                container.addChild(getFutureGroupMessage(course));
                container.addChild(new HtmlText(") "));
            }
            container.setIndented(false);
            return container;
        }

        private HtmlComponent getFutureDepartmentMessage(CompetenceCourse course) {
            setFutureDepartmentMessageShown(true);
            HtmlText text = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.department") + ": "
                    + course.getDepartmentUnit(ExecutionInterval.findLastChild()).getAcronym());
            text.setClasses(getMessageClass());
            return text;
        }

        private HtmlComponent getFutureGroupMessage(CompetenceCourse course) {
            setFutureGroupMessageShown(true);
            HtmlText text = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.group") + ": "
                    + course.getCompetenceCourseGroupUnit(ExecutionInterval.findLastChild()).getName());
            text.setClasses(getMessageClass());
            return text;
        }

        private HtmlComponent getFutureTransferMessage(CompetenceCourse course) {
            setFutureTransferMessageShown(true);
            HtmlText text = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.transfer"));
            text.setClasses(getMessageClass());
            return text;
        }

        private HtmlComponent getOldCoursePresentation(CompetenceCourse course) {
            HtmlInlineContainer container = new HtmlInlineContainer();
            if (!StringUtils.isEmpty(course.getCode())) {
                container.addChild(new HtmlText(course.getCode()));
                container.addChild(new HtmlText(" - "));
            }
            container.addChild(new HtmlText(course.getName()));
            container.addChild(new HtmlText(" ("));
            container.addChild(new HtmlText(course.getAcronym()));
            container.addChild(new HtmlText(") "));
            container.addChild(getStage(course.getCurricularStage()));
            container.addChild(new HtmlText(" ("));
            container.addChild(getOldDepartmentMessage(course));
            container.addChild(new HtmlText(")"));
            container.setIndented(false);
            return container;
        }

        private HtmlComponent getOldDepartmentMessage(CompetenceCourse course) {
            setOldDepartmentMessageShown(true);
            HtmlText text = new HtmlText(
                    BundleUtil.getString(Bundle.BOLONHA, "current.department") + ": " + course.getDepartmentUnit().getAcronym());
            text.setClasses(getMessageClass());
            return text;
        }

        private HtmlComponent getCaptionPresentation() {
            HtmlBlockContainer container = new HtmlBlockContainer();
            if (isFutureDepartmentMessageShown() || isFutureGroupMessageShown() || isFutureTransferMessageShown()
                    || isOldDepartmentMessageShown()) {
                HtmlText caption = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "caption"));
                caption.setClasses(CAPTION_CLASSES);
                caption.setFace(Face.STRONG);
                container.addChild(caption);
                HtmlList list = new HtmlList();
                if (isOldDepartmentMessageShown()) {
                    HtmlListItem item = list.createItem();
                    HtmlInlineContainer messageLine = new HtmlInlineContainer();
                    HtmlText message = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "current.department") + ":");
                    message.setFace(Face.EMPHASIS);
                    messageLine.addChild(message);
                    messageLine.addChild(new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "current.department.caption")));
                    item.addChild(messageLine);
                }
                if (isFutureDepartmentMessageShown()) {
                    HtmlListItem item = list.createItem();
                    HtmlInlineContainer messageLine = new HtmlInlineContainer();
                    HtmlText message = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.department") + ":");
                    message.setFace(Face.EMPHASIS);
                    messageLine.addChild(message);
                    messageLine.addChild(new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.department.caption")));
                    item.addChild(messageLine);
                }
                if (isFutureGroupMessageShown()) {
                    HtmlListItem item = list.createItem();
                    HtmlInlineContainer messageLine = new HtmlInlineContainer();
                    HtmlText message = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.group") + ":");
                    message.setFace(Face.EMPHASIS);
                    messageLine.addChild(message);
                    messageLine.addChild(new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.group.caption")));
                    item.addChild(messageLine);
                }
                if (isFutureTransferMessageShown()) {
                    HtmlListItem item = list.createItem();
                    HtmlInlineContainer messageLine = new HtmlInlineContainer();
                    HtmlText message = new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.transfer") + ":");
                    message.setFace(Face.EMPHASIS);
                    messageLine.addChild(message);
                    messageLine.addChild(new HtmlText(BundleUtil.getString(Bundle.BOLONHA, "future.transfer.caption")));
                    item.addChild(messageLine);
                }
                container.addChild(list);
            }
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
