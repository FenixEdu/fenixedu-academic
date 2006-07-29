package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class UICourseGroup extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    private CourseGroup courseGroup;
    private Boolean onlyStructure;
    private Boolean toOrder;
    private Boolean hideCourses;
    private Boolean reportsAvailable;
    
    public UICourseGroup() {
        super();
        this.courseGroup = (CourseGroup) super.degreeModule;
    }

    public UICourseGroup(DegreeModule courseGroup, Context previousContext, Boolean toEdit, Boolean showRules, int depth, String tabs, Boolean onlyStructure, Boolean toOrder, Boolean hideCourses, Boolean reportsAvailable, ExecutionYear executionYear) throws IOException {
        super(courseGroup, previousContext, toEdit, showRules, depth, tabs, executionYear);
        
        if (toOrder && (!onlyStructure || !toEdit)) {
            throw new IOException("incorrect.component.usage");
        }
        this.courseGroup = (CourseGroup) super.degreeModule;
        this.onlyStructure = onlyStructure;
        this.toOrder = toOrder;
        this.hideCourses = hideCourses;
        this.reportsAvailable = reportsAvailable;
    }

    public String getFamily() {
        return UICourseGroup.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        log(false);
        encodeCourseGroup(facesContext);
    }

    private void log(boolean on) {
        if (on) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(tabs);
            buffer.append("[LEVEL ").append(Integer.valueOf(this.depth)).append("]");
            buffer.append("[CG ").append(this.courseGroup.getIdInternal()).append("] ").append(this.courseGroup.getName());
            System.out.println(buffer);
        }
    }

    private void encodeCourseGroup(FacesContext facesContext) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        
        if (this.courseGroup.isRoot()) {
            encodeVisibleRoot();
        } else {
            encodeSelf();
            encodeChildCourseGroups();
        }
    }

    private void encodeRoot() throws IOException {
        if (reportsAvailable) {
            writer.startElement("p", this);
            writer.startElement("a", this);
            this.encodeLinkHref("../../bolonhaManager/curricularPlans/courseGroupReport.faces", "&courseGroupID=" + this.courseGroup.getIdInternal(), true);
            writer.write("Relatórios de Plano Curricular");
            writer.endElement("a");
            writer.endElement("p");
        }
        if (this.onlyStructure) {
            if (this.toEdit) {
                if (!this.toOrder) {
                    writer.startElement("p", this);
                    writer.writeAttribute("style", "mtop05", null);
                    encodeLink("createCourseGroup.faces", "&parentCourseGroupID=" + this.courseGroup.getIdInternal() + "&toOrder=false", false, "create.course.group.root");
                    writer.endElement("p");
                }
            }                            

            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mbottom0 mtop05", null);
            encodeChildCourseGroups();
            writer.endElement("table");
        } else {
            if (this.courseGroup.hasAnyChildContexts()) {
                encodeChildCourseGroups();
            } else {
                writer.startElement("table", this);
                writer.startElement("tr", this);
                writer.startElement("td", this);
                writer.writeAttribute("align", "center", null);
                writer.startElement("i", this);
                writer.append(this.getBundleValue("BolonhaManagerResources", "empty.curricularPlan"));
                writer.endElement("i");
                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("table");
            }
        }
    }

    private void encodeVisibleRoot() throws IOException {
        if (this.onlyStructure) {
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mbottom0 mtop05", null);
            encodeHeader();
            encodeChildCourseGroups();
            writer.endElement("table");
        } else {
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mvert0", null);
            writer.writeAttribute("style", "width: 70em;", null);

            encodeHeader();
            if (this.courseGroup.hasAnyChildContexts()) {
                if (this.showRules && this.courseGroup.hasAnyCurricularRules()) {
                    encodeCurricularRules();    
                }
                writer.endElement("table");

                if (!this.hideCourses && this.courseGroup.getSortedChildContextsWithCurricularCoursesByExecutionYear(executionYear).size() > 0) {
                    encodeChildCurricularCourses(70, (this.depth + 1) * 3);
                    //encodeSumsFooter(sums);
                }

                encodeChildCourseGroups();
            } else {
                writer.startElement("table", this);
                writer.startElement("tr", this);
                writer.startElement("td", this);
                writer.writeAttribute("align", "center", null);
                writer.startElement("i", this);
                writer.append(this.getBundleValue("BolonhaManagerResources", "empty.curricularPlan"));
                writer.endElement("i");
                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("table");
            }
        }
    }

    private void encodeChildCourseGroups() throws IOException {
        for (Context context : this.courseGroup.getSortedChildContextsWithCourseGroupsByExecutionYear(this.executionYear)) {
            new UICourseGroup(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth + 1, this.tabs + "\t", this.onlyStructure, this.toOrder, this.hideCourses, this.reportsAvailable, this.executionYear).encodeBegin(facesContext);
        }
    }

    private void encodeSelf() throws IOException {
        int width = (this.onlyStructure) ? 50 : 70;
        int courseGroupIndent = this.depth * 3;
        
        if (!this.onlyStructure) {
            writer.startElement("div", this);
            writer.writeAttribute("style", "padding-left: " + courseGroupIndent + "em;", null);
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mvert0", null);
            writer.writeAttribute("style", "width: " + String.valueOf(width - (this.depth * 3)) +"em;", null);
        }

        encodeHeader();
        
        if (!this.onlyStructure) {
            if (this.showRules && this.courseGroup.hasAnyCurricularRules()) {
                encodeCurricularRules();    
            }

            writer.endElement("table");
            writer.endElement("div");
            
            if (!this.hideCourses && this.courseGroup.getSortedChildContextsWithCurricularCoursesByExecutionYear(executionYear).size() > 0) {
                encodeChildCurricularCourses(width, courseGroupIndent);
                //encodeSumsFooter(sums);
            } else {
                //encodeEmptyCourseGroupInfo();
            }
        }
    }

    private void encodeHeader() throws IOException {
        writer.startElement("tr", this);

        encodeName(reportsAvailable);    
        
        if (this.toEdit) {
            if (this.onlyStructure) {
                if (this.toOrder) {
                    if (!this.courseGroup.isRoot()) {
                        encodeOrderOptions();    
                    }
                } else {
                    encodeEditOptions();
                }
            } else {
                encodeCourseGroupOptions();
            }
        }
        
        writer.endElement("tr");
    }

    private void encodeName(boolean linkable) throws IOException {
        if (this.onlyStructure) {
            writer.startElement("td", this);
            if (this.courseGroup.isRoot()) {
                writer.startElement("strong", this);       
            } else {
                writer.writeAttribute("style", "padding-left: " + String.valueOf((this.depth + 2)) + "em;", null);
            }
        } else {
            writer.writeAttribute("class", "bgcolor2", null);
            writer.startElement("th", this);
            writer.writeAttribute("class", "aleft", null);
            writer.writeAttribute("colspan", (this.toEdit) ? 3 : 5, null);
        }
        
        String name = null;
        if (!facesContext.getViewRoot().getLocale().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            name = this.courseGroup.getName() + " ";    
        } else {
            name = this.courseGroup.getNameEn() + " ";
        }
        if (linkable) {
            writer.startElement("a", this);
            this.encodeLinkHref("../../bolonhaManager/curricularPlans/courseGroupReport.faces", "&courseGroupID=" + this.courseGroup.getIdInternal(), true);
            writer.append(name);
            writer.endElement("a");
        } else {
            writer.append(name);
        }
         
        if (this.onlyStructure) {
            if (this.courseGroup.isRoot()) {
                writer.endElement("strong");       
            } 
            writer.endElement("td");
        } else {
            writer.endElement("th");
        }
    }

    private void encodeOrderOptions() throws IOException {
        writer.startElement("td", this);
        writer.append("(");

        encodeOrderOption(0, "top", false);
        encodeOrderOption(this.previousContext.getParentCourseGroup().getChildContextsCount() - 1, "end", false);
        encodeOrderOption(this.previousContext.getOrder() - 1, "up", false);
        encodeOrderOption(this.previousContext.getOrder() + 1, "down", true);
        
        writer.append(") ");
    }
    
    private void encodeOrderOption(Integer posToTest, String label, boolean lastOption) throws IOException {
        encodeLink("orderCourseGroup.faces", "&courseGroupID=" + this.courseGroup.getIdInternal() + "&contextID=" + this.previousContext.getIdInternal() + "&pos=" + posToTest + "&toOrder=true", false, label);
        if (!lastOption) {
            writer.append(" , ");
        }
    }

    private void encodeEditOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "aleft", null);
        writer.append("(");
        String createAssociateAditionalParameters = "&parentCourseGroupID=" + this.courseGroup.getIdInternal() + "&toOrder=false";
        String editAndDeleteAditionalParameters = "&courseGroupID=" + this.courseGroup.getIdInternal() + ((!this.courseGroup.isRoot()) ? ("&contextID=" + this.previousContext.getIdInternal()) : "") + "&toOrder=false";
        encodeLink("createCourseGroup.faces", createAssociateAditionalParameters, false, "create.course.group");
        writer.append(" , ");
        encodeLink("associateCourseGroup.faces", createAssociateAditionalParameters, false, "associate.course.group");
        writer.append(" , ");
        encodeLink("editCourseGroup.faces", editAndDeleteAditionalParameters, false, "edit");
        if (!this.courseGroup.isRoot() && this.executionYear == null) {
            writer.append(" , ");
            encodeLink("deleteCourseGroup.faces", editAndDeleteAditionalParameters, false, "delete");
        }
        writer.append(") ");
        writer.endElement("td");
    }

    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);
        if (this.showRules) {
            encodeLink("../curricularRules/createCurricularRule.faces", "&degreeModuleID=" + this.courseGroup.getIdInternal(), false, "setCurricularRule");
        } else {
            encodeLink("createCurricularCourse.faces", "&courseGroupID=" + this.courseGroup.getIdInternal(), false, "create.curricular.course");
            writer.append(" , ");
            encodeLink("associateCurricularCourse.faces", "&courseGroupID=" + this.courseGroup.getIdInternal(), false, "associate.curricular.course");
        }
        writer.endElement("th");
    }
    
    private void encodeChildCurricularCourses(int width, int courseGroupIndent) throws IOException {
        writer.startElement("div", this);
        writer.writeAttribute("class", (this.courseGroup.isRoot()) ? "indent3" : "indent" + (courseGroupIndent + 3), null);
        writer.startElement("table", this);
        writer.writeAttribute("class", "showinfo3 mvert0", null);
        writer.writeAttribute("style", "width: " + (width - (this.depth * 3) - 3)  + "em;", null);

        for (Context context : this.courseGroup.getSortedChildContextsWithCurricularCoursesByExecutionYear(executionYear)) {
            new UICurricularCourse(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth, this.tabs + "\t", this.executionYear).encodeBegin(facesContext);
        }
        
        writer.endElement("table");
        writer.endElement("div");
    }
    
    private void encodeSumsFooter(List<Double> sums) throws IOException {
        writer.startElement("tr", this);

        writer.startElement("td", this);
        writer.writeAttribute("colspan", 3, null);
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "highlight2 smalltxt", null);
        writer.writeAttribute("align", "right", null);
        
        encodeSumsLoadFooterElement(sums, "contactLessonHoursAcronym", 0);
        encodeSumsLoadFooterElement(sums, "autonomousWorkAcronym", 1);
        encodeSumsLoadFooterElement(sums, "totalLoadAcronym", 2);
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "aright highlight2", null);
        writer.append(this.getBundleValue("BolonhaManagerResources", "credits")).append(" ");
        writer.append(String.valueOf(sums.get(3)));
        writer.endElement("td");
        
        if (this.toEdit) {
            writer.startElement("td", this);
            writer.append("&nbsp;");
            writer.endElement("td");
        }

        writer.endElement("tr");
    }
    
    private void encodeSumsLoadFooterElement(List<Double> sums, String acronym, int order) throws IOException {
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("BolonhaManagerResources", acronym)).append("-");
        writer.endElement("span");
        writer.append(String.valueOf(sums.get(order))).append(" ");
    }

}
