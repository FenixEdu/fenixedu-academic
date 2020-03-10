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
package org.fenixedu.academic.ui.faces.components.degreeStructure;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.base.Strings;

public class UICourseGroup extends UIDegreeModule {

    private static final String EXPAND_GROUP_ID_PARAM = "expandGroupId";

    public static final String COMPONENT_TYPE = "org.fenixedu.academic.ui.faces.components.degreeStructure.UICourseGroup";

    public static final String COMPONENT_FAMILY = "org.fenixedu.academic.ui.faces.components.degreeStructure.UICourseGroup";

    private final CourseGroup courseGroup;
    private Boolean onlyStructure;
    private Boolean toOrder;
    private Boolean hideCourses;
    private Boolean reportsAvailable;

    public UICourseGroup() {
        super();
        this.courseGroup = (CourseGroup) super.degreeModule;
    }

    public UICourseGroup(DegreeModule courseGroup, Context previousContext, Boolean toEdit, Boolean showRules, int depth,
            String tabs, Boolean onlyStructure, Boolean toOrder, Boolean hideCourses, Boolean reportsAvailable,
            ExecutionYear executionYear, String module, String currentPage, Boolean expandable) throws IOException {
        super(courseGroup, previousContext, toEdit, showRules, depth, tabs, executionYear, module, currentPage, expandable);

        if (toOrder && (!onlyStructure || !toEdit)) {
            throw new IOException("incorrect.component.usage");
        }
        this.courseGroup = (CourseGroup) super.degreeModule;
        this.onlyStructure = onlyStructure;
        this.toOrder = toOrder;
        this.hideCourses = hideCourses;
        this.reportsAvailable = reportsAvailable;
    }

    @Override
    public String getFamily() {
        return UICourseGroup.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }

