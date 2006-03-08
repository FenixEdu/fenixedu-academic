/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class CourseGroupReportBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle enumerationResources = getResourceBundle("resources/EnumerationResources");
    
    private enum InfoToExport {
        CURRICULAR_STRUCTURE,
        STUDIES_PLAN;
    }
    
    private String name = null;
    private Integer courseGroupID;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        return (DegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, getDegreeCurricularPlanID());
    }
    
    public Integer getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldIntegerParameter("courseGroupID");
    }

    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }
    
    public CourseGroup getCourseGroup() throws FenixFilterException, FenixServiceException {
        return (CourseGroup) readDomainObject(CourseGroup.class, this.getCourseGroupID());
    }

    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getCourseGroupID() != null) ? this.getCourseGroup().getName() : name;    
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void exportCourseGroupCurricularStructureToExcel() throws FenixFilterException, FenixServiceException {
        exportToExcel(InfoToExport.CURRICULAR_STRUCTURE);
    }
    
    public void exportCourseGroupStudiesPlanToExcel() throws FenixFilterException, FenixServiceException {
        exportToExcel(InfoToExport.STUDIES_PLAN);
    }
    
    public void exportToExcel(InfoToExport infoToExport) throws FenixFilterException, FenixServiceException {
        List<Context> contextsWithCurricularCourses = contextsWithCurricularCoursesToList();
        
        String filename = this.getDegreeCurricularPlan().getName().replace(" ","_") + "-"; 
        filename += (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) ? "Estrutura_Curricular-" : "Plano_de_Estudos-";
        filename += this.getCourseGroup().getName().replace(" ", "_") + "-" + getFileName(Calendar.getInstance().getTime());

        try {
            exportToXls(infoToExport, contextsWithCurricularCourses, filename);
        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private List<Context> contextsWithCurricularCoursesToList() throws FenixFilterException, FenixServiceException {
        List<Context> result = new ArrayList<Context>();
        collectChildDegreeModules(result, this.getCourseGroup());
        
        return result;
    }
    
    private void collectChildDegreeModules(final List<Context> result, CourseGroup courseGroup) throws FenixFilterException, FenixServiceException {
        result.addAll(courseGroup.getSortedContextsWithCurricularCourses());
        
        for (final Context context : courseGroup.getSortedContextsWithCourseGroups()) {
            collectChildDegreeModules(result, (CourseGroup) context.getChildDegreeModule());
        }
    }

    private String getFileName(Date date) throws FenixFilterException, FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (day + "_" + month + "_" + year + "-" + hour + ":" + minutes);
    }
    
    private void exportToXls(InfoToExport infoToExport, List<Context> contextsWithCurricularCourses, String filename) throws IOException, FenixFilterException, FenixServiceException {
        this.getResponse().setContentType("application/vnd.ms-excel");
        this.getResponse().setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        ServletOutputStream outputStream = this.getResponse().getOutputStream();
        
        List<Object> headers = null;
        String spreadSheetName = null;
        Spreadsheet spreadsheet = null;
        if (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) {
            spreadSheetName = "Estrutura Curricular do Grupo";
            headers = getCurricularStructureHeader();
            spreadsheet = new Spreadsheet(spreadSheetName, headers);
            fillCurricularStructure(contextsWithCurricularCourses, spreadsheet);
        } else {
            spreadSheetName = "Plano de Estudos do Grupo";
            headers = getStudiesPlanHeaders();
            spreadsheet = new Spreadsheet(spreadSheetName, headers);
            fillStudiesPlan(contextsWithCurricularCourses, spreadsheet);
        }
        
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        this.getResponse().flushBuffer();
    }

    private List<Object> getCurricularStructureHeader() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Área Científica");
        headers.add("Sigla");
        headers.add("Créditos Obrigatórios");
        headers.add("Créditos Optativos");
        return headers;
    }
    
    private void fillCurricularStructure(List<Context> contextsWithCurricularCourses, final Spreadsheet spreadsheet) throws FenixFilterException, FenixServiceException {
        Set<Unit> scientificAreaUnits = new HashSet<Unit>();
        
        for (final Context contextWithCurricularCourse : contextsWithCurricularCourses) {
            CurricularCourse curricularCourse = (CurricularCourse) contextWithCurricularCourse.getChildDegreeModule();
            
            if (!scientificAreaUnits.contains(curricularCourse.getCompetenceCourse().getScientificAreaUnit())) {
                final Row row = spreadsheet.addRow();
                
                row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getName());
                row.setCell("");//curricularCourse.getCompetenceCourse().getScientificAreaUnit().getAcronym());
                row.setCell(Double.valueOf(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getScientificAreaUnitEctsCredits()).toString());
                
                scientificAreaUnits.add(curricularCourse.getCompetenceCourse().getScientificAreaUnit());
            }
        }
    }

    private List<Object> getStudiesPlanHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Unidades Curriculares");
        headers.add("Área Científica");
        headers.add("Tipo");
        headers.add("Horas Total");
        headers.add("Horas Contacto");
        headers.add("Créditos");
        headers.add("Observações");
        headers.add("Grupo");
        headers.add("Ano");
        headers.add("Semestre");
        return headers;
    }
    
    private void fillStudiesPlan(List<Context> contextsWithCurricularCourses, final Spreadsheet spreadsheet) throws FenixFilterException, FenixServiceException {
        for (final Context contextWithCurricularCourse : contextsWithCurricularCourses) {
            CurricularCourse curricularCourse = (CurricularCourse) contextWithCurricularCourse.getChildDegreeModule();
            
            final Row row = spreadsheet.addRow();
            
            row.setCell(curricularCourse.getName());
            row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getName());
            row.setCell(this.getFormatedMessage(enumerationResources, curricularCourse.getCompetenceCourse().getRegime().toString()));
            
            StringBuilder loads = new StringBuilder();
            int order = contextWithCurricularCourse.getCurricularPeriod().getOrder();
            
            if (curricularCourse.getTheoreticalHours(order) != 0.0) {
                loads.append(" T=").append(curricularCourse.getTheoreticalHours(order));    
            }
            if (curricularCourse.getProblemsHours(order) != 0.0) {
                loads.append(" TP=").append(curricularCourse.getProblemsHours(order));
            }
            if (curricularCourse.getLaboratorialHours(order) != 0.0) {
                loads.append(" PL=").append(curricularCourse.getLaboratorialHours(order));    
            }
            if (curricularCourse.getSeminaryHours(order) != 0.0) {
                loads.append(" S=").append(curricularCourse.getSeminaryHours());    
            }
            if (curricularCourse.getFieldWorkHours(order) != 0.0) {
                loads.append(" TC=").append(curricularCourse.getFieldWorkHours(order));    
            }
            if (curricularCourse.getTrainingPeriodHours(order) != 0.0) {
                loads.append(" E=").append(curricularCourse.getTrainingPeriodHours(order));    
            }
            if (curricularCourse.getTutorialOrientationHours(order) != 0.0) {
                loads.append(" OT=").append(curricularCourse.getTutorialOrientationHours(order));    
            }
            loads.append(" TA=").append(curricularCourse.getAutonomousWorkHours(order)).append(" (h/semestre)");
            loads.append(" C=").append(curricularCourse.getContactLoad(order));
            loads.append(" Total=").append(curricularCourse.getTotalLoad(order));
            
            row.setCell(curricularCourse.getEctsCredits(contextWithCurricularCourse.getCurricularPeriod().getOrder()).toString());
            row.setCell("");
            row.setCell(contextWithCurricularCourse.getParentCourseGroup().getName());
            row.setCell(contextWithCurricularCourse.getCurricularPeriod().getParent().getOrder().toString());
            row.setCell(contextWithCurricularCourse.getCurricularPeriod().getOrder().toString());
        }
    }

}
