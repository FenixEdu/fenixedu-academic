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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class UIDegreeCurricularPlan extends UIInput {
    public static final String COMPONENT_TYPE =
            "org.fenixedu.academic.ui.faces.components.degreeStructure.UIDegreeCurricularPlan";

    public static final String COMPONENT_FAMILY =
            "org.fenixedu.academic.ui.faces.components.degreeStructure.UIDegreeCurricularPlan";

    protected static final int ROOT_DEPTH = 0;

    private static final String MODULE = "module";

    private static final String DCP = "dcp";

    private static final String TO_ORDER = "toOrder";

    private static final String EXECUTION_YEAR = "executionYear";

    private static final String REPORTS_AVAILABLE = "reportsAvailable";

    private static final String HIDE_COURSES = "hideCourses";

    private static final String ONLY_STRUCTURE = "onlyStructure";

    private static final String ORGANIZE_BY = "organizeBy";

    private static final String SHOW_RULES = "showRules";

    private static final String TO_EDIT = "toEdit";

    private static final String YEARS = "years";

    private static final String CURRENT_PAGE = "currentPage";

    private static final String GROUP_EXPAND_ENABLED = "groupExpandEnabled";

    private static final int MAX_CONTEXTS_TO_RENDER = 1000;

    private boolean toEdit;
    private boolean showRules;
    private ExecutionYear executionYear;
    private String module;

    private FacesContext facesContext;
    private ResponseWriter writer;

    public UIDegreeCurricularPlan() {
        super();
        this.setRendererType(null);
    }

    @Override
    public String getFamily() {
        return UIDegreeCurricularPlan.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }

        module = facesContext.getExternalContext().getRequestContextPath() + (String) this.getAttributes().get(MODULE);

        final DegreeCurricularPlan dcp = getDegreeCurricularPlanAttribute();
        this.toEdit = (this.getBooleanAttribute(TO_EDIT) != null) ? (Boolean) this.getBooleanAttribute(TO_EDIT) : Boolean.FALSE;
        this.showRules =
                (this.getBooleanAttribute(SHOW_RULES) != null) ? (Boolean) this.getBooleanAttribute(SHOW_RULES) : Boolean.FALSE;
        final String organizeBy =
                (this.getAttributes().get(ORGANIZE_BY) != null) ? (String) this.getAttributes().get(ORGANIZE_BY) : "groups";
        final Boolean onlyStructure = getOnlyStructureAttribute();
        final Boolean toOrder =
                (this.getBooleanAttribute(TO_ORDER) != null) ? (Boolean) this.getBooleanAttribute(TO_ORDER) : Boolean.FALSE;
        final Boolean hideCourses = getHideCourseAttribute();
        final Boolean reportsAvailable = (this.getBooleanAttribute(REPORTS_AVAILABLE) != null) ? (Boolean) this
                .getBooleanAttribute(REPORTS_AVAILABLE) : Boolean.FALSE;
        if (this.getAttributes().get(EXECUTION_YEAR) != null) {
            executionYear = (ExecutionYear) this.getAttributes().get(EXECUTION_YEAR);
        }

        if (incorrectUseOfComponent(organizeBy, onlyStructure, toOrder, hideCourses)) {
            throw new IOException("incorrect.component.usage");
        }

        if (organizeBy.equalsIgnoreCase(YEARS)) {
            encodeByYears(facesContext, dcp);
        } else {
            new UICourseGroup(dcp.getRoot(), null, this.toEdit, this.showRules, ROOT_DEPTH, "", onlyStructure, toOrder,
                    hideCourses, reportsAvailable, executionYear, module, getCurrentPageAttribute(),
                    isToEnableGroupExpandOption()).encodeBegin(facesContext);
        }

        if (dcp.getDegreeStructure() != null && !dcp.getDegreeStructure().getChildsSet().isEmpty() && !onlyStructure) {
            encodeSubtitles(facesContext);
        }
    }

    protected Boolean getGroupExpandEnabledAttribute() {
        return (this.getBooleanAttribute(GROUP_EXPAND_ENABLED) != null) ? (Boolean) this
                .getBooleanAttribute(GROUP_EXPAND_ENABLED) : Boolean.FALSE;
    }

    protected String getCurrentPageAttribute() {
        return (String) getAttributes().get(CURRENT_PAGE);
    }

    protected Boolean getHideCourseAttribute() {
        final Boolean hideCourses = (this.getBooleanAttribute(HIDE_COURSES) != null) ? (Boolean) this
                .getBooleanAttribute(HIDE_COURSES) : Boolean.FALSE;
        return hideCourses;
    }

    protected Boolean getOnlyStructureAttribute() {
        final Boolean onlyStructure = (this.getBooleanAttribute(ONLY_STRUCTURE) != null) ? (Boolean) this
                .getBooleanAttribute(ONLY_STRUCTURE) : Boolean.FALSE;
        return onlyStructure;
    }

    private boolean incorrectUseOfComponent(String organizeBy, Boolean onlyStructure, Boolean toOrder, Boolean hideCourses) {
        return ((onlyStructure && (showRules || organizeBy.equals(YEARS))) || (toOrder && (!onlyStructure || !toEdit)));
    }

    private Boolean getBooleanAttribute(String attributeName) {
        if (this.getAttributes().get(attributeName) instanceof Boolean) {
            return (Boolean) this.getAttributes().get(attributeName);
        } else {
            return Boolean.valueOf((String) this.getAttributes().get(attributeName));
        }
    }

    private void encodeByYears(FacesContext facesContext, DegreeCurricularPlan dcp) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        if (isLargeDegreeCurricularPlan()) {
            encodeLargeDegreeCurricularPlanByYearsInfo();
            return;
        }

        if (dcp.getRoot().getChildContextsSet().isEmpty()) {
            encodeEmptyCurricularPlanInfo();
        } else {
            CurricularPeriod degreeStructure = dcp.getDegreeStructure();
            if (degreeStructure == null || degreeStructure.getChildsSet().isEmpty()) {
                encodeEmptyDegreeStructureInfo();
            } else {
                for (CurricularPeriod child : degreeStructure.getSortedChilds()) {
                    encodePeriodTable(child);
                }
            }
        }
    }

    private void encodeEmptyCurricularPlanInfo() throws IOException {
        encodeInfoTable("empty.curricularPlan");
    }

    private void encodeEmptyDegreeStructureInfo() throws IOException {
        encodeInfoTable("empty.degreeStructure");
    }

    private void encodeLargeDegreeCurricularPlanByYearsInfo() throws IOException {
        encodeInfoTable("large.degree.curricular.plans.cannot.be.viewed.by.years");
    }

    private void encodeInfoTable(String info) throws IOException {
        writer.startElement("table", this);
        writer.startElement("tr", this);
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(BundleUtil.getString(Bundle.BOLONHA, info));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void encodePeriodTable(CurricularPeriod curricularPeriod) throws IOException {
        if (!curricularPeriod.getChildsSet().isEmpty()) {
            for (CurricularPeriod child : curricularPeriod.getSortedChilds()) {
                encodePeriodTable(child);
            }
        } else {
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3", null);
            writer.writeAttribute("style", "width: 70em;", null);

            encodeHeader(curricularPeriod);
            if (!encodeCurricularCourses(curricularPeriod)) {
                encodeEmptySemesterInfo();
            }

            writer.endElement("table");
        }
    }

    private void encodeHeader(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("tr", this);
        writer.writeAttribute("class", "bgcolor2", null);

        writer.startElement("th", this);
        writer.writeAttribute("class", "aleft", null);
        writer.writeAttribute("colspan", (this.toEdit) ? 3 : 5, null);
        writer.append(curricularPeriod.getFullLabel());
        writer.endElement("th");

        if (this.toEdit) {
            encodeCourseGroupOptions(curricularPeriod);
        }

        writer.endElement("tr");
    }

    private void encodeCourseGroupOptions(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);
        if (!this.showRules && loggedPersonCanManageDegreeCurricularPlans(getDegreeCurricularPlanAttribute())) {
            encodeLink(module + "/createCurricularCourse.faces", "&curricularYearID="
                    + curricularPeriod.getParent().getChildOrder() + "&curricularSemesterID=" + curricularPeriod.getChildOrder(),
                    false, "create.curricular.course");
            writer.append(" , ");
            encodeLink(module + "/associateCurricularCourse.faces", "&curricularYearID="
                    + curricularPeriod.getParent().getChildOrder() + "&curricularSemesterID=" + curricularPeriod.getChildOrder(),
                    false, "associate.curricular.course");
        }
        writer.endElement("th");
    }

    private final Map<CurricularPeriod, List<Context>> toRepeat = new HashMap<CurricularPeriod, List<Context>>();

    private boolean encodeCurricularCourses(CurricularPeriod curricularPeriod) throws IOException {
        boolean anyCurricularCourseEncoded = false;

        for (Context context : curricularPeriod.getContextsSet()) {
            if (context.getChildDegreeModule().isLeaf()
                    && (executionYear == null || context.isValidForExecutionAggregation(executionYear))) {
                anyCurricularCourseEncoded = true;

                CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

                curricularCourse.getContactLoad(curricularPeriod, executionYear);
                curricularCourse.getAutonomousWorkHours(curricularPeriod, executionYear);
                curricularCourse.getTotalLoad(curricularPeriod, executionYear);
                curricularCourse.getEctsCredits(curricularPeriod, executionYear);
                new UICurricularCourse(curricularCourse, context, this.toEdit, this.showRules, this.executionYear, this.module,
                        null, false).encodeBegin(facesContext);

                if (curricularCourse.isAnual()) {
                    remindToEncodeInNextPeriod(curricularPeriod, context);
                }
            }
        }

        if (toRepeat.containsKey(curricularPeriod)) {
            anyCurricularCourseEncoded = true;

            for (Context check : toRepeat.get(curricularPeriod)) {
                new UICurricularCourse(check.getChildDegreeModule(), check, this.toEdit, this.showRules, this.executionYear,
                        this.module, null, false).encodeInNextPeriod(facesContext);
            }
        }

        return anyCurricularCourseEncoded;
    }

    private void remindToEncodeInNextPeriod(CurricularPeriod curricularPeriod, Context context) {
        if (curricularPeriod.getNext() != null) {
            List<Context> toUpdate = toRepeat.get(curricularPeriod.getNext());
            if (toUpdate == null) {
                toUpdate = new ArrayList<Context>();
            }
            toUpdate.add(context);
            toRepeat.put(curricularPeriod.getNext(), toUpdate);
        }
    }

    private void encodeEmptySemesterInfo() throws IOException {
        writer.startElement("tr", this);
        writer.startElement("td", this);
        if (this.toEdit) {
            writer.writeAttribute("colspan", 5, null);
        } else {
            writer.writeAttribute("colspan", 3, null);
        }
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(BundleUtil.getString(Bundle.BOLONHA, "no.associated.curricular.courses.to.year"));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
    }

    private void encodeLink(String page, String aditionalParameters, boolean blank, String... bundleKeys) throws IOException {
        writer.startElement("a", this);
        encodeLinkHref(page, aditionalParameters, blank);
        for (String bundleKey : bundleKeys) {
            writer.write(BundleUtil.getString(Bundle.BOLONHA, bundleKey));
        }
        writer.endElement("a");
    }

    private void encodeLinkHref(String page, String aditionalParameters, boolean blank) throws IOException {
        Map requestParameterMap = this.facesContext.getExternalContext().getRequestParameterMap();
        StringBuilder href = new StringBuilder();
        href.append(page).append("?");
        Object dcpId = null;
        if (requestParameterMap.get("degreeCurricularPlanID") != null) {
            dcpId = requestParameterMap.get("degreeCurricularPlanID");
        } else if (requestParameterMap.get("dcpId") != null) {
            dcpId = requestParameterMap.get("dcpId");
        }
        href.append("degreeCurricularPlanID=").append(dcpId);
        if (this.executionYear != null) {
            href.append("&executionYearID=").append(this.executionYear.getExternalId());
        }
        if (aditionalParameters != null) {
            href.append(aditionalParameters);
        }
        href.append("&organizeBy=").append(requestParameterMap.get(ORGANIZE_BY));
        href.append("&showRules=").append(requestParameterMap.get(SHOW_RULES));
        href.append("&hideCourses=").append(requestParameterMap.get(HIDE_COURSES));
        href.append("&action=").append(requestParameterMap.get("action"));
        writer.writeAttribute("href", href.toString(), null);
        if (blank) {
            writer.writeAttribute("target", "_blank", null);
        }
    }

    private void encodeSubtitles(FacesContext facesContext) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        writer.startElement("p", this);
        writer.writeAttribute("class", "mtop2 mbottom05", null);
        writer.startElement("em", this);
        writer.append(BundleUtil.getString(Bundle.BOLONHA, "subtitle")).append(":\n");
        writer.endElement("em");
        writer.endElement("p");

        writer.startElement("ul", this);
        writer.writeAttribute("class", "nobullet mtop05 mbottom2", null);
        writer.writeAttribute("style", "padding-left: 0pt; font-style: italic;", null);

        encodeSubtitleElement(Bundle.ENUMERATION, RegimeType.SEMESTRIAL.toString() + ".ACRONYM", RegimeType.SEMESTRIAL.toString(),
                null);
        encodeSubtitleElement(Bundle.ENUMERATION, RegimeType.ANUAL.toString() + ".ACRONYM", RegimeType.ANUAL.toString(), null);

        encodeSubtitleElement(Bundle.BOLONHA, "contactLessonHoursAcronym", "contactLessonHours", null);
        encodeSubtitleElement(Bundle.BOLONHA, "autonomousWorkAcronym", "autonomousWork", null);

        StringBuilder explanation = new StringBuilder();
        explanation.append(" (");
        explanation.append(BundleUtil.getString(Bundle.BOLONHA, "contactLessonHoursAcronym"));
        explanation.append(" + ");
        explanation.append(BundleUtil.getString(Bundle.BOLONHA, "autonomousWorkAcronym"));
        explanation.append(")");
        encodeSubtitleElement(Bundle.BOLONHA, "totalLoadAcronym", "totalLoad", explanation);
        writer.endElement("ul");
    }

    private void encodeSubtitleElement(String bundle, String acronym, String full, StringBuilder explanation) throws IOException {
        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(BundleUtil.getString(bundle, acronym)).append(" - ");
        writer.endElement("span");
        writer.append(BundleUtil.getString(bundle, full));
        if (explanation != null) {
            writer.append(explanation);
        }
        writer.endElement("li");
    }

    private Boolean loggedPersonCanManageDegreeCurricularPlans(DegreeCurricularPlan degreeCurricularPlan) {
        return AcademicPredicates.MANAGE_DEGREE_CURRICULAR_PLANS.evaluate(degreeCurricularPlan.getDegree());
    }

    private DegreeCurricularPlan getDegreeCurricularPlanAttribute() {
        return (DegreeCurricularPlan) this.getAttributes().get(DCP);
    }

    private boolean isToEnableGroupExpandOption() {
        return getGroupExpandEnabledAttribute() && !getOnlyStructureAttribute() && !getHideCourseAttribute()
                && isLargeDegreeCurricularPlan();
    }

    protected boolean isLargeDegreeCurricularPlan() {
        int totalContexts = 0;
        for (final CurricularCourse curricularCourse : getDegreeCurricularPlanAttribute().getCurricularCoursesSet()) {
            totalContexts += curricularCourse.getParentContextsByExecutionYear(executionYear).size();
        }

        return totalContexts > MAX_CONTEXTS_TO_RENDER;
    }

}
