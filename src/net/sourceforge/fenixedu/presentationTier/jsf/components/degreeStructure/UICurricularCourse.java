package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;

public class UICurricularCourse extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    private CurricularCourse curricularCourse;
    private boolean byYears;
    private ExecutionPeriod lastExecutionPeriod;
    
    public UICurricularCourse() {
        super();
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules, int depth, String tabs, ExecutionYear executionYear, String module) {
        super(curricularCourse, previousContext, toEdit, showRules, depth, tabs, executionYear, module);
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules, ExecutionYear executionYear, String module) {
        super(curricularCourse, previousContext, toEdit, showRules, 0, null, executionYear, module);
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = true;
        
        if (this.executionYear == null) {
            this.executionYear = ExecutionYear.readCurrentExecutionYear(); 
        }
        
        lastExecutionPeriod = this.executionYear.getLastExecutionPeriod();
    }
    
    public String getFamily() {
        return UICurricularCourse.COMPONENT_FAMILY;
    }

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
        
        if (!byYears && this.curricularCourse.isBolonhaDegree() && this.curricularCourse.isAnual()) {
            encodeInNextPeriod(facesContext);
        }
    }

    private void log(boolean on) {
        if (on) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(tabs);
            buffer.append("[LEVEL ").append(new Integer(this.depth)).append("]");
            buffer.append("[CC ").append(this.curricularCourse.getIdInternal()).append("][");
            buffer.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR)).append("Y,");
            buffer.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER)).append("S] ");
            buffer.append(this.curricularCourse.getName(lastExecutionPeriod));
            System.out.println(buffer.toString());
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

        String name = null;
        if (!facesContext.getViewRoot().getLocale().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            name = this.curricularCourse.getName(lastExecutionPeriod);
        } else {
            name = this.curricularCourse.getNameEn(lastExecutionPeriod);
        }
        
        if (linkable) {
            writer.startElement("a", this);
            if (this.curricularCourse.isBolonhaDegree()) {
        	encodeLinkHref("viewCurricularCourse.faces", "&curricularCourseID=" + this.curricularCourse.getIdInternal(), false);
            } else {
        	encodeNonBolonhaLinkHref();
            }
            writer.append(name);
            writer.endElement("a");
        } else {
            writer.append(name);
        }
        
        writer.endElement("td");
    }
    
    private void encodeNonBolonhaLinkHref() throws IOException {

        final StringBuilder href = new StringBuilder();
        href.append("showCourseSite.do?method=showCurricularCourseSite");

        href.append("&curricularCourseID=").append(this.curricularCourse.getIdInternal());
        href.append("&degreeID=").append(this.curricularCourse.getDegree().getIdInternal());
        href.append("&degreeCurricularPlanID=").append(this.curricularCourse.getDegreeCurricularPlan().getIdInternal());
        
        final Map requestParameterMap = this.facesContext.getExternalContext().getRequestParameterMap();
        if (this.executionYear != null) {
            final ExecutionPeriod executionPeriod = this.executionYear.getLastExecutionPeriod();
            href.append("&executionPeriodOID=").append(executionPeriod.getIdInternal());
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
        if (!this.curricularCourse.isOptional() && this.curricularCourse.hasRegime()) {
            writer.writeAttribute("class", "highlight2 smalltxt", null);
            writer.writeAttribute("align", "center", null);
            writer.writeAttribute("style", "width: 1em;", null);
            writer.append(this.getBundleValue("EnumerationResources", this.curricularCourse.getRegime().toString() + ".ACRONYM"));
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
            writer.append(this.curricularCourse.getContactLoad(curricularPeriod).toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getAutonomousWorkHours(curricularPeriod).toString()).append(" ");
            
            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "totalLoadAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getTotalLoad(curricularPeriod).toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "credits.abbreviation")).append(" ");
            writer.append(this.curricularCourse.getEctsCredits(curricularPeriod).toString());
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
        	writer.append(getBundleValue("EnumerationResources", previousContext.getCurricularPeriod().getParent().getPeriodType().name() + ".ABBREVIATION"));
        	writer.append(", ");
            }
             
            writer.append(String.valueOf(previousContext.getCurricularPeriod().getChildOrder() + 1));
            writer.append(" "); 
            writer.append(getBundleValue("EnumerationResources", previousContext.getCurricularPeriod().getPeriodType().name() + ".ABBREVIATION"));
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
            writer.append(this.curricularCourse.getCompetenceCourse().getContactLoad(previousContext.getCurricularPeriod().getChildOrder() + 1).toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getCompetenceCourse().getAutonomousWorkHours(previousContext.getCurricularPeriod().getChildOrder() + 1).toString()).append(" ");
            
            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "totalLoadAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getCompetenceCourse().getTotalLoad(previousContext.getCurricularPeriod().getChildOrder() + 1).toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("align", "right", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "credits.abbreviation")).append(" ");
            writer.append(String.valueOf(this.curricularCourse.getCompetenceCourse().getEctsCredits(previousContext.getCurricularPeriod().getChildOrder() + 1)));
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
        encodeLink("editCurricularCourse.faces", "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.curricularCourse.getIdInternal(), false, "edit");
        if (this.executionYear == null) {
            writer.append(" , ");
            encodeLink("deleteCurricularCourseContext.faces", "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.curricularCourse.getIdInternal(), false, "delete");
        }
        writer.endElement("td");
    }

    private void encodeCurricularRulesOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 9em;", null);
        encodeLink(module + "/curricularRules/createCurricularRule.faces", "&degreeModuleID=" + this.curricularCourse.getIdInternal(), false, "setCurricularRule");
        writer.endElement("td");
    }

}
