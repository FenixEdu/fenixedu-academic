package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;

public class UICurricularCourse extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    private Context previousContext;
    private boolean byYears;
    
    public UICurricularCourse() {
        super();
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Boolean toEdit, int depth, String tabs, Context previousContext, Boolean showRules) {
        super(curricularCourse, toEdit, depth, tabs);
        this.previousContext = previousContext;
        this.byYears = false;
        this.showRules = showRules;
    }

    public UICurricularCourse(CurricularCourse curricularCourse, Boolean toEdit, Context previousContext, Boolean showRules) {
        this.degreeModule = curricularCourse;
        this.toEdit = toEdit;
        this.previousContext = previousContext;
        this.byYears = true; 
        this.showRules = showRules;
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
        System.out.println(buffer.toString());

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        encodeCurricularCourse();
    }

    private void encodeCurricularCourse() throws IOException {
        writer.startElement("tr", this);
        
        writer.startElement("td", this);
        writer.startElement("a", this);
        String action = "&action=" + ((this.toEdit) ? "build" : (String) this.facesContext.getExternalContext().getRequestParameterMap().get("action")); 
        writer.writeAttribute("href", "viewCurricularCourse.faces?curricularCourseID=" + this.degreeModule.getIdInternal() + action, null);
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.degreeModule.getName());    
        } else {
            writer.append(this.degreeModule.getNameEn());
        }
        writer.endElement("a");
        writer.endElement("td");
        
        writer.startElement("td", this);
        writer.writeAttribute("class", "smalltxt", null);
        writer.writeAttribute("align", "center", null);
        if (!byYears) {
            writer.writeAttribute("style", "width: 10em;", null);
            writer.append(CurricularPeriodLabelFormatter.getFullLabel((CurricularPeriod)previousContext.getCurricularPeriod(), getLocale()));
        } else {
            writer.append(previousContext.getCourseGroup().getName());
        }
        writer.endElement("td");
        writer.startElement("td", this);
        writer.writeAttribute("class", "highlight2 smalltxt", null);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("style", "width: 1em;", null);
        writer.append(this.getBundleValue("EnumerationResources", ((CurricularCourse)this.degreeModule).getRegime().toString() + ".ACRONYM"));
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "smalltxt", null);
        writer.writeAttribute("align", "aright", null);
        writer.writeAttribute("style", "width: 13em;", null);
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
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("style", "width: 7em;", null);
        writer.append(((CurricularCourse)this.degreeModule).getEctsCredits().toString());
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
        writer.writeAttribute("style", "width: 7em;", null);
        encodeLink("editCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal(), "edit");
        writer.append(" , ");
        encodeLink("deleteCurricularCourseContext.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal(), "delete");
        writer.endElement("td");
    }

    private void encodeCurricularRulesOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 7em;", null);
        encodeLink("../curricularRules/createCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&degreeModuleID=" + this.degreeModule.getIdInternal(), "setCurricularRule");
        writer.endElement("td");
    }

}
