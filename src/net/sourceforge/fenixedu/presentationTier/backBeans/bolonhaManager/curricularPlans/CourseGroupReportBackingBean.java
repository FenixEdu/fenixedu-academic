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
    private InfoToExport infoToExport;
    private boolean rootWasClicked;
    private String name = null;
    private Integer courseGroupID;
    private Map<Context, String> contextPaths = new HashMap<Context, String>();

    private enum InfoToExport {
        CURRICULAR_STRUCTURE,
        STUDIES_PLAN;
    }
    
    public CourseGroupReportBackingBean() throws FenixFilterException, FenixServiceException {
        super();
        rootWasClicked = this.getDegreeCurricularPlan().getRoot().equals(this.getCourseGroup());
    }
    
    public Boolean getRootWasClicked() {
        return rootWasClicked;
    }
    
    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        return rootDomainObject.readDegreeCurricularPlanByOID(getDegreeCurricularPlanID());
    }
    
    public Integer getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldIntegerParameter("courseGroupID");
    }

    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }
    
    public CourseGroup getCourseGroup() throws FenixFilterException, FenixServiceException {
        return (CourseGroup) rootDomainObject.readDegreeModuleByOID(getCourseGroupID());
    }

    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getCourseGroupID() != null) ? this.getCourseGroup().getName() : name;    
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void exportCourseGroupCurricularStructureToExcel() throws FenixFilterException, FenixServiceException {
        infoToExport = InfoToExport.CURRICULAR_STRUCTURE;
        exportToExcel();
    }
    
    public void exportCourseGroupStudiesPlanToExcel() throws FenixFilterException, FenixServiceException {
        infoToExport = InfoToExport.STUDIES_PLAN; 
        exportToExcel();
    }
    
    public Map<Context, String> getContextPaths() {
        return contextPaths;
    }
    
    public void exportToExcel() throws FenixFilterException, FenixServiceException {
        String filename = this.getDegreeCurricularPlan().getName().replace(" ","_") + "-"; 
        filename += (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) ? "Estrutura_Curricular-" : "Plano_de_Estudos";
        if (!rootWasClicked) {
            filename += "-" + this.getCourseGroup().getName().replace(" ", "_");
        }
        filename += "-" + getFileName(Calendar.getInstance().getTime());

        try {
            exportToXls(filename);
        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private List<Context> contextsWithCurricularCoursesToList(CourseGroup startingPoint) throws FenixFilterException, FenixServiceException {
        List<Context> result = new ArrayList<Context>();
        getContextPaths().clear();
        collectChildDegreeModules(result, startingPoint, startingPoint.getName());
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
    
    private void exportToXls(String filename) throws IOException, FenixFilterException, FenixServiceException {
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
            reportInfo(spreadsheet);
        } else {
            spreadSheetName = "Plano de Estudos do Grupo";
            headers = getStudiesPlanHeaders();
            spreadsheet = new Spreadsheet(spreadSheetName, headers);
            reportInfo(spreadsheet);
        }
        
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        this.getResponse().flushBuffer();
    }

    private void reportInfo(Spreadsheet spreadsheet) throws FenixFilterException, FenixServiceException {
        List<Context> contextsWithCurricularCourses = null;
        if (rootWasClicked) {
            // first level contexts are to be reported
            for (Context context : this.getCourseGroup().getSortedChildContextsWithCourseGroups()) {
                CourseGroup toBeReported = (CourseGroup) context.getChildDegreeModule();
                contextsWithCurricularCourses = contextsWithCurricularCoursesToList(toBeReported);
                
                if (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) {
                    fillCurricularStructure(toBeReported.getName(), contextsWithCurricularCourses, spreadsheet);
                    spreadsheet.addRow();
                } else {
                    fillStudiesPlan(contextsWithCurricularCourses, spreadsheet);
                    spreadsheet.addRow();
                }
            }
        } else {
            contextsWithCurricularCourses = contextsWithCurricularCoursesToList(this.getCourseGroup());
            
            if (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) {
                fillCurricularStructure(null, contextsWithCurricularCourses, spreadsheet);
            } else {
                fillStudiesPlan(contextsWithCurricularCourses, spreadsheet);            
            }
        }
    }

    private List<Object> getCurricularStructureHeader() {
        final List<Object> headers = new ArrayList<Object>();
        if (rootWasClicked) {
            headers.add("Grupo");
        }
        headers.add("Área Científica");
        headers.add("Sigla");
        headers.add("Créditos Obrigatórios");
        headers.add("Créditos Optativos");
        return headers;
    }
    
    private void fillCurricularStructure(String courseGroupBeingReported, List<Context> contextsWithCurricularCourses, final Spreadsheet spreadsheet) throws FenixFilterException, FenixServiceException {
        Set<Unit> scientificAreaUnits = new HashSet<Unit>();
        
        for (final Context contextWithCurricularCourse : contextsWithCurricularCourses) {
            CurricularCourse curricularCourse = (CurricularCourse) contextWithCurricularCourse.getChildDegreeModule();
            
            if (!curricularCourse.isOptional()
                    && !scientificAreaUnits.contains(curricularCourse.getCompetenceCourse().getScientificAreaUnit())) {
                final Row row = spreadsheet.addRow();
                
                if (rootWasClicked && courseGroupBeingReported != null) {
                    row.setCell(courseGroupBeingReported);
                }
                row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getName());
                row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getAcronym());
                row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getScientificAreaUnitEctsCredits(contextsWithCurricularCourses).toString());
                
                scientificAreaUnits.add(curricularCourse.getCompetenceCourse().getScientificAreaUnit());
            }
        }
    }

    private List<Object> getStudiesPlanHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Unidade Curricular");
        headers.add("Grupo");
        headers.add("Área Científica");
        headers.add("Sigla");
        headers.add("Tipo");
        headers.add("Ano");
        headers.add("Semestre");
        headers.add("Créditos");
        headers.add("T");
        headers.add("TP");
        headers.add("PL");
        headers.add("TC");
        headers.add("S");
        headers.add("E");
        headers.add("OT");
        headers.add("TA");
        headers.add("Observações");
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
        row.setCell(parentCourseGroupName);
        if (curricularCourse.isOptional()) {
            row.setCell(""); // scientific area unit name
            row.setCell(""); // scientific area unit acronym
            
            row.setCell(""); // regime
            row.setCell(curricularPeriod.getParent().getOrder().toString());
            row.setCell(curricularPeriod.getOrder().toString());

            row.setCell(""); // ects
            row.setCell(""); // t
            row.setCell(""); // tp
            row.setCell(""); // pl
            row.setCell(""); // tc
            row.setCell(""); // s
            row.setCell(""); // e
            row.setCell(""); // ot
            row.setCell(""); // ta
        } else {
            row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getName());
            row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getAcronym());
            
            row.setCell(this.getFormatedMessage(enumerationResources, curricularCourse.getCompetenceCourse().getRegime().toString()));
            row.setCell(curricularPeriod.getParent().getChildOrder() == null ? "" : curricularPeriod.getParent().getOrder().toString());
            row.setCell(curricularPeriod.getOrder().toString());
        
            row.setCell(curricularCourse.getEctsCredits(curricularPeriod).toString());
            row.setCell(curricularCourse.getTheoreticalHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getProblemsHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getLaboratorialHours(curricularPeriod).toString());    
            row.setCell(curricularCourse.getFieldWorkHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getSeminaryHours().toString());    
            row.setCell(curricularCourse.getTrainingPeriodHours(curricularPeriod).toString());
            row.setCell(curricularCourse.getTutorialOrientationHours(curricularPeriod).toString());    
            row.setCell(curricularCourse.getAutonomousWorkHours(curricularPeriod).toString());
        }
        row.setCell(""); // notes
    }

}
