package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.CurricularCourse;
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
        buffer.append("[LEVEL ").append(Integer.valueOf(this.depth)).append("]");
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
            encodeChildCourseGroups();
        } else {
            // root course group
            if (this.onlyStructure) {
                if (this.toEdit) {
                    encodeLink("createCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                    .get("degreeCurricularPlanID") + "&parentCourseGroupID=" + this.degreeModule.getIdInternal(), "create.course.group");

                    writer.startElement("table", this);
                    writer.writeAttribute("class", "showinfo1 sp thleft", null);
                }
                encodeChildCourseGroups();
                
                writer.endElement("table");
            } else {
                if (!((CourseGroup)this.degreeModule).hasAnyCourseGroupContexts()) {
                    writer.startElement("table", this);
                    writer.startElement("tr", this);
                    writer.startElement("td", this);
                    writer.writeAttribute("align", "center", null);
                    writer.startElement("i", this);
                    writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "empty.curricularPlan"));
                    writer.endElement("i");
                    writer.endElement("td");
                    writer.endElement("tr");
                    writer.endElement("table");
                } else {
                    encodeChildCourseGroups();
                    encodeSubtitles();
                }
            }
        }
    }

    private void encodeSubtitles() throws IOException {
        writer.startElement("ul", this);
        writer.writeAttribute("class", "nobullet", null);
        writer.writeAttribute("style", "padding-left: 0pt;", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "subtitle")).append(":\n");

        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHoursAcronym")).append(" - ");
        writer.endElement("span");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHours"));
        writer.endElement("li");
        
        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWorkAcronym")).append(" - ");
        writer.endElement("span");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWork"));
        writer.endElement("li");
        
        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "totalLoadAcronym")).append(" - ");
        writer.endElement("span");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "totalLoad")).append(" (");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHoursAcronym")).append(" + ");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWorkAcronym")).append(")");
        writer.endElement("li");
        
        writer.endElement("ul");
    }
    
    private void encodeSelf() throws IOException {
        Integer width = (this.onlyStructure) ? 50 : 70;
        
        if (!this.onlyStructure) {
            if (this.depth == BASE_DEPTH) {
                writer.startElement("table", this);
                writer.writeAttribute("class", "showinfo1 sp thleft", null);
                writer.writeAttribute("style", "width: " + width + "em;", null);
            } else if (this.depth > BASE_DEPTH) {
                writer.startElement("div", this);
                writer.writeAttribute("class", "indent" + String.valueOf((this.depth * 3)), null);
                writer.startElement("table", this);
                writer.writeAttribute("class", "showinfo1 sp thleft", null);
                writer.writeAttribute("style", "width: " + String.valueOf(width - (this.depth * 3)) +"em;", null);
            }
        }

        encodeHeader();

        if (!this.onlyStructure) {
            if (((CourseGroup)this.degreeModule).getContextsWithCurricularCourses().size() > 0) {
                List<Double> sums = encodeChildCurricularCourses();
                encodeSumsFooter(sums);
            } else {
                encodeEmptyCourseGroupInfo();   
            }
            writer.endElement("table");
            if (this.depth > BASE_DEPTH) {
                writer.endElement("div");
            }
        }
    }

    private void encodeHeader() throws IOException {
        writer.startElement("tr", this);
        if (!this.onlyStructure) {
            writer.writeAttribute("class", "bgcolor2", null);
            writer.startElement("th", this);
            writer.writeAttribute("colspan", (this.toEdit) ? 3 : 5, null);
            writer.startElement("strong", this);
        } else {
            writer.startElement("td", this);
            if (this.depth == BASE_DEPTH) {
                writer.startElement("strong", this);       
            } else if (this.depth > BASE_DEPTH) {
                writer.writeAttribute("class", "pleft" + String.valueOf((this.depth + 2)), null);
            }
        }

        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.degreeModule.getName()).append(" ");
        } else {
            writer.append(this.degreeModule.getNameEn()).append(" ");
        }
        
        if (!this.onlyStructure) {
            writer.endElement("strong");
            writer.endElement("th");
        } else {
            if (this.depth == BASE_DEPTH) {
                writer.endElement("strong");
            }
            writer.endElement("td");
        }
        
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
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright", null);
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
        writer.endElement("td");
    }

    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);
        encodeLink("createCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "create.curricular.course");
        writer.append(" , ");
        encodeLink("associateCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.degreeModule.getIdInternal(), "associate.curricular.course");
        writer.endElement("td");
    }
    
    private List<Double> encodeChildCurricularCourses() throws IOException {
        double sumContactLoad = 0.0;
        double sumAutonomousWork = 0.0;
        double sumTotalLoad = 0.0;
        double sumCredits = 0.0;
        for (Context context : ((CourseGroup)this.degreeModule).getContextsWithCurricularCourses()) {
            DegreeModule degreeModule = context.getDegreeModule();
            
            sumContactLoad += ((CurricularCourse)degreeModule).getContactLoad(context.getCurricularPeriod().getOrder());
            sumAutonomousWork += ((CurricularCourse)degreeModule).getAutonomousWorkHours(context.getCurricularPeriod().getOrder());
            sumTotalLoad += ((CurricularCourse)degreeModule).getTotalLoad(context.getCurricularPeriod().getOrder());
            sumCredits += degreeModule.getEctsCredits();
            new UICurricularCourse(context.getDegreeModule(), this.toEdit, this.depth, this.tabs + "\t", context).encodeBegin(facesContext);
        }
        
        List<Double> result = new ArrayList<Double>(4);
        result.add(sumContactLoad);
        result.add(sumAutonomousWork);
        result.add(sumTotalLoad);
        result.add(sumCredits);
        
        return result;
    }
    
    private void encodeSumsFooter(List<Double> sums) throws IOException {
        writer.startElement("tr", this);

        writer.startElement("td", this);
        writer.writeAttribute("colspan", 3, null);
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "highlight2 smalltxt", null);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("style", "width: 13em;", null);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHoursAcronym")).append("-");
        writer.endElement("span");
        writer.append(String.valueOf(sums.get(0))).append(" ");

        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
        writer.endElement("span");
        writer.append(String.valueOf(sums.get(1))).append(" ");

        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "totalLoadAcronym")).append("-");
        writer.endElement("span");
        writer.append(String.valueOf(sums.get(2))).append(" ");
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "aright highlight2", null);
        writer.writeAttribute("style", "width: 7em;", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "credits")).append(" ");
        writer.append(String.valueOf(sums.get(3)));
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
            writer.writeAttribute("colspan", 5, null);    
        } else {
            writer.writeAttribute("colspan", 3, null);
        }
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "no.associated.curricular.courses"));
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
