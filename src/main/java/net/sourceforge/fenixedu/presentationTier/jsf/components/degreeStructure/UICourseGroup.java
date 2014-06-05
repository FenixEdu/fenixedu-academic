/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class UICourseGroup extends UIDegreeModule {

    public static final String COMPONENT_TYPE =
            "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    public static final String COMPONENT_FAMILY =
            "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

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
            ExecutionYear executionYear, String module) throws IOException {
        super(courseGroup, previousContext, toEdit, showRules, depth, tabs, executionYear, module);

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
            if (this.courseGroup.hasAnyChildContexts()) {
                if (this.showRules && this.courseGroup.hasAnyCurricularRules()) {
                    encodeCurricularRules();
                }
                writer.endElement("table");

                if (!this.hideCourses
                        && this.courseGroup.getSortedOpenChildContextsWithCurricularCourses(executionYear).size() > 0) {
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
                writer.append(BundleUtil.getString(Bundle.BOLONHA, "BolonhaManagerResources", "empty.curricularPlan"));
                writer.endElement("i");
                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("table");
            }
        }
    }

    private void encodeChildCourseGroups() throws IOException {
        for (Context context : this.courseGroup.getSortedOpenChildContextsWithCourseGroups(this.executionYear)) {
            new UICourseGroup(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth + 1, this.tabs
                    + "\t", this.onlyStructure, this.toOrder, this.hideCourses, this.reportsAvailable, this.executionYear,
                    this.module).encodeBegin(facesContext);
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
            if (this.showRules && this.courseGroup.hasAnyCurricularRules()) {
                encodeCurricularRules();
            }

            writer.endElement("table");
            writer.endElement("div");

            if (!this.hideCourses && this.courseGroup.getSortedOpenChildContextsWithCurricularCourses(executionYear).size() > 0) {
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
            this.encodeLinkHref(module + "/curricularPlans/courseGroupReport.faces",
                    "&courseGroupID=" + this.courseGroup.getExternalId(), true);
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
        String editAndDeleteAditionalParameters =
                "&courseGroupID=" + this.courseGroup.getExternalId()
                        + ((!this.courseGroup.isRoot()) ? ("&contextID=" + this.previousContext.getExternalId()) : "")
                        + "&toOrder=false";

        if (loggedPersonCanManageDegreeCurricularPlans()) {
            writer.append("(");

            encodeLink(module + "/createCourseGroup.faces", createAssociateAditionalParameters, false, "create.course.group");

            if (!this.courseGroup.isRoot() && !this.courseGroup.isBranchCourseGroup()) {
                writer.append(" , ");
                encodeLink(module + "/createBranchCourseGroup.faces", createAssociateAditionalParameters, false,
                        "create.branch.group");
            }

            writer.append(" , ");
            encodeLink(module + "/associateCourseGroup.faces", createAssociateAditionalParameters, false,
                    "associate.course.group");

            if (!this.courseGroup.isRoot()) {
                writer.append(" , ");
                encodeLink(module + "/editCourseGroup.faces", editAndDeleteAditionalParameters, false, "edit");
            }

            if (!this.courseGroup.isRoot() /* && this.executionYear == null */) {
                writer.append(" , ");
                encodeLink(module + "/deleteCourseGroup.faces", editAndDeleteAditionalParameters, false, "delete");
            }

            if (this.courseGroup.isCycleCourseGroup()) {
                writer.append(" , ");
                encodeLink(module + "/editCycleCourseGroupInformation.faces", editAndDeleteAditionalParameters, false,
                        "editInformation");
            }
            writer.append(") ");
        }
        writer.endElement("td");
    }

    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);
        if (this.showRules) {
            if (!this.courseGroup.isRoot() || loggedPersonCanManageDegreeCurricularPlans()) {
                encodeLink(module + "/curricularRules/createCurricularRule.faces",
                        "&degreeModuleID=" + this.courseGroup.getExternalId(), false, "setCurricularRule");
            }
        } else {
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

        for (Context context : this.courseGroup.getSortedOpenChildContextsWithCurricularCourses(executionYear)) {
            new UICurricularCourse(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth, this.tabs
                    + "\t", this.executionYear, this.module).encodeBegin(facesContext);
        }

        writer.endElement("table");
        writer.endElement("div");
    }

}
