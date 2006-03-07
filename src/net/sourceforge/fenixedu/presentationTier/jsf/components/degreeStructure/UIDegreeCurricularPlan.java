package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;

public class UIDegreeCurricularPlan extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    protected static final int ROOT_DEPTH = -1;

    private boolean toEdit;
    private boolean showRules;

    private FacesContext facesContext;
    private ResponseWriter writer;

    public UIDegreeCurricularPlan() {
        super();
        this.setRendererType(null);
    }

    public String getFamily() {
        return UIDegreeCurricularPlan.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }

        final DegreeCurricularPlan dcp = (DegreeCurricularPlan) this.getAttributes().get("dcp");
        if (!dcp.getCurricularStage().equals(CurricularStage.OLD)) {
            this.toEdit = (this.getBooleanAttribute("toEdit") != null) ? (Boolean) this.getBooleanAttribute("toEdit") : Boolean.FALSE;
            this.showRules = (this.getBooleanAttribute("showRules") != null) ? (Boolean) this.getBooleanAttribute("showRules") : Boolean.FALSE;
            final String organizeBy = (this.getAttributes().get("organizeBy") != null) ? (String) this.getAttributes().get("organizeBy") : "groups";
            final Boolean onlyStructure = (this.getBooleanAttribute("onlyStructure") != null) ? (Boolean) this.getBooleanAttribute("onlyStructure") : Boolean.FALSE;
            final Boolean toOrder = (this.getBooleanAttribute("toOrder") != null) ? (Boolean) this.getBooleanAttribute("toOrder") : Boolean.FALSE;
            final Boolean hideCourses = (this.getBooleanAttribute("hideCourses") != null) ? (Boolean) this.getBooleanAttribute("hideCourses") : Boolean.FALSE;
            
            if (incorrectUseOfComponent(organizeBy, onlyStructure, toOrder, hideCourses)) {
                throw new IOException("incorrect.component.usage");
            }
            
            if (organizeBy.equalsIgnoreCase("years")) {
                encodeByYears(facesContext, dcp);
            } else {
                StringBuilder dcpBuffer = new StringBuilder();
                dcpBuffer.append("[DCP ").append(dcp.getIdInternal()).append("] ").append(dcp.getName());
                //System.out.println(dcpBuffer);

                new UICourseGroup(dcp.getDegreeModule(), null, this.toEdit, this.showRules, ROOT_DEPTH, "", onlyStructure, toOrder, hideCourses).encodeBegin(facesContext);
            }
        }
    }

    private boolean incorrectUseOfComponent(String organizeBy, Boolean onlyStructure, Boolean toOrder, Boolean hideCourses) {
        return ((onlyStructure && (showRules || organizeBy.equals("years")))
                || (toOrder && (!onlyStructure || !toEdit)));
    }

    private Boolean getBooleanAttribute(String attributeName) {
        if (this.getAttributes().get(attributeName) instanceof Boolean) {
            return (Boolean) this.getAttributes().get(attributeName);
        } else {
            return Boolean.valueOf((String) this.getAttributes().get(attributeName));
        }
    }

    private void encodeByYears(FacesContext facesContext, DegreeCurricularPlan dcp) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        if (!((CourseGroup) dcp.getDegreeModule()).hasAnyCourseGroupContexts()) {
            encodeEmptyCurricularPlanInfo();
        } else {
            CurricularPeriod degreeStructure = dcp.getDegreeStructure();
            if (degreeStructure == null || !degreeStructure.hasAnyChilds()) {
                encodeEmptyDegreeStructureInfo();
            } else {
                for (CurricularPeriod child : degreeStructure.getSortedChilds()) {
                    encodePeriodTable(child);
                }
                encodeSubtitles();
            }
        }
    }

    private void encodeEmptyCurricularPlanInfo() throws IOException {
        encodeInfoTable("empty.curricularPlan");
    }

    private void encodeEmptyDegreeStructureInfo() throws IOException {
        encodeInfoTable("empty.degreeStructure");
    }
    
    private void encodeInfoTable(String info) throws IOException {
        writer.startElement("table", this);
        writer.startElement("tr", this);
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(this.getBundleValue("BolonhaManagerResources", info));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void encodeSubtitles() throws IOException {
        writer.startElement("br", this);
        writer.append("&nbsp;");
        writer.endElement("br");

        writer.startElement("ul", this);
        writer.writeAttribute("class", "nobullet", null);
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
    
    private void encodePeriodTable(CurricularPeriod curricularPeriod) throws IOException {
        if (curricularPeriod.hasAnyChilds()) {
            for (CurricularPeriod child : curricularPeriod.getSortedChilds()) {
                encodePeriodTable(child);    
            }
        } else {
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo3", null);
            writer.writeAttribute("style", "width: 70em;", null);

            encodeHeader(curricularPeriod);
            if (curricularPeriod.hasAnyContexts()) {
                List<Double> sums = encodeCurricularCourses(curricularPeriod.getContexts());
                //encodeSumsFooter(sums);
            } else {
                encodeEmptySemesterInfo();
            }
            
            writer.endElement("table");
        }
    }
    
    private void encodeHeader(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("tr", this);
        writer.writeAttribute("class", "bgcolor2", null);
        
        writer.startElement("th", this);
        writer.writeAttribute("class", "aleft", null);        
        writer.writeAttribute("colspan", (this.toEdit) ? 3 : 5, null);
        writer.append(curricularPeriod.getFullLabel());
        writer.endElement("th");

        if (this.toEdit) {
            encodeCourseGroupOptions(curricularPeriod);    
        }

        writer.endElement("tr");
    }
    
    private void encodeCourseGroupOptions(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);
        if (!this.showRules) {
            String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
            encodeLink("createCurricularCourse.faces?degreeCurricularPlanID="
                    + this.facesContext.getExternalContext().getRequestParameterMap().get(
                            "degreeCurricularPlanID") + "&curricularYearID=" + curricularPeriod.getParent().getOrder()
                    + "&curricularSemesterID=" + curricularPeriod.getOrder() + organizeBy, "create.curricular.course");
            writer.append(" , ");
            encodeLink("associateCurricularCourse.faces?degreeCurricularPlanID=" 
                    + this.facesContext.getExternalContext().getRequestParameterMap().get(
                            "degreeCurricularPlanID") + "&curricularYearID=" + curricularPeriod.getParent().getOrder()
                    + "&curricularSemesterID=" + curricularPeriod.getOrder() + organizeBy, "associate.curricular.course");
        }
        writer.endElement("th");
    }
    
    private List<Double> encodeCurricularCourses(List<Context> contexts) throws IOException {
        double sumContactLoad = 0.0;
        double sumAutonomousWork = 0.0;
        double sumTotalLoad = 0.0;
        double sumCredits = 0.0;
        for (Context context : contexts) {
            DegreeModule degreeModule = context.getDegreeModule();
            if (degreeModule instanceof CurricularCourse) {
                sumContactLoad += ((CurricularCourse)degreeModule).getContactLoad(context.getCurricularPeriod().getOrder());
                sumAutonomousWork += ((CurricularCourse)degreeModule).getAutonomousWorkHours(context.getCurricularPeriod().getOrder());
                sumTotalLoad += ((CurricularCourse)degreeModule).getTotalLoad(context.getCurricularPeriod().getOrder());
                sumCredits += ((CurricularCourse)degreeModule).getEctsCredits(context.getCurricularPeriod().getOrder());
                new UICurricularCourse((CurricularCourse) degreeModule, context, this.toEdit, this.showRules).encodeBegin(facesContext);
            }
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
        writer.writeAttribute("align", "aright", null);
        //writer.writeAttribute("style", "width: 13em;", null);
        
        encodeSumsLoadFooterElement(sums, "contactLessonHoursAcronym", 0);
        encodeSumsLoadFooterElement(sums, "autonomousWorkAcronym", 1);
        encodeSumsLoadFooterElement(sums, "totalLoadAcronym", 2);
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "aright highlight2", null);
        writer.writeAttribute("style", "width: 9em;", null);
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

    private void encodeEmptySemesterInfo() throws IOException {
        writer.startElement("tr", this);
        writer.startElement("td", this);
        if (this.toEdit) {
            writer.writeAttribute("colspan", 5, null);
        } else {
            writer.writeAttribute("colspan", 3, null);
        }
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(this.getBundleValue("BolonhaManagerResources", "no.associated.curricular.courses.to.year"));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
    }

    protected String getBundleValue(String bundleName, String bundleKey) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/" + bundleName, facesContext.getViewRoot().getLocale());
        return bundle.getString(bundleKey);
    }

    protected void encodeLink(String href, String bundleKey) throws IOException {
        writer.startElement("a", this);
        writer.writeAttribute("href", href, null);
        writer.write(this.getBundleValue("BolonhaManagerResources", bundleKey));
        writer.endElement("a");
    }

}
