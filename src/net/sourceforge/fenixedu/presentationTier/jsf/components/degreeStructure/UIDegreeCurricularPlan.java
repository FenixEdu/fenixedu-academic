package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.IContext;

public class UIDegreeCurricularPlan extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    protected static final int BASE_DEPTH = -1;

    private Boolean toEdit;
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

        final IDegreeCurricularPlan dcp = (IDegreeCurricularPlan) this.getAttributes().get("dcp");
        if (!dcp.getCurricularStage().equals(CurricularStage.OLD)) {
            final Boolean onlyStructure = this.getBooleanAttribute("onlyStructure");
            this.toEdit = this.getBooleanAttribute("toEdit");
            final String organizeBy = (String) this.getAttributes().get("organizeBy");
            
            if (organizeBy != null && organizeBy.equalsIgnoreCase("years")) {
                encodeByYears(facesContext,dcp);
            } else {
                StringBuffer dcpBuffer = new StringBuffer();
                dcpBuffer.append("[DCP ").append(dcp.getIdInternal()).append("] ").append(dcp.getName());
                System.out.println(dcpBuffer);
                
                new UICourseGroup(dcp.getDegreeModule(), onlyStructure, this.toEdit, BASE_DEPTH, "").encodeBegin(facesContext);
            }
        }
    }

    private Boolean getBooleanAttribute(String attributeName) {
        if (this.getAttributes().get(attributeName) instanceof Boolean) {
            return (Boolean) this.getAttributes().get(attributeName);
        } else {
            return Boolean.valueOf((String) this.getAttributes().get(attributeName));
        }
    }

    private void encodeByYears(FacesContext facesContext, IDegreeCurricularPlan dcp) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        
        int maxYears = 1;
        if (dcp.getDegree().getBolonhaDegreeType().equals(BolonhaDegreeType.DEGREE)) {
            maxYears = 3;
        } else if (dcp.getDegree().getBolonhaDegreeType().equals(BolonhaDegreeType.MASTER_DEGREE)) {
            maxYears = 2;
        } else if (dcp.getDegree().getBolonhaDegreeType().equals(BolonhaDegreeType.INTEGRATED_MASTER_DEGREE)) {
            maxYears = 5;
        }
        
        List<ICurricularCourse> dcpCurricularCourses = dcp.getDcpCurricularCourses();
        for (int year = 1; year <= maxYears; year++) {
            List<ICurricularCourse> anualCurricularCourses = collectCurrentYearCurricularCoursesBySemester(year, 0, dcpCurricularCourses);
            if (!anualCurricularCourses.isEmpty()) {
                encodeSemesterTable(year, 0, anualCurricularCourses);    
            }
            encodeSemesterTable(year, 1, collectCurrentYearCurricularCoursesBySemester(year, 1, dcpCurricularCourses));
            encodeSemesterTable(year, 2, collectCurrentYearCurricularCoursesBySemester(year, 2, dcpCurricularCourses));

            
        }
        
    }

    private List<ICurricularCourse> collectCurrentYearCurricularCoursesBySemester(int year, int semester, List<ICurricularCourse> dcpCurricularCourses) {
        List<ICurricularCourse> currentYearCurricularCoursesBySemester = new ArrayList<ICurricularCourse>();
        
        for (ICurricularCourse cc : dcpCurricularCourses) {
            for (IContext ccContext : cc.getDegreeModuleContexts()) {
                if (ccContext.getCurricularSemester().getCurricularYear().getYear().equals(year)) {
                    if (ccContext.getCurricularSemester().getSemester().equals(semester)) {
                        currentYearCurricularCoursesBySemester.add(cc);    
                    }
                }
            }
        }
        return currentYearCurricularCoursesBySemester;
    }

    private void encodeSemesterTable(int year, int semester, List<ICurricularCourse> currentSemesterCurricularCourses) throws IOException {
        writer.startElement("table", this);
        writer.writeAttribute("border", 1, null);
        encodeHeader(year, semester);
        if (currentSemesterCurricularCourses.size() > 0) {
            encodeCurricularCourses(semester, currentSemesterCurricularCourses);
        } else {
            encodeEmptySemesterInfo();
        }            
        
        if (this.toEdit) {
            encodeCourseGroupOptions();
        }

        if (currentSemesterCurricularCourses.size() > 0) {
            encodeTotalCreditsFooter();
        }
        writer.endElement("table");
    }

    private void encodeHeader(int year, int semester) throws IOException {
        writer.writeAttribute("class", "style2 margintop", null);    
        writer.startElement("tr", this);
        writer.startElement("th", this);
        if (this.toEdit) {
            writer.writeAttribute("colspan", 4, null);    
        } else {
            writer.writeAttribute("colspan", 3, null);
        }
        writer.startElement("strong", this);
        writer.append(String.valueOf(year)).append("º ");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "year"));
        if (semester != 0) {
            writer.append("/").append(String.valueOf(semester)).append("º ");
            writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "semester"));
        }
        writer.endElement("strong");
        writer.endElement("th");
        writer.endElement("tr");
    }
    
    private void encodeCurricularCourses(int semester, List<ICurricularCourse> currentYearCurricularCourses) throws IOException {
        for (ICurricularCourse cc : currentYearCurricularCourses) {
            for (IContext ccContext : cc.getDegreeModuleContexts()) {
                if (ccContext.getCurricularSemester().getSemester().equals(semester)) {
                    new UICurricularCourse(cc, this.toEdit, ccContext).encodeBegin(facesContext);   
                }
            }
        }
    }
    
    private void encodeEmptySemesterInfo() throws IOException {
        writer.startElement("tr", this);
        writer.startElement("td", this);
        if (this.toEdit) {
            writer.writeAttribute("colspan", 4, null);    
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
    
    protected String getBundleValue(String bundleName, String bundleKey) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, facesContext.getViewRoot().getLocale());
        return bundle.getString(bundleKey);
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
                .get("degreeCurricularPlanID"), "create.curricular.course");
        writer.endElement("li");
        writer.startElement("li", this);
        encodeLink("associateCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID"), "associate.curricular.course");
        writer.endElement("li");
        
        writer.endElement("ul");
        writer.endElement("td");
        
        writer.endElement("tr");
    }
    
    protected void encodeLink(String href, String bundleKey) throws IOException {
        writer.startElement("a", this);
        writer.writeAttribute("href", href, null);
        writer.write(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", bundleKey));
        writer.endElement("a");
    }
    
    private void encodeTotalCreditsFooter() throws IOException {
        writer.startElement("tr", this);

        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "credits")).append(": ");
        writer.endElement("td");
        writer.startElement("td", this);
        writer.append("&nbsp;");
        writer.endElement("td");
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        writer.append("n há milagres");
        writer.endElement("td");        
        if (this.toEdit) {
            writer.startElement("td", this);
            writer.append("&nbsp;");
            writer.endElement("td");
        }
        
        writer.endElement("tr");
    }
    
}
