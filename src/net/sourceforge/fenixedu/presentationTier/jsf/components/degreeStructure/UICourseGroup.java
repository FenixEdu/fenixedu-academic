package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;

public class UICourseGroup extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICourseGroup";

    private CourseGroup courseGroup;
    private Boolean onlyStructure;
    private Boolean toOrder;
    private Boolean hideCourses;
    
    public UICourseGroup() {
        super();
        this.courseGroup = (CourseGroup) super.degreeModule;
    }

    public UICourseGroup(DegreeModule courseGroup, Context previousContext, Boolean toEdit, Boolean showRules, int depth, String tabs, Boolean onlyStructure, Boolean toOrder, Boolean hideCourses) throws IOException {
        super(courseGroup, previousContext, toEdit, showRules, depth, tabs);
        
        if (toOrder && (!onlyStructure || !toEdit)) {
            throw new IOException("incorrect.component.usage");
        }
        this.courseGroup = (CourseGroup) super.degreeModule;
        this.onlyStructure = onlyStructure;
        this.toOrder = toOrder;
        this.hideCourses = hideCourses;
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
            encodeRoot();
        } else {
            encodeSelf();
            encodeChildCourseGroups();
        }
    }

    private void encodeRoot() throws IOException {
        if (this.onlyStructure) {
            if (this.toEdit) {
                if (!this.toOrder) {
                    String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
                    writer.startElement("p", this);
                    writer.writeAttribute("style", "mtop05", null);
                    encodeLink("createCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap().get("degreeCurricularPlanID") + "&parentCourseGroupID=" + this.courseGroup.getIdInternal() + organizeBy + "&toOrder=false", "create.course.group.root");
                    writer.endElement("p");
                }
            }                            

            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3 mbottom0 mtop05", null);
            encodeChildCourseGroups();
            writer.endElement("table");
        } else {
            if (!this.courseGroup.hasAnyChildContexts()) {
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
            } else {
                encodeChildCourseGroups();
                if (this.courseGroup.getParentDegreeCurricularPlan().getDegreeStructure().hasAnyChilds()) {
                    encodeSubtitles();
                }
            }
        }
    }

    private void encodeChildCourseGroups() throws IOException {
        for (Context context : this.courseGroup.getSortedChildContextsWithCourseGroups()) {
            new UICourseGroup(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth + 1, this.tabs + "\t", this.onlyStructure, this.toOrder, this.hideCourses).encodeBegin(facesContext);
        }
    }

    private void encodeSubtitles() throws IOException {
        writer.startElement("ul", this);
        writer.writeAttribute("class", "nobullet mtop2", null);
        writer.writeAttribute("style", "padding-left: 0pt; font-style: italic;", null);
        writer.append(this.getBundleValue("BolonhaManagerResources", "subtitle")).append(":\n");

        encodeSubtitleElement("EnumerationResources", RegimeType.SEMESTRIAL.toString() + ".ACRONYM", RegimeType.SEMESTRIAL.toString(), null);
        encodeSubtitleElement("EnumerationResources", RegimeType.ANUAL.toString() + ".ACRONYM", RegimeType.ANUAL.toString(), null);

        encodeSubtitleElement("BolonhaManagerResources", "contactLessonHoursAcronym", "contactLessonHours", null);
        encodeSubtitleElement("BolonhaManagerResources", "autonomousWorkAcronym", "autonomousWork", null);

        StringBuilder explanation = new StringBuilder();
        explanation.append(" (");
        explanation.append(this.getBundleValue("BolonhaManagerResources", "contactLessonHoursAcronym"));
        explanation.append(" + ");
        explanation.append(this.getBundleValue("BolonhaManagerResources", "autonomousWorkAcronym"));
        explanation.append(")");
        encodeSubtitleElement("BolonhaManagerResources", "totalLoadAcronym", "totalLoad", explanation);
        
        writer.endElement("ul");
    }

    private void encodeSubtitleElement(String bundle, String acronym, String full, StringBuilder explanation) throws IOException {
        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("" + bundle, acronym)).append(" - ");
        writer.endElement("span");
        writer.append(this.getBundleValue("" + bundle, full));
        if (explanation != null) {
            writer.append(explanation);
        }
        writer.endElement("li");
    }
    
    private void encodeSelf() throws IOException {
        int width = (this.onlyStructure) ? 50 : 70;
        int courseGroupIndent = this.depth * 3;
        
        if (!this.onlyStructure) {
            if (this.depth == BASE_DEPTH) {
                writer.startElement("table", this);
                writer.writeAttribute("class", "showinfo3 mvert0", null);
                writer.writeAttribute("style", "width: " + width + "em;", null);
            } else if (this.depth > BASE_DEPTH) {
                writer.startElement("div", this);
                writer.writeAttribute("style", "padding-left: " + courseGroupIndent + "em;", null);
                writer.startElement("table", this);
                writer.writeAttribute("class", "showinfo3 mvert0", null);
                writer.writeAttribute("style", "width: " + String.valueOf(width - (this.depth * 3)) +"em;", null);
            }
        }

        encodeHeader();
        
        if (!this.onlyStructure) {
            if (this.showRules && this.courseGroup.hasAnyCurricularRules()) {
                encodeCurricularRules();    
            }

            writer.endElement("table");
            if (this.depth > BASE_DEPTH) {
                writer.endElement("div");
            }
            
            if (!this.hideCourses && this.courseGroup.getSortedChildContextsWithCurricularCourses().size() > 0) {
                encodeChildCurricularCourses(width, courseGroupIndent);
                //encodeSumsFooter(sums);
            } else {
                //encodeEmptyCourseGroupInfo();
            }
        }
    }

    private void encodeHeader() throws IOException {
        writer.startElement("tr", this);

        encodeName(false);    
        
        if (this.toEdit) {
            if (this.onlyStructure) {
                if (this.toOrder) {
                    encodeOrderOptions();
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
            if (this.depth == BASE_DEPTH) {
                writer.startElement("strong", this);       
            } else if (this.depth > BASE_DEPTH) {
                writer.writeAttribute("style", "padding-left: " + String.valueOf((this.depth + 2)) + "em;", null);
            }
        } else {
            writer.writeAttribute("class", "bgcolor2", null);
            writer.startElement("th", this);
            writer.writeAttribute("class", "aleft", null);
            writer.writeAttribute("colspan", (this.toEdit) ? 3 : 5, null);
        }
        
        if (linkable) {
            writer.startElement("a", this);
            String action = "&action=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("action");
            String showRules = "&showRules=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("showRules");        
            String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");
            String toOrder = "&toOrder=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("toOrder");
            String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
            writer.writeAttribute("href", "courseGroupReport.faces?degreeCurricularPlanID="+ this.facesContext.getExternalContext().getRequestParameterMap()
                    .get("degreeCurricularPlanID") + "&courseGroupID=" + this.courseGroup.getIdInternal() + action + organizeBy + showRules + hideCourses + toOrder, null);
        }
        
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.courseGroup.getName()).append(" ");    
        } else {
            writer.append(this.courseGroup.getNameEn()).append(" ");
        }
        
        if (linkable) {
            writer.endElement("a");    
        }
        
        if (this.onlyStructure) {
            if (this.depth == BASE_DEPTH) {
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
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");
        encodeLink("orderCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.courseGroup.getIdInternal() + "&contextID=" + this.previousContext.getIdInternal() + "&pos=" + posToTest + "&toOrder=true" + organizeBy + hideCourses, label);
        if (!lastOption) {
            writer.append(" , ");
        }
    }

    private void encodeEditOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright", null);
        writer.append("(");
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        encodeLink("createCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&parentCourseGroupID=" + this.courseGroup.getIdInternal() + organizeBy + "&toOrder=false", "create.course.group");
        writer.append(" , ");
        encodeLink("associateCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&parentCourseGroupID=" + this.courseGroup.getIdInternal() + organizeBy + "&toOrder=false", "associate.course.group");
        writer.append(" , ");
        encodeLink("editCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.courseGroup.getIdInternal() + organizeBy + "&toOrder=false", "edit");
        writer.append(" , ");
        encodeLink("deleteCourseGroup.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&courseGroupID=" + this.courseGroup.getIdInternal() + "&contextID=" + this.previousContext.getIdInternal() + organizeBy + "&toOrder=false", "delete");
        writer.append(") ");
        writer.endElement("td");
    }

    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        if (!this.showRules) {
            encodeLink("createCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                    .get("degreeCurricularPlanID") + "&courseGroupID=" + this.courseGroup.getIdInternal() + organizeBy, "create.curricular.course");
            writer.append(" , ");
            encodeLink("associateCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                    .get("degreeCurricularPlanID") + "&courseGroupID=" + this.courseGroup.getIdInternal() + organizeBy, "associate.curricular.course");
        } else {
            String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");
            encodeLink("../curricularRules/createCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                    .get("degreeCurricularPlanID") + "&degreeModuleID=" + this.courseGroup.getIdInternal() + organizeBy + hideCourses, "setCurricularRule");
        }
        writer.endElement("th");
    }
    
    private void encodeChildCurricularCourses(int width, int courseGroupIndent) throws IOException {
        writer.startElement("div", this);
        writer.writeAttribute("class", (this.depth == BASE_DEPTH) ? "indent3" : "indent" + (courseGroupIndent + 3), null);
        writer.startElement("table", this);
        writer.writeAttribute("class", "showinfo3 mvert0", null);
        writer.writeAttribute("style", "width: " + (width - (this.depth * 3) - 3)  + "em;", null);

        for (Context context : this.courseGroup.getSortedChildContextsWithCurricularCourses()) {
            new UICurricularCourse(context.getChildDegreeModule(), context, this.toEdit, this.showRules, this.depth, this.tabs + "\t").encodeBegin(facesContext);
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
        writer.writeAttribute("class", "aright", null);
        //writer.writeAttribute("style", "width: 13em;", null);
        
        encodeSumsLoadFooterElement(sums, "contactLessonHoursAcronym", 0);
        encodeSumsLoadFooterElement(sums, "autonomousWorkAcronym", 1);
        encodeSumsLoadFooterElement(sums, "totalLoadAcronym", 2);
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "aright highlight2", null);
        //writer.writeAttribute("style", "width: 7em;", null);
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
