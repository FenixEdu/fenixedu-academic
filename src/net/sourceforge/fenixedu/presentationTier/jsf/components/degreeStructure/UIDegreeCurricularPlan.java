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
        writer.writeAttribute("class", "style2", null);
        encodeHeader(year, semester);
        double totalCredits = 0.0;
        if (currentSemesterCurricularCourses.size() > 0) {
             totalCredits = encodeCurricularCourses(semester, currentSemesterCurricularCourses);
        } else {
            encodeEmptySemesterInfo();
        }            
        
        if (currentSemesterCurricularCourses.size() > 0) {
            encodeTotalCreditsFooter(totalCredits);
        }
        writer.endElement("table");
    }

    private void encodeHeader(int year, int semester) throws IOException {
        writer.startElement("tr", this);

        writer.startElement("th", this);
        writer.writeAttribute("colspan", 3, null);
        writer.writeAttribute("class", "aleft", null);
        writer.startElement("strong", this);
        writer.append(String.valueOf(year)).append("º ");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "year"));
        if (semester != 0) {
            writer.append("/").append(String.valueOf(semester)).append("º ");
            writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "semester"));
        }
        writer.endElement("strong");
        writer.endElement("th");
        
        if (this.toEdit) {
            encodeCourseGroupOptions();
        }
        
        writer.endElement("tr");
    }
    
    private void encodeCourseGroupOptions() throws IOException {
        writer.startElement("th", this);
        writer.writeAttribute("class", "aleft", null);
        writer.writeAttribute("width", "73px", null);
        encodeLink("createCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID"), "create.curricular.course");
        writer.endElement("th");
    }
    
    private double encodeCurricularCourses(int semester, List<ICurricularCourse> currentYearCurricularCourses) throws IOException {
        double totalCredits = 0.0;
        
        for (ICurricularCourse cc : currentYearCurricularCourses) {
            for (IContext ccContext : cc.getDegreeModuleContexts()) {
                if (ccContext.getCurricularSemester().getSemester().equals(semester)) {
                    totalCredits += cc.computeEctsCredits();
                    new UICurricularCourse(cc, this.toEdit, ccContext).encodeBegin(facesContext);   
                }
            }
        }
        
        return totalCredits;
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
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "no.associated.curricular.courses.to.year"));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
    }
    
    protected String getBundleValue(String bundleName, String bundleKey) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, facesContext.getViewRoot().getLocale());
        return bundle.getString(bundleKey);
    }
    
    protected void encodeLink(String href, String bundleKey) throws IOException {
        writer.startElement("a", this);
        writer.writeAttribute("href", href, null);
        writer.write(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", bundleKey));
        writer.endElement("a");
    }
    
    private void encodeTotalCreditsFooter(double totalCredits) throws IOException {
        writer.startElement("tr", this);
        
        writer.startElement("td", this);
        writer.writeAttribute("colspan", 2, null);
        writer.endElement("td");
        
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright highlight01", null);
        writer.writeAttribute("width", "73px", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "credits")).append(" ");
        writer.append(String.valueOf(totalCredits));
        writer.endElement("td");        
        if (this.toEdit) {
            writer.startElement("td", this);
            writer.writeAttribute("width", "73px", null);
            writer.append("&nbsp;");
            writer.endElement("td");
        }
        
        writer.endElement("tr");
    }
    
}
