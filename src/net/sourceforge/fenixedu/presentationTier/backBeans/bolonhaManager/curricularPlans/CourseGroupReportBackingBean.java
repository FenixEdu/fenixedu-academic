package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class CourseGroupReportBackingBean extends FenixBackingBean {
    private final ResourceBundle enumerationResources = getResourceBundle("resources/EnumerationResources");
    
    private enum InfoToExport {
        CURRICULAR_STRUCTURE,
        STUDIES_PLAN;
    }
    
    private String name = null;
    private Integer courseGroupID;
    private Map<Context, String> contextPaths = new HashMap<Context, String>();

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
    
    public Map<Context, String> getContextPaths() {
        return contextPaths;
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
        getContextPaths().clear();
        collectChildDegreeModules(result, this.getCourseGroup(), this.getCourseGroup().getName());
        return result;
    }
    
    private void collectChildDegreeModules(final List<Context> result, CourseGroup courseGroup, String previousPath) throws FenixFilterException, FenixServiceException {
        for (final Context context : courseGroup.getSortedChildContextsWithCurricularCourses()) {
            result.add(context);
            getContextPaths().put(context, previousPath);
        }
        for (final Context context : courseGroup.getSortedChildContextsWithCourseGroups()) {
            collectChildDegreeModules(result, (CourseGroup) context.getChildDegreeModule(), previousPath + " > " + context.getChildDegreeModule().getName());
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
        headers.add("Nome Área Científica");
        headers.add("Sigla Área Científica");
        headers.add("Tipo");
        headers.add("T");
        headers.add("TP");
        headers.add("PL");
        headers.add("TC");
        headers.add("S");
        headers.add("E");
        headers.add("OT");
        headers.add("TA");
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
            CurricularPeriod curricularPeriod = contextWithCurricularCourse.getCurricularPeriod();
            String parentCourseGroupName = getContextPaths().get(contextWithCurricularCourse);
            
            fillCurricularCourse(spreadsheet, curricularCourse, curricularPeriod, parentCourseGroupName);
            if (curricularCourse.isAnual()) {
                fillCurricularCourse(spreadsheet, curricularCourse, curricularPeriod.getNext(), parentCourseGroupName);
            }
        }
    }

    private void fillCurricularCourse(final Spreadsheet spreadsheet, CurricularCourse curricularCourse, CurricularPeriod curricularPeriod, String parentCourseGroupName) {
        final Row row = spreadsheet.addRow();
        
        row.setCell(curricularCourse.getName());
        if (curricularCourse.isOptional()) {
            row.setCell(""); // scientific area unit name
            row.setCell(""); // scientific area unit acronym
            row.setCell(""); // regime
            row.setCell(""); // t
            row.setCell(""); // tp
            row.setCell(""); // pl
            row.setCell(""); // tc
            row.setCell(""); // s
            row.setCell(""); // e
            row.setCell(""); // ot
            row.setCell(""); // ta
            row.setCell(""); // ects
        } else {
            row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getName());
            row.setCell(""); //row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getAcronym());
            row.setCell(this.getFormatedMessage(enumerationResources, curricularCourse.getCompetenceCourse().getRegime().toString()));
        
            row.setCell(curricularCourse.getTheoreticalHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getProblemsHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getLaboratorialHours(curricularPeriod).toString());    
            row.setCell(curricularCourse.getFieldWorkHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getSeminaryHours().toString());    
            row.setCell(curricularCourse.getTrainingPeriodHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getTutorialOrientationHours(curricularPeriod).toString());    
            row.setCell(curricularCourse.getAutonomousWorkHours(curricularPeriod).toString());
            
            row.setCell(curricularCourse.getEctsCredits(curricularPeriod).toString());
        }
        row.setCell(""); // notes
        row.setCell(parentCourseGroupName);
        row.setCell(curricularPeriod.getParent().getOrder().toString());
        row.setCell(curricularPeriod.getOrder().toString());
    }

}
