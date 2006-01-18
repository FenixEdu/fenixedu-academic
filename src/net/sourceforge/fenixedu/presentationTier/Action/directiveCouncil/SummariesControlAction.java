/*
 * Created on Jan 16, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Department;
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
import net.sourceforge.fenixedu.util.NumberUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

public class SummariesControlAction extends DispatchAction {

    public ActionForward prepareSummariesControl(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        readAndSaveAllDepartments(request);
        return mapping.findForward("success");
    }

    public ActionForward listExecutionPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        readAndSaveAllExecutionPeriods(request);
        return prepareSummariesControl(mapping, actionForm, request, response);
    }

    public ActionForward listSummariesControl(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        String departmentID = (String) dynaActionForm.get("department");
        String executionPeriodID = (String) dynaActionForm.get("executionPeriod");
        boolean runProcess = true;

        if (departmentID == null || departmentID.equals("")) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.no.deparment"));
            saveErrors(request, actionErrors);
            dynaActionForm.set("executionPeriod", "");
            runProcess = false;
        }
        if (executionPeriodID == null || executionPeriodID.equals("")) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.no.execution.period"));
            saveErrors(request, actionErrors);
            runProcess = false;
        }

        if (runProcess) {            
            getListing(request, departmentID, executionPeriodID);
        }
        
        readAndSaveAllDepartments(request);
        readAndSaveAllExecutionPeriods(request);

        return mapping.findForward("success");
    }

    private void getListing(HttpServletRequest request, String departmentID, String executionPeriodID) throws FenixFilterException, FenixServiceException {
        final Department department;
        final ExecutionPeriod executionPeriod;
        Object[] args = { Department.class, Integer.valueOf(departmentID) }, args1 = {
                ExecutionPeriod.class, Integer.valueOf(executionPeriodID) };
        try {
            department = (Department) ServiceManagerServiceFactory.executeService(null,
                    "ReadDomainObject", args);
            executionPeriod = (ExecutionPeriod) ServiceManagerServiceFactory.executeService(null,
                    "ReadDomainObject", args1);

        } catch (FenixServiceException e) {
            throw new FenixServiceException();
        }

        List<Teacher> allDepartmentTeachers = (department != null && executionPeriod != null) ? department
                .getTeachers(executionPeriod.getBeginDate(), executionPeriod.getEndDate())
                : new ArrayList<Teacher>();

        List<ListElementDTO> allListElements = new ArrayList<ListElementDTO>();

        for (Teacher teacher : allDepartmentTeachers) {
            TeacherService teacherService = teacher
                    .getTeacherServiceByExecutionPeriod(executionPeriod);
            for (Professorship professorship : teacher.getProfessorships()) {

                Double lessonHours = 0.0, summaryHours = 0.0, percentage = 0.0, difference = 0.0, totalSummaryHours = 0.0;

                if (professorship.belongsToExecutionPeriod(executionPeriod)
                        && !professorship.getExecutionCourse().isMasterDegreeOnly()) {
                    for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {

                        DegreeTeachingService degreeTeachingService = null;

                        // GET LESSON HOURS
                        if (teacherService != null) {
                            degreeTeachingService = teacherService
                                    .getDegreeTeachingServiceByShiftAndExecutionCourse(shift,
                                            professorship);
                            if (degreeTeachingService != null) {
                                percentage = degreeTeachingService.getPercentage();
                                lessonHours += getLessonHoursByExecutionCourseAndExecutionPeriod(
                                        percentage, teacherService, professorship, shift);
                            }
                        }

                        // GET SUMMARIES HOURS
                        if (degreeTeachingService != null) {
                            for (Summary summary : shift.getAssociatedSummaries()) {
                                if (summary.getProfessorship() != null
                                        && summary.getProfessorship().equals(professorship)) {
                                    Lesson lesson = SummaryUtils.getSummaryLesson(summary);
                                    if (lesson != null) {
                                        summaryHours += lesson.hours();
                                    } else {
                                        if (!shift.getAssociatedLessons().isEmpty()) {
                                            totalSummaryHours += shift.getAssociatedLessons().get(0)
                                                    .hours();
                                        }
                                    }
                                }
                            }
                        }

                        // GET TOTAL SUMMARY HOURS
                        for (Summary summary : shift.getAssociatedSummaries()) {
                            if (summary.getProfessorship() != null
                                    && summary.getProfessorship().equals(professorship)) {
                                Lesson lesson = SummaryUtils.getSummaryLesson(summary);
                                if (lesson != null) {
                                    totalSummaryHours += lesson.hours();
                                } else {
                                    if (!shift.getAssociatedLessons().isEmpty()) {
                                        totalSummaryHours += shift.getAssociatedLessons().get(0)
                                                .hours();
                                    }
                                }
                            }
                        }
                    }

                    summaryHours = NumberUtils.formatNumber(summaryHours, 1);
                    lessonHours = NumberUtils.formatNumber(lessonHours, 1);
                    difference = getDifference(lessonHours, summaryHours);

                    Category category = teacher.getCategory();
                    String categoryName = (category != null) ? category.getCode() : "";

                    ListElementDTO listElementDTO = new ListElementDTO(
                            teacher.getPerson().getNome(), professorship.getExecutionCourse()
                                    .getNome(), teacher.getTeacherNumber(), categoryName,
                            lessonHours, summaryHours, totalSummaryHours, difference);

                    allListElements.add(listElementDTO);
                }
            }
        }

        Collections.sort(allListElements, new ReverseComparator(new BeanComparator("difference")));
        request.setAttribute("listElements", allListElements);
    }

    private Double getDifference(Double lessonHours, Double summaryHours) {
        Double difference;
        difference = (1 - ((lessonHours - summaryHours) / lessonHours)) * 100;
        if (difference.isNaN()) {
            difference = 0.0;
        } else {
            difference = NumberUtils.formatNumber(difference, 2);
        }
        return difference;
    }

    private Double getLessonHoursByExecutionCourseAndExecutionPeriod(Double percentage,
            TeacherService teacherService, Professorship professorship, Shift shift) {

        Double shiftLessonHoursSum = 0.0;
        for (Lesson lesson : shift.getAssociatedLessons()) {
            shiftLessonHoursSum += lesson.hours();
        }
        return ((percentage / 100) * shiftLessonHoursSum) * 14; // 14 weeks
    }

    private List<LabelValueBean> getNotClosedExecutionPeriods(
            List<InfoExecutionPeriod> allExecutionPeriods) {
        List<LabelValueBean> executionPeriods = new ArrayList<LabelValueBean>();
        for (InfoExecutionPeriod infoExecutionPeriod : allExecutionPeriods) {
            LabelValueBean labelValueBean = new LabelValueBean();
            labelValueBean.setLabel(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - "
                    + infoExecutionPeriod.getSemester() + "º Semestre");
            labelValueBean.setValue(infoExecutionPeriod.getIdInternal().toString());
            executionPeriods.add(labelValueBean);
        }
        Collections.sort(executionPeriods, new BeanComparator("label"));
        return executionPeriods;
    }

    private List<LabelValueBean> getAllDepartments(List<Department> allDepartments) {
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
        List<Department> allDepartments = new ArrayList<Department>();
        Object[] args = { Department.class };
        try {
            allDepartments = (List<Department>) ServiceManagerServiceFactory.executeService(null,
                    "ReadAllDomainObjects", args);

        } catch (FenixServiceException e) {
            throw new FenixServiceException();
        }

        List<LabelValueBean> departments = getAllDepartments(allDepartments);
        request.setAttribute("departments", departments);
    }

    private void readAndSaveAllExecutionPeriods(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        List<InfoExecutionPeriod> allExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        Object[] args = {};
        try {

            allExecutionPeriods = (List<InfoExecutionPeriod>) ServiceManagerServiceFactory
                    .executeService(null, "ReadNotClosedExecutionPeriods", args);

        } catch (FenixServiceException e) {
            throw new FenixServiceException();
        }

        List<LabelValueBean> executionPeriods = getNotClosedExecutionPeriods(allExecutionPeriods);
        request.setAttribute("executionPeriods", executionPeriods);
    }

    public class ListElementDTO {
        String teacherName, executionCourseName, categoryName;

        Integer teacherNumber;

        Double lessonHours, summaryHours, totalSummaryHours, difference;

        public ListElementDTO(String teacherName, String executionCourseName, Integer teacherNumber,
                String categoryName, Double lessonHours, Double summaryHours, Double totalSummaryHours,
                Double difference) {

            // this.difference = difference;
            this.difference = difference;
            this.executionCourseName = executionCourseName;
            this.lessonHours = lessonHours;
            this.summaryHours = summaryHours;
            this.teacherName = teacherName;
            this.teacherNumber = teacherNumber;
            this.totalSummaryHours = totalSummaryHours;
            this.categoryName = categoryName;
        }

        public Double getDifference() {
            return difference;
        }

        public String getExecutionCourseName() {
            return executionCourseName;
        }

        public Double getLessonHours() {
            return lessonHours;
        }

        public Double getSummaryHours() {
            return summaryHours;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public Integer getTeacherNumber() {
            return teacherNumber;
        }

        public Double getTotalSummaryHours() {
            return totalSummaryHours;
        }

        public String getCategoryName() {
            return categoryName;
        }
    }
}