        encodeCourseGroup(facesContext);
    }

    private void encodeCourseGroup(FacesContext facesContext) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        if (this.courseGroup.isRoot()) {
            encodeVisibleRoot();
        } else {
            encodeSelf();
            encodeChildCourseGroups();
        }
    }

    private void encodeVisibleRoot() throws IOException {
        if (this.onlyStructure) {
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mbottom0 mtop05", null);
            encodeHeader();
            encodeChildCourseGroups();
            writer.endElement("table");
        } else {
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mvert0", null);
            writer.writeAttribute("style", "width: 70em;", null);

            encodeHeader();
            if (!this.courseGroup.getChildContextsSet().isEmpty()) {
                if (this.showRules && !this.courseGroup.getCurricularRulesSet().isEmpty()) {
                    encodeCurricularRules();
                }
                writer.endElement("table");

                if (!this.hideCourses && getSortedOpenChildContextsWithCurricularCourses().size() > 0) {
                    encodeChildCurricularCourses(70, (this.depth + 1) * 3);
                    // encodeSumsFooter(sums);
                }

                encodeChildCourseGroups();
            } else {
                writer.startElement("table", this);
                writer.startElement("tr", this);
                writer.startElement("td", this);
                writer.writeAttribute("align", "center", null);
                writer.startElement("i", this);
                writer.append(BundleUtil.getString(Bundle.BOLONHA, "empty.curricularPlan"));
                writer.endElement("i");
                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("table");
            }
        }
    }

    private List<Context> getSortedOpenChildContextsWithCurricularCourses() {
        return this.courseGroup.getOpenChildContextsForExecutionAggregation(CurricularCourse.class, executionYear).stream()
                .sorted().collect(Collectors.toList());
    }

    private void encodeChildCourseGroups() throws IOException {
        for (Context context : this.courseGroup.getOpenChildContextsForExecutionAggregation(CourseGroup.class, this.executionYear)
                .stream().sorted().collect(Collectors.toList())) {
            new UICourseGroup(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth + 1,
                    this.tabs + "\t", this.onlyStructure, this.toOrder, this.hideCourses, this.reportsAvailable,
                    this.executionYear, this.module, this.currentPage, this.expandable).encodeBegin(facesContext);
        }
    }

    private void encodeSelf() throws IOException {
        int width = (this.onlyStructure) ? 50 : 70;
        int courseGroupIndent = this.depth * 3;

        if (!this.onlyStructure) {
            writer.startElement("div", this);
            writer.writeAttribute("style", "padding-left: " + courseGroupIndent + "em;", null);
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mvert0", null);
            writer.writeAttribute("style", "width: " + String.valueOf(width - (this.depth * 3)) + "em;", null);
        }

        encodeHeader();

        if (!this.onlyStructure) {
            if (this.showRules && !this.courseGroup.getCurricularRulesSet().isEmpty()) {
                encodeCurricularRules();
            }

            writer.endElement("table");
            writer.endElement("div");

            if (!this.hideCourses && getSortedOpenChildContextsWithCurricularCourses().size() > 0) {
                encodeChildCurricularCourses(width, courseGroupIndent);
                // encodeSumsFooter(sums);
            } else {
                // encodeEmptyCourseGroupInfo();
            }
        }
    }

    private void encodeHeader() throws IOException {
        writer.startElement("tr", this);

        encodeName(reportsAvailable);

        if (this.toEdit) {
            if (this.onlyStructure) {
                if (this.toOrder) {
                    if (!this.courseGroup.isRoot()) {
                        encodeOrderOptions();
                    }
                } else {
                    encodeEditOptions();
                }
            } else {
                encodeCourseGroupOptions();
            }

        }

        writer.endElement("tr");
    }

    private void encodeName(boolean linkable) throws IOException {
        if (this.onlyStructure) {
            writer.startElement("td", this);
            if (this.courseGroup.isRoot()) {
                writer.startElement("strong", this);
            } else {
                writer.writeAttribute("style", "padding-left: " + String.valueOf((this.depth + 2)) + "em;", null);
            }
        } else {
            writer.writeAttribute("class", "bgcolor2", null);
            writer.startElement("th", this);
            writer.writeAttribute("class", "aleft", null);
            writer.writeAttribute("colspan", (this.toEdit) ? 3 : 5, null);
        }

        if (linkable) {
            writer.startElement("a", this);
            this.encodeLinkHref(module + "/courseGroupReport.faces", "&courseGroupID=" + this.courseGroup.getExternalId(), true);
            appendCodeAndName();
            writer.endElement("a");
        } else {
            appendCodeAndName();
        }

        if (this.onlyStructure) {
            if (this.courseGroup.isRoot()) {
                writer.endElement("strong");
            }
            writer.endElement("td");
        } else {
            writer.endElement("th");
        }
    }

    private void encodeOrderOptions() throws IOException {
        writer.startElement("td", this);
        if (loggedPersonCanManageDegreeCurricularPlans()) {
            writer.append("(");

            encodeOrderOption(0, "top", false);
            encodeOrderOption(this.previousContext.getParentCourseGroup().getChildContextsSet().size() - 1, "end", false);
            encodeOrderOption(this.previousContext.getChildOrder() - 1, "up", false);
            encodeOrderOption(this.previousContext.getChildOrder() + 1, "down", true);

            writer.append(") ");
        }
    }

    private void encodeOrderOption(Integer posToTest, String label, boolean lastOption) throws IOException {
        encodeLink(module + "/orderCourseGroup.faces", "&courseGroupID=" + this.courseGroup.getExternalId() + "&contextID="
                + this.previousContext.getExternalId() + "&pos=" + posToTest + "&toOrder=true", false, label);
        if (!lastOption) {
            writer.append(" , ");
        }
    }

    private void encodeEditOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "aleft", null);

        String createAssociateAditionalParameters = "&parentCourseGroupID=" + this.courseGroup.getExternalId() + "&toOrder=false";
        String editAndDeleteAditionalParameters = "&courseGroupID=" + this.courseGroup.getExternalId()
                + ((!this.courseGroup.isRoot()) ? ("&contextID=" + this.previousContext.getExternalId()) : "") + "&toOrder=false";

        if (loggedPersonCanManageDegreeCurricularPlans()) {
            writer.append("(");

            encodeLink(module + "/createCourseGroup.faces", createAssociateAditionalParameters, false, "create.course.group");

            if (!this.courseGroup.isRoot() && !this.courseGroup.isBranchCourseGroup()) {
                writer.append(" , ");
                encodeLink(module + "/createBranchCourseGroup.faces", createAssociateAditionalParameters, false,
                        "create.branch.group");
            }

            //Course group sharing is a legacy behavior (unsupported) and should not be used anymore
//            writer.append(" , ");
//            encodeLink(module + "/associateCourseGroup.faces", createAssociateAditionalParameters, false,
//                    "associate.course.group");

//            if (!this.courseGroup.isRoot()) {
            writer.append(" , ");
            encodeLink(module + "/editCourseGroup.faces", editAndDeleteAditionalParameters, false, "edit");
//            }

            if (!this.courseGroup.isRoot() /* && this.executionYear == null */) {
                writer.append(" , ");
                encodeLink(module + "/deleteCourseGroup.faces", editAndDeleteAditionalParameters, false, "delete");
            }

            writer.append(") ");
        }
        writer.endElement("td");
    }

    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);

        boolean expandOptionAvailable = false;
        if (expandable && !isToRenderExpanded() && !getSortedOpenChildContextsWithCurricularCourses().isEmpty()) {
            encodeLink(module + "/" + currentPage, "&" + EXPAND_GROUP_ID_PARAM + "=" + this.courseGroup.getExternalId(), false,
                    "label.expand");
            expandOptionAvailable = true;
        }

        if (this.showRules) {
            if (!this.courseGroup.isRoot() || loggedPersonCanManageDegreeCurricularPlans()) {
                if (expandOptionAvailable) {
                    writer.append(" , ");
                }

                encodeLink(module + "/curricularRules/createCurricularRule.faces",
                        "&degreeModuleID=" + this.courseGroup.getExternalId(), false, "setCurricularRule");
            }
        } else {
            if (expandOptionAvailable) {
                writer.append(" , ");
            }

            encodeLink(module + "/createCurricularCourse.faces", "&courseGroupID=" + this.courseGroup.getExternalId(), false,
                    "create.curricular.course");
            writer.append(" , ");
            encodeLink(module + "/associateCurricularCourse.faces", "&courseGroupID=" + this.courseGroup.getExternalId(), false,
                    "associate.curricular.course");
        }
        writer.endElement("th");
    }

    private void encodeChildCurricularCourses(int width, int courseGroupIndent) throws IOException {
        writer.startElement("div", this);
        writer.writeAttribute("class", (this.courseGroup.isRoot()) ? "indent3" : "indent" + (courseGroupIndent + 3), null);
        writer.startElement("table", this);
        writer.writeAttribute("class", "showinfo3 mvert0", null);
        writer.writeAttribute("style", "width: " + (width - (this.depth * 3) - 3) + "em;", null);

        if (!expandable || isToRenderExpanded()) {
            for (Context context : getSortedOpenChildContextsWithCurricularCourses()) {
                new UICurricularCourse(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth,
                        this.tabs + "\t", this.executionYear, this.module, null, false).encodeBegin(facesContext);
            }
        }

        writer.endElement("table");
        writer.endElement("div");
    }

    private boolean isToRenderExpanded() {
        final String expandGroupId = getExpandGroupId();
        return expandGroupId != null && expandGroupId.equals(this.courseGroup.getExternalId());
    }

    protected String getExpandGroupId() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getParameter(EXPAND_GROUP_ID_PARAM);
    }

    @Override
    protected void appendCodeAndName() throws IOException {
        super.appendCodeAndName();

        if (degreeModule.isCourseGroup()) {
            final ProgramConclusion programConclusion = ((CourseGroup) degreeModule).getProgramConclusion();
            if (programConclusion != null) {
                final String name = programConclusion.getName().getContent();
                final String description = programConclusion.getDescription().getContent();

                writer.startElement("strong", this);
                writer.append(" [");
                writer.append(name);
                if (!Strings.isNullOrEmpty(description)) {
                    writer.append(" - ");
                    writer.append(description);
                }
                writer.append("]");
                writer.endElement("strong");
            }
        }

        if (this.toEdit) {

            if (!degreeModule.isLeaf() && ((CourseGroup) degreeModule).getIsOptional()) {
                writer.startElement("strong", this);
                writer.append(" (");
                writer.append(BundleUtil.getString(Bundle.BOLONHA, "optional"));
                writer.append(")");
                writer.endElement("strong");
            }

            if (degreeModule.isBranchCourseGroup()) {
                writer.startElement("strong", this);
                writer.append(" (");
                writer.append(BundleUtil.getString(Bundle.BOLONHA, "branchCourseGroup"));
                writer.append(")");
                writer.endElement("strong");
            }
        }
    }

}
