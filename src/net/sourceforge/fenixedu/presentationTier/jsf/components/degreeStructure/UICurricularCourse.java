package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;

public class UICurricularCourse extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    private boolean byYears;
    
    public UICurricularCourse() {
        super();
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules, int depth, String tabs) {
        super(curricularCourse, previousContext, toEdit, showRules, depth, tabs);
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules) {
        super(curricularCourse, previousContext, toEdit, showRules, 0, null);
        this.byYears = true;
    }
    
    public String getFamily() {
        return UICurricularCourse.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        StringBuilder buffer = new StringBuilder();
        buffer.append(tabs);
        buffer.append("[LEVEL ").append(new Integer(this.depth)).append("]");
        buffer.append("[CC ").append(this.degreeModule.getIdInternal()).append("][");
        buffer.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR)).append("Y,");
        buffer.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER)).append("S] ");
        buffer.append(this.degreeModule.getName());
        //System.out.println(buffer.toString());

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        encodeCurricularCourse();
        if (this.showRules && this.degreeModule.hasAnyCurricularRules()) {
            encodeCurricularRules();    
        }
    }

    private void encodeCurricularCourse() throws IOException {
        writer.startElement("tr", this);
        
        writer.startElement("td", this);
        writer.startElement("a", this);
        String action = "&action=" + ((this.toEdit) ? "build" : (String) this.facesContext.getExternalContext().getRequestParameterMap().get("action")); 
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        String showRules = "&showRules=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("showRules");        
        String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");
        writer.writeAttribute("href", "viewCurricularCourse.faces?curricularCourseID=" + this.degreeModule.getIdInternal() + action + organizeBy + showRules + hideCourses, null);
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.degreeModule.getName());    
        } else {
            writer.append(this.degreeModule.getNameEn());
        }
        writer.endElement("a");
        writer.endElement("td");
        
        writer.startElement("td", this);
        writer.writeAttribute("class", "smalltxt", null);
        if (!byYears) {
            //writer.writeAttribute("style", "width: 10em;", null);
            writer.writeAttribute("align", "center", null);
            writer.append(CurricularPeriodLabelFormatter.getFullLabel((CurricularPeriod)previousContext.getCurricularPeriod(), getLocale(), true));
        } else {
            writer.append(previousContext.getCourseGroup().getName());
        }
        writer.endElement("td");
        writer.startElement("td", this);
        if (!((CurricularCourse) this.degreeModule).getType().equals(CurricularCourseType.OPTIONAL_COURSE)) {
            writer.writeAttribute("class", "highlight2 smalltxt", null);
            writer.writeAttribute("align", "center", null);
            writer.writeAttribute("style", "width: 1em;", null);
            writer.append(this.getBundleValue("EnumerationResources", ((CurricularCourse)this.degreeModule).getRegime().toString() + ".ACRONYM"));
        } else {
            writer.append("&nbsp;");
        }
        writer.endElement("td");

        writer.startElement("td", this);
        if (!((CurricularCourse) this.degreeModule).getType().equals(CurricularCourseType.OPTIONAL_COURSE)) {
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("class", "aright", null);
            //writer.writeAttribute("style", "width: 13em;", null);
            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "contactLessonHoursAcronym")).append("-");
            writer.endElement("span");
            writer.append(((CurricularCourse)this.degreeModule).getContactLoad(previousContext.getCurricularPeriod().getOrder()).toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            writer.append(((CurricularCourse)this.degreeModule).getAutonomousWorkHours(previousContext.getCurricularPeriod().getOrder()).toString()).append(" ");
            
            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "totalLoadAcronym")).append("-");
            writer.endElement("span");
            writer.append(((CurricularCourse)this.degreeModule).getTotalLoad(previousContext.getCurricularPeriod().getOrder()).toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("class", "aright", null);
            //writer.writeAttribute("style", "width: 7em;", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "credits.abbreviation")).append(" ");
            writer.append(((CurricularCourse)this.degreeModule).getEctsCredits(previousContext.getCurricularPeriod().getOrder()).toString());
        } else {
            writer.append("&nbsp;");
            writer.endElement("td");
            writer.startElement("td", this);
        }
        writer.endElement("td");

        if (this.toEdit) {
            if (!this.showRules) {
                encodeCurricularCourseOptions();    
            } else {
                encodeCurricularRulesOptions();
            }
        }
        
        writer.endElement("tr");
    }

    private void encodeCurricularCourseOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 9em;", null);
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        encodeLink("editCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal() + organizeBy, "edit");
        writer.append(" , ");
        encodeLink("deleteCurricularCourseContext.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal() + organizeBy, "delete");
        writer.endElement("td");
    }

    private void encodeCurricularRulesOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 9em;", null);
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");        
        encodeLink("../curricularRules/createCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&degreeModuleID=" + this.degreeModule.getIdInternal() + organizeBy + hideCourses, "setCurricularRule");
        writer.endElement("td");
    }

}
