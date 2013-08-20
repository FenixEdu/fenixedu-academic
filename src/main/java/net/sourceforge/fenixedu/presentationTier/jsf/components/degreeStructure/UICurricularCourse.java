package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;

public class UICurricularCourse extends UIDegreeModule {
    public static final String COMPONENT_TYPE =
            "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    public static final String COMPONENT_FAMILY =
            "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    private CurricularCourse curricularCourse;
    private boolean byYears;

    public UICurricularCourse() {
        super();
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules,
            int depth, String tabs, ExecutionYear executionYear, String module) {
        super(curricularCourse, previousContext, toEdit, showRules, depth, tabs, executionYear, module);
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules,
            ExecutionYear executionYear, String module) {
        super(curricularCourse, previousContext, toEdit, showRules, 0, null, executionYear, module);
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

        log(false);

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        encodeCurricularCourse();

        if (this.showRules && this.curricularCourse.hasAnyCurricularRules()) {
            encodeCurricularRules();
        }

        if (!byYears && this.curricularCourse.isBolonhaDegree() && this.curricularCourse.isAnual(this.executionYear)) {
            encodeInNextPeriod(facesContext);
        }
    }

    private void log(boolean on) {
        if (LogLevel.INFO) {
            if (on) {
                StringBuilder buffer = new StringBuilder();
                buffer.append(tabs);
                buffer.append("[LEVEL ").append(new Integer(this.depth)).append("]");
                buffer.append("[CC ").append(this.curricularCourse.getExternalId()).append("][");
                buffer.append(previousContext.getCurricularPeriod().getOrderByType(AcademicPeriod.YEAR)).append("Y,");
                buffer.append(previousContext.getCurricularPeriod().getOrderByType(AcademicPeriod.SEMESTER)).append("S] ");
                buffer.append(this.curricularCourse.getName(lastExecutionPeriod));
                System.out.println(buffer.toString());
            }
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
                encodeLinkHref("viewCurricularCourse.faces", "&curricularCourseID=" + this.curricularCourse.getExternalId(),
                        false);
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
        href.append("showCourseSite.do?method=showCurricularCourseSite");

        href.append("&curricularCourseID=").append(this.curricularCourse.getExternalId());
        href.append("&degreeID=").append(this.curricularCourse.getDegree().getExternalId());
        href.append("&degreeCurricularPlanID=").append(this.curricularCourse.getDegreeCurricularPlan().getExternalId());

        final Map<String, String> requestParameterMap = this.facesContext.getExternalContext().getRequestParameterMap();
        if (this.executionYear != null) {
            final ExecutionSemester executionSemester = this.executionYear.getLastExecutionPeriod();
            href.append("&executionPeriodOID=").append(executionSemester.getExternalId());
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
        if (!this.curricularCourse.isOptional() && this.curricularCourse.getRegime(this.executionYear) != null) {
            writer.writeAttribute("class", "highlight2 smalltxt", null);
            writer.writeAttribute("align", "center", null);
            writer.writeAttribute("style", "width: 1em;", null);
            writer.append(this.getBundleValue("EnumerationResources", this.curricularCourse.getRegime(this.executionYear)
                    .toString() + ".ACRONYM"));
        } else {
            writer.append("&nbsp;");
        }
        writer.endElement("td");
    }

    private void encodeLoadsAndCredits(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("td", this);
        if (!this.curricularCourse.isOptional()) {
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "contactLessonHoursAcronym")).append("-");
            writer.endElement("span");
            final BigDecimal contactLoad =
                    new BigDecimal(this.curricularCourse.getContactLoad(curricularPeriod, executionYear)).setScale(2,
                            RoundingMode.HALF_EVEN);
            writer.append(contactLoad.toPlainString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getAutonomousWorkHours(curricularPeriod, executionYear).toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "totalLoadAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getTotalLoad(curricularPeriod, executionYear).toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "credits.abbreviation")).append(" ");
            writer.append(this.curricularCourse.getEctsCredits(curricularPeriod, executionYear).toString());
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
        if (!byYears) {
            writer.writeAttribute("align", "center", null);
            if (previousContext.getCurricularPeriod().getParent().getChildOrder() != null) {
                writer.append(String.valueOf(previousContext.getCurricularPeriod().getParent().getChildOrder()));
                writer.append(" ");
                writer.append(getBundleValue("EnumerationResources", previousContext.getCurricularPeriod().getParent()
                        .getAcademicPeriod().getName()
                        + ".ABBREVIATION"));
                writer.append(", ");
            }

            writer.append(String.valueOf(previousContext.getCurricularPeriod().getChildOrder() + 1));
            writer.append(" ");
            writer.append(getBundleValue("EnumerationResources", previousContext.getCurricularPeriod().getAcademicPeriod()
                    .getName()
                    + ".ABBREVIATION"));
        } else {
            writer.append(previousContext.getParentCourseGroup().getName());
        }
        writer.endElement("td");

        encodeRegime();

        writer.startElement("td", this);
        if (!this.curricularCourse.isOptional()) {
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "contactLessonHoursAcronym")).append("-");
            writer.endElement("span");
            final BigDecimal contactLoad =
                    new BigDecimal(this.curricularCourse.getCompetenceCourse().getContactLoad(
                            previousContext.getCurricularPeriod().getChildOrder() + 1, executionYear)).setScale(2,
                            RoundingMode.HALF_EVEN);
            writer.append(contactLoad.toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            writer.append(
                    this.curricularCourse.getCompetenceCourse()
                            .getAutonomousWorkHours(previousContext.getCurricularPeriod().getChildOrder() + 1, executionYear)
                            .toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "totalLoadAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getCompetenceCourse()
                    .getTotalLoad(previousContext.getCurricularPeriod().getChildOrder() + 1, executionYear).toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "credits.abbreviation")).append(" ");
            writer.append(String.valueOf(this.curricularCourse.getCompetenceCourse().getEctsCredits(
                    previousContext.getCurricularPeriod().getChildOrder() + 1, executionYear)));
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
            encodeLink("editCurricularCourse.faces", "&contextID=" + this.previousContext.getExternalId()
                    + "&curricularCourseID=" + this.curricularCourse.getExternalId(), false, "edit");
            // if (this.executionYear == null) {
            writer.append(" , ");
            encodeLink("deleteCurricularCourseContext.faces", "&contextID=" + this.previousContext.getExternalId()
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
