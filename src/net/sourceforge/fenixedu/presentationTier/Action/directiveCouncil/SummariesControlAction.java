/*
 * Created on Jan 16, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.SummariesControlElementDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class SummariesControlAction extends FenixDispatchAction {

    private BigDecimal EMPTY = BigDecimal.valueOf(0.00);
    
    private BigDecimal ONE = BigDecimal.valueOf(1.00);
    
    private BigDecimal HUNDRED = BigDecimal.valueOf(100.00);
    
    public ActionForward prepareSummariesControl(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        readAndSaveAllDepartments(request);
        return mapping.findForward("success");
    }

    public ActionForward listExecutionPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        readAndSaveAllExecutionPeriods(request);

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        String departmentID = (String) dynaActionForm.get("department");
        String executionPeriodID = (String) dynaActionForm.get("executionPeriod");

        if (departmentID != null && !departmentID.equals("") && executionPeriodID != null
                && !executionPeriodID.equals("")) {

            getListing(request, departmentID, executionPeriodID);
            saveDepartmentAndExecutionPeriod(request, departmentID, executionPeriodID);

        } else if (departmentID == null || departmentID.equals("")) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("error.no.deparment"));
            saveMessages(request, actionMessages);
        } else if (executionPeriodID == null || executionPeriodID.equals("")) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("error.no.execution.period"));
            saveMessages(request, actionMessages);
        }

        return prepareSummariesControl(mapping, actionForm, request, response);
    }

    public ActionForward listSummariesControl(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        String departmentID = (String) dynaActionForm.get("department");
        String executionPeriodID = (String) dynaActionForm.get("executionPeriod");
        boolean runProcess = true;

        if (departmentID == null || departmentID.equals("")) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("error.no.deparment"));
            saveMessages(request, actionMessages);
            dynaActionForm.set("executionPeriod", "");
            runProcess = false;
        }
        if (executionPeriodID == null || executionPeriodID.equals("")) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("error.no.execution.period"));
            saveMessages(request, actionMessages);
            runProcess = false;
        }

        if (runProcess) {
            getListing(request, departmentID, executionPeriodID);
            saveDepartmentAndExecutionPeriod(request, departmentID, executionPeriodID);
        }

        readAndSaveAllDepartments(request);
        readAndSaveAllExecutionPeriods(request);

        return mapping.findForward("success");
    }

    private void saveDepartmentAndExecutionPeriod(HttpServletRequest request, String departmentID, String executionPeriodID) {
        request.setAttribute("department", departmentID);
        request.setAttribute("executionPeriod", executionPeriodID);
    }

    private List<SummariesControlElementDTO> getListing(HttpServletRequest request, String departmentID,
            String executionPeriodID) throws FenixFilterException, FenixServiceException {

        final Department department = rootDomainObject.readDepartmentByOID(Integer.valueOf(departmentID));       
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(Integer.valueOf(executionPeriodID));
       
        List<Teacher> allDepartmentTeachers = (department != null && executionPeriod != null) ? department
                .getAllTeachers(executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay())
                : new ArrayList<Teacher>();

        List<SummariesControlElementDTO> allListElements = new ArrayList<SummariesControlElementDTO>();
             
        for (Teacher teacher : allDepartmentTeachers) {
                       
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
            for (Professorship professorship : teacher.getProfessorships()) {       	
        	
                BigDecimal lessonHours = EMPTY, summaryHours = EMPTY, courseDifference = EMPTY;
                BigDecimal shiftDifference = EMPTY, courseSummaryHours = EMPTY;

                if (professorship.belongsToExecutionPeriod(executionPeriod) && !professorship.getExecutionCourse().isMasterDegreeOnly()) {

                    for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {

                        DegreeTeachingService degreeTeachingService = readDegreeTeachingService(teacherService, shift, professorship);                                                
                        if (degreeTeachingService != null) {
                            
                            // GET LESSON HOURS
                            lessonHours = readLessonHours(degreeTeachingService.getPercentage(), shift, lessonHours);
                            
                            // GET SHIFT SUMMARIES HOURS
                            summaryHours = readSummaryHours(professorship, shift, summaryHours);                            
                        }

                        // GET COURSE SUMMARY HOURS
                        courseSummaryHours = readSummaryHours(professorship, shift, courseSummaryHours);
                    }                  
                    
                    summaryHours = summaryHours.setScale(2, RoundingMode.HALF_UP);
                    lessonHours = lessonHours.setScale(2, RoundingMode.HALF_UP);
                    courseSummaryHours = courseSummaryHours.setScale(2, RoundingMode.HALF_UP);

                    shiftDifference = getDifference(lessonHours, summaryHours);
                    courseDifference = getDifference(lessonHours, courseSummaryHours);

                    Category category = teacher.getCategory();
                    String categoryName = (category != null) ? category.getCode() : "";
                    String siglas = getSiglas(professorship);

                    SummariesControlElementDTO listElementDTO = new SummariesControlElementDTO(teacher
                            .getPerson().getNome(), professorship.getExecutionCourse().getNome(),
                            teacher.getTeacherNumber(), categoryName, lessonHours, summaryHours,
                            courseSummaryHours, shiftDifference, courseDifference, siglas);

                    allListElements.add(listElementDTO);
                }
            }
        }

        Collections.sort(allListElements, new BeanComparator("teacherNumber"));
        request.setAttribute("listElements", allListElements);

        return allListElements;
    }

    private DegreeTeachingService readDegreeTeachingService(TeacherService teacherService, Shift shift, Professorship professorship) {
        DegreeTeachingService degreeTeachingService = null;
        if (teacherService != null) {
            degreeTeachingService = teacherService.getDegreeTeachingServiceByShiftAndProfessorship(shift, professorship);
        }
        return degreeTeachingService;
    }

    private BigDecimal readLessonHours(Double percentage, Shift shift, BigDecimal lessonHours) {                                     
        BigDecimal shiftLessonHoursSum = EMPTY;
	for (Lesson lesson : shift.getAssociatedLessons()) {
	    shiftLessonHoursSum = shiftLessonHoursSum.add(BigDecimal.valueOf(lesson.getAllLessonDates().size() * lesson.hours()));
	}
	return lessonHours.add(BigDecimal.valueOf((percentage / 100)).multiply(shiftLessonHoursSum));                
    }

    private BigDecimal readSummaryHours(Professorship professorship, Shift shift, BigDecimal summaryHours) {	
	for (Summary summary : shift.getAssociatedSummaries()) {
	    if(summary.getProfessorship() != null && summary.getProfessorship().equals(professorship)) {
                BigDecimal lessonHours = EMPTY;
                if(summary.getLesson() != null) {
                    lessonHours = BigDecimal.valueOf(summary.getLesson().hours());		
                } else if(!shift.getAssociatedLessons().isEmpty()) {	
                    lessonHours = BigDecimal.valueOf(shift.getAssociatedLessons().get(0).hours());		
                }	
                summaryHours = summaryHours.add(lessonHours);
	    }
	}		
	return summaryHours;
    }
    
    private BigDecimal getDifference(BigDecimal lessonHours, BigDecimal summaryHours) {		 
        Double difference;
        difference = (1 - ((lessonHours.doubleValue() - summaryHours.doubleValue()) / lessonHours.doubleValue())) * 100;
        if (difference.isNaN() || difference.isInfinite()) {
            difference = 0.0;
        }        
        return  BigDecimal.valueOf(difference).setScale(2, RoundingMode.HALF_UP);
    }
      
    private String getSiglas(Professorship professorship) {
        ExecutionCourse executionCourse = professorship.getExecutionCourse();
        int numberOfCurricularCourse = executionCourse.getAssociatedCurricularCourses().size();

        List<String> siglas = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();

        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            String sigla = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
            if (!siglas.contains(sigla)) {
                if (numberOfCurricularCourse < executionCourse.getAssociatedCurricularCourses().size()) {
                    buffer.append(",");
                }
                buffer.append(sigla);
                siglas.add(sigla);
            }
            numberOfCurricularCourse--;
        }
        return buffer.toString();
    }
    
    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        String departmentID = (String) dynaActionForm.get("department");
        String executionPeriodID = (String) dynaActionForm.get("executionPeriod");

        List<SummariesControlElementDTO> list = getListing(request, departmentID, executionPeriodID);
        try {
            String filename = "ControloSumarios:" + getFileName(Calendar.getInstance().getTime());
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            exportToXls(list, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    public ActionForward exportToCSV(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        String departmentID = (String) dynaActionForm.get("department");
        String executionPeriodID = (String) dynaActionForm.get("executionPeriod");

        List<SummariesControlElementDTO> list = getListing(request, departmentID, executionPeriodID);

        try {
            String filename = "ControloSumarios:" + getFileName(Calendar.getInstance().getTime());
            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".csv");

            ServletOutputStream writer = response.getOutputStream();
            exportToCSV(list, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    private void exportToXls(final List<SummariesControlElementDTO> allListElements,
            OutputStream outputStream) throws IOException {
        final List<Object> headers = getHeaders();
        final Spreadsheet spreadsheet = new Spreadsheet("Controlo de Sumários", headers);
        fillSpreadSheet(allListElements, spreadsheet);
        spreadsheet.exportToXLSSheet(outputStream);
    }

    private void exportToCSV(final List<SummariesControlElementDTO> allListElements,
            OutputStream outputStream) throws IOException {
        final Spreadsheet spreadsheet = new Spreadsheet("Controlo de Sumários");
        fillSpreadSheet(allListElements, spreadsheet);
        spreadsheet.exportToCSV(outputStream, ";");
    }

    private void fillSpreadSheet(final List<SummariesControlElementDTO> allListElements,
            final Spreadsheet spreadsheet) {
        for (final SummariesControlElementDTO summariesControlElementDTO : allListElements) {
            final Row row = spreadsheet.addRow();
            row.setCell(summariesControlElementDTO.getTeacherName());
            row.setCell(summariesControlElementDTO.getTeacherNumber().toString());
            row.setCell(summariesControlElementDTO.getCategoryName());
            row.setCell(summariesControlElementDTO.getExecutionCourseName());
            row.setCell(summariesControlElementDTO.getSiglas());
            row.setCell(summariesControlElementDTO.getLessonHours().toString());
            row.setCell(summariesControlElementDTO.getSummaryHours().toString());
            row.setCell(summariesControlElementDTO.getShiftDifference().toString());
            row.setCell(summariesControlElementDTO.getCourseSummaryHours().toString());
            row.setCell(summariesControlElementDTO.getCourseDifference().toString());
        }
    }

    private List<Object> getHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Nome");
        headers.add("Número");
        headers.add("Categoria");
        headers.add("Disciplina");
        headers.add("Licenciatura(s)");
        headers.add("Horas Declaradas");
        headers.add("Sumários nos Turnos");
        headers.add("Percentagem nos Turnos");
        headers.add("Sumários na Disciplina");
        headers.add("Percentagem na Disciplina");
        return headers;
    }   
    
    private List<LabelValueBean> getNotClosedExecutionPeriods(
            List<InfoExecutionPeriod> allExecutionPeriods) {
        List<LabelValueBean> executionPeriods = new ArrayList<LabelValueBean>();
        for (InfoExecutionPeriod infoExecutionPeriod : allExecutionPeriods) {
            LabelValueBean labelValueBean = new LabelValueBean();
            labelValueBean.setLabel(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - " + infoExecutionPeriod.getSemester() + "º Semestre");
            labelValueBean.setValue(infoExecutionPeriod.getIdInternal().toString());
            executionPeriods.add(labelValueBean);
        }
        Collections.sort(executionPeriods, new BeanComparator("label"));
        return executionPeriods;
    }

    private List<LabelValueBean> getAllDepartments(Collection<Department> allDepartments) {
        List<LabelValueBean> departments = new ArrayList<LabelValueBean>();
        for (Department department : allDepartments) {
            LabelValueBean labelValueBean = new LabelValueBean();
            labelValueBean.setValue(department.getIdInternal().toString());
            labelValueBean.setLabel(department.getRealName());
            departments.add(labelValueBean);
        }
        Collections.sort(departments, new BeanComparator("label"));
        return departments;
    }

    private void readAndSaveAllDepartments(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        Collection<Department> allDepartments = rootDomainObject.getDepartments();
        List<LabelValueBean> departments = getAllDepartments(allDepartments);
        request.setAttribute("departments", departments);
    }

    private void readAndSaveAllExecutionPeriods(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        List<InfoExecutionPeriod> allExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        Object[] args = {};

        allExecutionPeriods = (List<InfoExecutionPeriod>) ServiceManagerServiceFactory.executeService(
                null, "ReadNotClosedExecutionPeriods", args);

        List<LabelValueBean> executionPeriods = getNotClosedExecutionPeriods(allExecutionPeriods);
        request.setAttribute("executionPeriods", executionPeriods);
    }

    private String getFileName(Date date) throws FenixFilterException, FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (day + "-" + month + "-" + year + "_" + hour + ":" + minutes);
    }
}
