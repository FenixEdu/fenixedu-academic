package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class UICourseGroup extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    public UICourseGroup() {
        super();
    }
    
    public UICourseGroup(DegreeModule courseGroup, Boolean onlyStructure, Boolean toEdit, int depth, String tabs) {
        super(courseGroup, onlyStructure, toEdit, depth, tabs);
    }

    public String getFamily() {
        return UICourseGroup.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        StringBuilder buffer = new StringBuilder();
        buffer.append(tabs);
        buffer.append("[LEVEL ").append(new Integer(this.depth)).append("]");
        buffer.append("[CG ").append(this.degreeModule.getIdInternal()).append("] ").append(this.degreeModule.getName());
        System.out.println(buffer);

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        encodeCourseGroup();
    }

    private void encodeCourseGroup() throws IOException {
        if (!this.degreeModule.isRoot()) {
            // this is not the root course group
            encodeSelf();
        } else {
            // root course group
            if (this.onlyStructure) {
                if (this.toEdit) {
                    writer.startElement("table", this);
                    writer.writeAttribute("class", "style2", null);
                    writer.writeAttribute("width", "60%", null);
                    writer.startElement("tr", this);
                    writer.startElement("th", this);
                    writer.append("(");
                    encodeLink("createCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                    .get("degreeCurricularPlanID") + "&parentCourseGroupID=" + this.degreeModule.getIdInternal(), "create.course.group");
                    writer.append(") ");
                    writer.endElement("th");
                    writer.endElement("tr");
                
                    encodeChildCurricularCourses();
                    writer.endElement("table");
                }
            } else if (!((CourseGroup)this.degreeModule).hasAnyCourseGroupContexts()) {
                writer.startElement("table", this);
                writer.startElement("tr", this);
                writer.startElement("td", this);
                writer.writeAttribute("align", "center", null);
                writer.startElement("i", this);
                writer.append(this.getBundleValue(facesContext, "ServidorApresentacao/BolonhaManagerResources", "empty.curricularPlan"));
                writer.endElement("i");
                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("table");
            }
        }
        encodeChildCourseGroups();
    }

    private void encodeSelf() throws IOException {
        writer.startElement("table", this);
        if (this.depth == BASE_DEPTH + 1) {
            writer.writeAttribute("class", "style2", null);    
        } else if (this.depth > BASE_DEPTH + 1) {
            writer.writeAttribute("class", "style2 indent" + new Integer(this.depth), null);
        }
        if (this.onlyStructure) {
            writer.writeAttribute("width", "60%", null);
        }
        encodeHeader();
        if (!this.onlyStructure) {
            if (((CourseGroup)this.degreeModule).getContextsWithCurricularCourses().size() > 0) {
                encodeChildCurricularCourses();
                encodeTotalCreditsFooter();
            } else {
                encodeEmptyCourseGroupInfo();   
            }
        }
        
        writer.endElement("table");
    }

    private void encodeHeader() throws IOException {
        writer.startElement("tr", this);
        
        writer.startElement("th", this);
        writer.writeAttribute("colspan", 3, null);
        writer.writeAttribute("class", "aleft", null);
        writer.startElement("strong", this);
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.degreeModule.getName()).append(" ");
        } else {
            writer.append(this.degreeModule.getNameEn()).append(" ");
        }
        writer.endElement("strong");
        writer.endElement("th");
        
        if (this.toEdit) {
            if (this.onlyStructure) {
                encodeEditOptions();
            } else {
                encodeCourseGroupOptions();
            }
        }
        
        writer.endElement("tr");
    }

    private void encodeEditOptions() throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("width", "150px", null);
        writer.append("(");
        encodeLink("createCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&parentCourseGroupID=" + this.degreeModule.getIdInternal(), "create.course.group");
        writer.append(" , ");
        encodeLink("editCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "edit");
        writer.append(" , ");
        encodeLink("deleteCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "delete");
        writer.append(") ");
        writer.endElement("th");
    }

    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aleft", null);
        writer.writeAttribute("width", "180px", null);
        encodeLink("createCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "create.curricular.course");
        writer.append(" , ");
        encodeLink("associateCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "associate.curricular.course");
        writer.endElement("th");
    }
    
    private void encodeChildCurricularCourses() throws IOException {
        for (Context context : ((CourseGroup)this.degreeModule).getContextsWithCurricularCourses()) {
            new UICurricularCourse(context.getDegreeModule(), this.toEdit, this.depth, this.tabs + "\t", context).encodeBegin(facesContext);
        }
    }
    
    private void encodeTotalCreditsFooter() throws IOException {
        writer.startElement("tr", this);
        
        writer.startElement("td", this);
        writer.writeAttribute("colspan", 2, null);
        writer.endElement("td");
        
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright highlight01", null);
        writer.writeAttribute("width", "73px", null);
        writer.append(this.getBundleValue(facesContext, "ServidorApresentacao/BolonhaManagerResources", "credits")).append(" ");
        writer.append(this.degreeModule.computeEctsCredits().toString());
        writer.endElement("td");        
        if (this.toEdit) {
            writer.startElement("td", this);
            writer.append("&nbsp;");
            writer.endElement("td");
        }
        
        writer.endElement("tr");
    }
    
    private void encodeEmptyCourseGroupInfo() throws IOException {
        writer.startElement("tr", this);
        writer.startElement("td", this);
        if (this.toEdit) {
            writer.writeAttribute("colspan", 4, null);    
        } else {
            writer.writeAttribute("colspan", 3, null);
        }
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(this.getBundleValue(facesContext, "ServidorApresentacao/BolonhaManagerResources", "no.associated.curricular.courses"));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
    }

    private void encodeChildCourseGroups() throws IOException {
        for (Context context : ((CourseGroup)this.degreeModule).getContextsWithCourseGroups()) {
            new UICourseGroup(context.getDegreeModule(), this.onlyStructure, this.toEdit, this.depth + 1, this.tabs + "\t").encodeBegin(facesContext);
        }
    }

}
