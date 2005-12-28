package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.degreeStructure.IContext;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.IDegreeModule;

public class UICourseGroup extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    public UICourseGroup() {
        super();
    }
    
    public UICourseGroup(IDegreeModule courseGroup, Boolean onlyStructure, Boolean toEdit, int depth, String tabs) {
        super(courseGroup, onlyStructure, toEdit, depth, tabs);
    }

    public String getFamily() {
        return UICourseGroup.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(tabs);
        buffer.append("[LEVEL ").append(new Integer(this.depth)).append("]");
        buffer.append("[CG ").append(this.degreeModule.getIdInternal()).append("] ").append(this.degreeModule.getName());
        System.out.println(buffer);

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        encodeCourseGroup();
    }

    private void encodeCourseGroup() throws IOException {
        if (((ICourseGroup)this.degreeModule).getNewDegreeCurricularPlan() == null) {
            // this is not the root course group
            encodeSelf();
        } else {
            // root course group
            if (this.onlyStructure && this.toEdit) {
                writer.startElement("table", this);
                writer.writeAttribute("border", 1, null);
    
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
        }
        encodeChildCourseGroups();
    }

    private void encodeSelf() throws IOException {
        writer.startElement("table", this);
        writer.writeAttribute("border", 1, null);
        encodeHeader();
        if (!this.onlyStructure) {
            if (((ICourseGroup)this.degreeModule).getContextsWithCurricularCourses().size() > 0) {
                encodeChildCurricularCourses();
            } else {
                encodeEmptyCourseGroupInfo();   
            }            
        }
        
        if (!this.onlyStructure && this.toEdit) {
            encodeCourseGroupOptions();
        }

        if (!this.onlyStructure && ((ICourseGroup)this.degreeModule).getContextsWithCurricularCourses().size() > 0) {
            encodeTotalCreditsFooter();
        }
        writer.endElement("table");
    }

    private void encodeHeader() throws IOException {
        if (this.depth == BASE_DEPTH + 1) {
            writer.writeAttribute("class", "style2 margintop", null);    
        } else if (this.depth > BASE_DEPTH + 1) {
            writer.writeAttribute("class", "style2 indent" + new Integer(this.depth), null);
        }
        writer.startElement("tr", this);
        writer.startElement("th", this);
        if (this.toEdit) {
            writer.writeAttribute("colspan", 4, null);    
        } else {
            writer.writeAttribute("colspan", 3, null);
        }
        writer.startElement("strong", this);
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.degreeModule.getName()).append(" ");    
        } else {
            writer.append(this.degreeModule.getNameEn()).append(" ");
        }
        writer.endElement("strong");
        if (this.onlyStructure && this.toEdit) {
            encodeEditOptions();
        }
        writer.endElement("th");
        writer.endElement("tr");
    }

    private void encodeEditOptions() throws IOException {
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
    }

    private void encodeChildCurricularCourses() throws IOException {
        for (IContext context : ((ICourseGroup)this.degreeModule).getContextsWithCurricularCourses()) {
            new UICurricularCourse(context.getDegreeModule(), this.toEdit, this.depth, this.tabs + "\t", context).encodeBegin(facesContext);
        }
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
        for (IContext context : ((ICourseGroup)this.degreeModule).getContextsWithCourseGroups()) {
            new UICourseGroup(context.getDegreeModule(), this.onlyStructure, this.toEdit, this.depth + 1, this.tabs + "\t").encodeBegin(facesContext);
        }
    }

    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("tr", this);

        writer.startElement("td", this);
        writer.writeAttribute("colspan", 3, null);
        writer.endElement("td");
        
        writer.startElement("td", this);
        writer.writeAttribute("style", "width: 130px;", null);
        writer.startElement("ul", this);
        writer.writeAttribute("style", "margin: 0; padding: 0.25em 1em;", null);
        
        writer.startElement("li", this);
        encodeLink("createCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "create.curricular.course");
        writer.endElement("li");
        writer.startElement("li", this);
        encodeLink("associateCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "associate.curricular.course");
        writer.endElement("li");
        
        writer.endElement("ul");
        writer.endElement("td");
        
        writer.endElement("tr");
    }

    private void encodeTotalCreditsFooter() throws IOException {
        writer.startElement("tr", this);

        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.append(this.getBundleValue(facesContext, "ServidorApresentacao/BolonhaManagerResources", "credits")).append(": ");
        writer.endElement("td");
        writer.startElement("td", this);
        writer.append("&nbsp;");
        writer.endElement("td");
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        writer.append(this.degreeModule.computeEctsCredits().toString());
        writer.endElement("td");        
        if (this.toEdit) {
            writer.startElement("td", this);
            writer.append("&nbsp;");
            writer.endElement("td");
        }
        
        writer.endElement("tr");
    }

}
