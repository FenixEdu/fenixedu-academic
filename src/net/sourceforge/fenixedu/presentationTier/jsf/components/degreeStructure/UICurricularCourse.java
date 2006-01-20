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

    public UICurricularCourse(DegreeModule curricularCourse, Boolean toEdit, int depth, String tabs, Context previousContext) {
        super(curricularCourse, toEdit, depth, tabs);
        this.previousContext = previousContext;
        this.byYears = false;
    }

    public UICurricularCourse(CurricularCourse curricularCourse, Boolean toEdit, Context previousContext) {
        this.degreeModule = curricularCourse;
        this.toEdit = toEdit;
        this.previousContext = previousContext;
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
        System.out.println(buffer.toString());

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        encodeCurricularCourse();
    }

    private void encodeCurricularCourse() throws IOException {
        writer.startElement("tr", this);
        
        writer.startElement("td", this);
        writer.startElement("a", this);
        writer.writeAttribute("href", "viewCurricularCourse.faces?curricularCourseID=" + this.degreeModule.getIdInternal() + "&action=return", null);
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.degreeModule.getName());    
        } else {
            writer.append(this.degreeModule.getNameEn());
        }
        writer.endElement("a");
        writer.endElement("td");
        
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        if (!byYears) {
            writer.writeAttribute("width", "100px", null);
            writer.append(CurricularPeriodLabelFormatter.getFullLabel((CurricularPeriod)previousContext.getCurricularPeriod(), getLocale()));
        } else {
            //writer.writeAttribute("width", "300px", null);
            writer.append(previousContext.getCourseGroup().getName());
        }
        writer.endElement("td");
        
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright", null);
        writer.append(((CurricularCourse)this.degreeModule).getEctsCredits().toString());
        writer.endElement("td");
        
        if (this.toEdit) {
            encodeCurricularCourseOptions();    
        }
        
        writer.endElement("tr");
    }

    private void encodeCurricularCourseOptions() throws IOException {
        writer.startElement("td", this);
        encodeLink("editCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal(), "edit");
        writer.append(" , ");
        encodeLink("deleteCurricularCourseContext.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal(), "delete");
        writer.endElement("td");
    }
    
}
