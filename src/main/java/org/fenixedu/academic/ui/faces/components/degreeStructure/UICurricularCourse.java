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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CurricularPeriodLabelFormatter;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class UICurricularCourse extends UIDegreeModule {

    public static final String COMPONENT_TYPE = "org.fenixedu.academic.ui.faces.components.degreeStructure.UICurricularCourse";

    public static final String COMPONENT_FAMILY = "org.fenixedu.academic.ui.faces.components.degreeStructure.UICurricularCourse";

    private final CurricularCourse curricularCourse;
    private final boolean byYears;

    public UICurricularCourse() {
        super();
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules,
            int depth, String tabs, ExecutionYear executionYear, String module, String currentPage, Boolean expandable) {
        super(curricularCourse, previousContext, toEdit, showRules, depth, tabs, executionYear, module, currentPage, expandable);
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules,
            ExecutionYear executionYear, String module, String currentPage, Boolean expandable) {
        super(curricularCourse, previousContext, toEdit, showRules, 0, null, executionYear, module, currentPage, expandable);
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = true;
    }

    @Override
    public String getFamily() {
        return UICurricularCourse.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        encodeCurricularCourse();

        if (this.showRules && !this.curricularCourse.getCurricularRulesSet().isEmpty()) {
            encodeCurricularRules();
        }

        if (!byYears && this.curricularCourse.isBolonhaDegree() && this.curricularCourse.isAnual(this.executionYear)) {
            encodeInNextPeriod(facesContext);
        }
    }

    private void encodeCurricularCourse() throws IOException {
        writer.startElement("tr", this);

        encodeName(true);
        encodeContext(previousContext.getCurricularPeriod());
        encodeRegime();
        encodeLoadsAndCredits(previousContext.getCurricularPeriod());

        if (this.toEdit) {
            if (this.showRules) {
                encodeCurricularRulesOptions();
            } else {
                encodeCurricularCourseOptions();
            }
        }

        writer.endElement("tr");
    }

    private void encodeName(boolean linkable) throws IOException {
        writer.startElement("td", this);

        if (linkable) {
            writer.startElement("a", this);
            if (this.curricularCourse.isBolonhaDegree()) {
                encodeLinkHref(module + "/viewCurricularCourse.faces",
                        "&curricularCourseID=" + this.curricularCourse.getExternalId(), false);
            } else {
                encodeNonBolonhaLinkHref();
            }
            appendCodeAndName();
            writer.endElement("a");
        } else {
            appendCodeAndName();
        }

        writer.endElement("td");
    }

    private void encodeNonBolonhaLinkHref() throws IOException {

        final StringBuilder href = new StringBuilder();
        href.append(module + "/showCourseSite.do?method=showCurricularCourseSite");

        href.append("&curricularCourseID=").append(this.curricularCourse.getExternalId());
        href.append("&degreeID=").append(this.curricularCourse.getDegree().getExternalId());
        href.append("&degreeCurricularPlanID=").append(this.curricularCourse.getDegreeCurricularPlan().getExternalId());

        final Map<String, String> requestParameterMap = this.facesContext.getExternalContext().getRequestParameterMap();
        if (this.executionYear != null) {
            final ExecutionInterval executionInterval = this.executionYear.getLastExecutionPeriod();
            href.append("&executionPeriodOID=").append(executionInterval.getExternalId());
        } else if (requestParameterMap.get("executionPeriodOID") != null) {
            href.append("&executionPeriodOID=").append(requestParameterMap.get("executionPeriodOID"));
        }

        writer.writeAttribute("href", href.toString(), null);
    }

    private void encodeContext(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "smalltxt", null);
        if (!byYears) {
            writer.writeAttribute("align", "center", null);
            writer.append(CurricularPeriodLabelFormatter.getFullLabel(curricularPeriod, true));
        } else {
            writer.append(previousContext.getParentCourseGroup().getName());
        }
        writer.endElement("td");
    }

    private void encodeRegime() throws IOException {
        writer.startElement("td", this);
        if (!this.curricularCourse.isOptionalCurricularCourse() && this.curricularCourse.getRegime(this.executionYear) != null) {
            writer.writeAttribute("class", "highlight2 smalltxt", null);
            writer.writeAttribute("align", "center", null);
            writer.writeAttribute("style", "width: 1em;", null);
            writer.append(BundleUtil.getString(Bundle.ENUMERATION,
                    this.curricularCourse.getRegime(this.executionYear).toString() + ".ACRONYM"));
        } else {
            writer.append("&nbsp;");
        }
        writer.endElement("td");
    }

    private void encodeLoadsAndCredits(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("td", this);
        if (!this.curricularCourse.isOptionalCurricularCourse()) {
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "contactLessonHoursAcronym")).append("-");
            writer.endElement("span");
            final BigDecimal contactLoad = new BigDecimal(this.curricularCourse.getContactLoad(curricularPeriod, executionYear))
                    .setScale(2, RoundingMode.HALF_EVEN);
            writer.append(contactLoad.toPlainString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getAutonomousWorkHours(curricularPeriod, executionYear).toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "totalLoadAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getTotalLoad(curricularPeriod, executionYear).toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "credits.abbreviation")).append(" ");
            writer.append(this.curricularCourse.getEctsCredits(curricularPeriod, executionYear).toString());
            if (this.curricularCourse.getBaseWeight() != null) {
                writer.append(" (");
                writer.append(BundleUtil.getString(Bundle.BOLONHA, "weight")).append(" ");
                writer.append(this.curricularCourse.getBaseWeight().toString());
                writer.append(")");
            }
        } else {
            writer.append("&nbsp;");
            writer.endElement("td");
            writer.startElement("td", this);
        }
        writer.endElement("td");
    }

    public void encodeInNextPeriod(FacesContext facesContext) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        writer.startElement("tr", this);

        encodeName(false);

        writer.startElement("td", this);
        writer.writeAttribute("class", "smalltxt", null);
        final int order = previousContext.getCurricularPeriod().getChildOrder() + 1;
        if (!byYears) {
            writer.writeAttribute("align", "center", null);
            if (previousContext.getCurricularPeriod().getParent().getChildOrder() != null) {
                writer.append(String.valueOf(previousContext.getCurricularPeriod().getParent().getChildOrder()));
                writer.append(" ");
                writer.append(BundleUtil.getString(Bundle.ENUMERATION,
                        previousContext.getCurricularPeriod().getParent().getAcademicPeriod().getName() + ".ABBREVIATION"));
                writer.append(", ");
            }

            writer.append(String.valueOf(order));
            writer.append(" ");
            writer.append(BundleUtil.getString(Bundle.ENUMERATION,
                    previousContext.getCurricularPeriod().getAcademicPeriod().getName() + ".ABBREVIATION"));
        } else {
            writer.append(previousContext.getParentCourseGroup().getName());
        }
        writer.endElement("td");

        encodeRegime();

        writer.startElement("td", this);
        if (!this.curricularCourse.isOptionalCurricularCourse()) {
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "contactLessonHoursAcronym")).append("-");
            writer.endElement("span");

            final CompetenceCourseInformation information =
                    this.curricularCourse.getCompetenceCourse().findInformationMostRecentUntil(executionYear);

            final BigDecimal contactLoad = new BigDecimal(information != null ? information.getContactLoad(order) : 0.0)
                    .setScale(2, RoundingMode.HALF_EVEN);
            writer.append(contactLoad.toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            final Double autonomousWorkHours = information != null ? information.getAutonomousWorkHours(order) : 0.0;
            writer.append(autonomousWorkHours.toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "totalLoadAcronym")).append("-");
            writer.endElement("span");
            final Double totalLoad = information != null ? information.getTotalLoad(order) : 0.0;
            writer.append(totalLoad.toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);
            writer.append(BundleUtil.getString(Bundle.BOLONHA, "credits.abbreviation")).append(" ");
            final Double ectsCredits = information != null ? information.getEctsCredits(order) : 0.0;
            writer.append(String.valueOf(ectsCredits));
        } else {
            writer.append("&nbsp;");
            writer.endElement("td");
            writer.startElement("td", this);
        }
        writer.endElement("td");

        writer.startElement("td", this);
        writer.append("&nbsp;");
        writer.endElement("td");

        writer.endElement("tr");
    }

    private void encodeCurricularCourseOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 9em;", null);
        if (loggedPersonCanManageDegreeCurricularPlans()) {
            encodeLink(module + "/editCurricularCourse.faces", "&contextID=" + this.previousContext.getExternalId()
                    + "&curricularCourseID=" + this.curricularCourse.getExternalId(), false, "edit");
            // if (this.executionYear == null) {
            writer.append(" , ");
            encodeLink(module + "/deleteCurricularCourseContext.faces", "&contextID=" + this.previousContext.getExternalId()
                    + "&curricularCourseID=" + this.curricularCourse.getExternalId(), false, "delete");
            // }
        }
        writer.endElement("td");
    }

    private void encodeCurricularRulesOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 9em;", null);
        if (loggedPersonCanManageDegreeCurricularPlans()) {
            encodeLink(module + "/curricularRules/createCurricularRule.faces",
                    "&degreeModuleID=" + this.curricularCourse.getExternalId(), false, "setCurricularRule");
        }
        writer.endElement("td");
    }

}
