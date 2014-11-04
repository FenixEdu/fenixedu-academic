/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Jan 16, 2006
 *	by mrsp
 */
package org.fenixedu.academic.ui.struts.action.directiveCouncil;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.directiveCouncil.DepartmentSummaryElement;
import org.fenixedu.academic.dto.directiveCouncil.DepartmentSummaryElement.SummaryControlCategory;
import org.fenixedu.academic.dto.directiveCouncil.DetailSummaryElement;
import org.fenixedu.academic.dto.directiveCouncil.ExecutionCourseSummaryElement;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.DegreeTeachingService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

public abstract class SummariesControlAction extends FenixDispatchAction {

    private final BigDecimal EMPTY = BigDecimal.ZERO;
    private static final String DEFAULT_MODULE = "pedagogicalCouncil";
    private static final String ENUMERATION_MODULE = "Enumeration";

    @EntryPoint
    public ActionForward prepareSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DepartmentSummaryElement departmentSummaryElement = new DepartmentSummaryElement(null, null);
        request.setAttribute("executionSemesters", departmentSummaryElement);
        return mapping.findForward("success");
    }

    public ActionForward listSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DepartmentSummaryElement departmentSummaryElement = getRenderedObject();
        String executionSemesterID = null;
        if (departmentSummaryElement != null) {
            executionSemesterID = departmentSummaryElement.getExecutionSemester().getExternalId();
        } else {
            executionSemesterID = (String) getFromRequest(request, "executionSemesterID");
            ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionSemesterID);
            departmentSummaryElement = new DepartmentSummaryElement(null, executionSemester);
        }
        request.setAttribute("executionSemesters", departmentSummaryElement);
        setAllDepartmentsSummaryResume(request, executionSemesterID);

        return mapping.findForward("success");
    }

    public ActionForward listDepartmentSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DepartmentSummaryElement departmentSummaryElement = getRenderedObject();
        SummaryControlCategory summaryControlCategory = departmentSummaryElement.getSummaryControlCategory();
        departmentSummaryElement =
                getDepartmentSummaryResume(departmentSummaryElement.getExecutionSemester(),
                        departmentSummaryElement.getDepartment());
        departmentSummaryElement.setSummaryControlCategory(summaryControlCategory);

        request.setAttribute("departmentResume", departmentSummaryElement);
        List<DepartmentSummaryElement> departmentList = new ArrayList<DepartmentSummaryElement>();
        departmentList.add(departmentSummaryElement);
        request.setAttribute("departmentResumeList", departmentList);
        RenderUtils.invalidateViewState();

        return mapping.findForward("success");
    }

    public ActionForward departmentSummariesResume(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String departmentID = request.getParameter("departmentID");
        String executionPeriodID = request.getParameter("executionSemesterID");
        String categoryControl = (String) getFromRequest(request, "categoryControl");

        SummaryControlCategory summaryControlCategory = null;
        if (!StringUtils.isEmpty(categoryControl)) {
            summaryControlCategory = SummaryControlCategory.valueOf(categoryControl);
        }

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodID);
        final Department department = FenixFramework.getDomainObject(departmentID);
        DepartmentSummaryElement departmentSummaryResume = getDepartmentSummaryResume(executionSemester, department);
        departmentSummaryResume.setSummaryControlCategory(summaryControlCategory);
        request.setAttribute("departmentResume", departmentSummaryResume);

        List<DepartmentSummaryElement> departmentList = new ArrayList<DepartmentSummaryElement>();
        departmentList.add(departmentSummaryResume);
        request.setAttribute("departmentResumeList", departmentList);

        return mapping.findForward("success");
    }

    public ActionForward executionCourseSummariesControl(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        String departmentID = (String) getFromRequest(request, "departmentID");
        String categoryControl = (String) getFromRequest(request, "categoryControl");
        String executionCourseId = (String) getFromRequest(request, "executionCourseID");
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);

        List<DetailSummaryElement> executionCoursesResume =
                getExecutionCourseResume(executionCourse.getExecutionPeriod(), executionCourse.getProfessorshipsSet());

        request.setAttribute("departmentID", departmentID);
        request.setAttribute("categoryControl", categoryControl);
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("executionCoursesResume", executionCoursesResume);
        return mapping.findForward("success");
    }

    public ActionForward teacherSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String departmentID = (String) getFromRequest(request, "departmentID");
        String categoryControl = (String) getFromRequest(request, "categoryControl");
        String executionSemesterId = (String) getFromRequest(request, "executionSemesterID");

        String personId = (String) getFromRequest(request, "personID");
        Person person = FenixFramework.getDomainObject(personId);

        List<Pair<ExecutionSemester, List<DetailSummaryElement>>> last4SemestersSummaryControl =
                new ArrayList<Pair<ExecutionSemester, List<DetailSummaryElement>>>();
        ExecutionSemester executionSemesterToPresent = ExecutionSemester.readActualExecutionSemester();

        List<DetailSummaryElement> executionCoursesResume =
                getExecutionCourseResume(executionSemesterToPresent,
                        person.getProfessorshipsByExecutionSemester(executionSemesterToPresent));
        last4SemestersSummaryControl.add(new Pair<ExecutionSemester, List<DetailSummaryElement>>(executionSemesterToPresent,
                executionCoursesResume));
        for (int iter = 0; iter < 3; iter++) {
            executionSemesterToPresent = executionSemesterToPresent.getPreviousExecutionPeriod();
            executionCoursesResume =
                    getExecutionCourseResume(executionSemesterToPresent,
                            person.getProfessorshipsByExecutionSemester(executionSemesterToPresent));
            last4SemestersSummaryControl.add(new Pair<ExecutionSemester, List<DetailSummaryElement>>(executionSemesterToPresent,
                    executionCoursesResume));
        }

        request.setAttribute("last4SemestersSummaryControl", last4SemestersSummaryControl);
        request.setAttribute("person", person);
        request.setAttribute("departmentID", departmentID);
        request.setAttribute("categoryControl", categoryControl);
        request.setAttribute("executionSemesterID", executionSemesterId);

        return mapping.findForward("success");
    }

    private List<DetailSummaryElement> getExecutionCourseResume(final ExecutionSemester executionSemester,
            Collection<Professorship> professorships) {
        List<DetailSummaryElement> allListElements = new ArrayList<DetailSummaryElement>();
        LocalDate today = new LocalDate();
        LocalDate oneWeekBeforeToday = today.minusDays(8);
        for (Professorship professorship : professorships) {
            BigDecimal summariesGiven = EMPTY, lessonsDeclared = EMPTY;
            BigDecimal givenSumariesPercentage = EMPTY;
            BigDecimal notTaughtSummaries = EMPTY;
            BigDecimal notTaughtSummariesPercentage = EMPTY;
            if (professorship.belongsToExecutionPeriod(executionSemester)
                    && !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()) {
                for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {
                    DegreeTeachingService degreeTeachingService =
                            TeacherService.getDegreeTeachingServiceByShift(professorship, shift);
                    if (degreeTeachingService != null) {
                        // Get the number of declared lessons
                        lessonsDeclared =
                                getDeclaredLesson(degreeTeachingService.getPercentage(), shift, lessonsDeclared,
                                        oneWeekBeforeToday);
                    }
                    // Get the number of summaries given
                    summariesGiven = getSummariesGiven(professorship, shift, summariesGiven, oneWeekBeforeToday);
                    // Get the number of not taught summaries
                    notTaughtSummaries = getNotTaughtSummaries(professorship, shift, notTaughtSummaries, oneWeekBeforeToday);
                }
                summariesGiven = summariesGiven.setScale(1, RoundingMode.HALF_UP);
                notTaughtSummaries = notTaughtSummaries.setScale(1, RoundingMode.HALF_UP);
                lessonsDeclared = lessonsDeclared.setScale(1, RoundingMode.HALF_UP);
                givenSumariesPercentage = getDifference(lessonsDeclared, summariesGiven);
                notTaughtSummariesPercentage = getDifference(lessonsDeclared, notTaughtSummaries);

                Teacher teacher = professorship.getTeacher();
                String categoryName =
                        teacher != null && ProfessionalCategory.getCategory(teacher) != null ? ProfessionalCategory
                                .getCategory(teacher).getName().getContent() : null;
                String siglas = getSiglas(professorship);

                String teacherEmail =
                        professorship.getPerson().getDefaultEmailAddress() != null ? professorship.getPerson()
                                .getDefaultEmailAddress().getPresentationValue() : null;

                DetailSummaryElement listElementDTO =
                        new DetailSummaryElement(professorship.getPerson().getName(), professorship.getExecutionCourse()
                                .getNome(), teacher != null ? teacher.getTeacherId() : null, teacherEmail, categoryName,
                                lessonsDeclared, summariesGiven, givenSumariesPercentage, notTaughtSummaries,
                                notTaughtSummariesPercentage, siglas);

                allListElements.add(listElementDTO);
            }
        }
        return allListElements;
    }

    private void setAllDepartmentsSummaryResume(HttpServletRequest request, String executionPeriodOID)
            throws FenixServiceException {

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodOID);

        List<DepartmentSummaryElement> allDepartmentsSummariesResume = new ArrayList<DepartmentSummaryElement>();
        for (Department department : rootDomainObject.getDepartmentsSet()) {
            DepartmentSummaryElement departmentSummariesElement = getDepartmentSummaryResume(executionSemester, department);
            allDepartmentsSummariesResume.add(departmentSummariesElement);
        }
        if (executionSemester.isCurrent()) {
            LocalDate oneWeekBeforeDate = new LocalDate();
            request.setAttribute("currentSemester", "true");
            request.setAttribute("oneWeekBeforeDate", oneWeekBeforeDate.minusDays(8));
        }
        Collections.sort(allDepartmentsSummariesResume, new BeanComparator("department.realName"));
        request.setAttribute("summariesResumeMap", allDepartmentsSummariesResume);
    }

    private DepartmentSummaryElement getDepartmentSummaryResume(final ExecutionSemester executionSemester,
            final Department department) {
        DepartmentSummaryElement departmentSummariesElement = new DepartmentSummaryElement(department, executionSemester);
        Set<ExecutionCourse> allDepartmentExecutionCourses = getDepartmentExecutionCourses(department, executionSemester);
        if (allDepartmentExecutionCourses != null) {
            LocalDate today = new LocalDate();
            LocalDate oneWeekBeforeToday = today.minusDays(8);
            for (ExecutionCourse executionCourse : allDepartmentExecutionCourses) {
                int instanceLessonsTotal[] = { 0, 0, 0 };
                for (Shift shift : executionCourse.getAssociatedShifts()) {
                    getInstanceLessonsTotalsByShift(shift, instanceLessonsTotal, oneWeekBeforeToday);
                }
                BigDecimal result = BigDecimal.valueOf(0);
                BigDecimal numberOfLessonInstances = BigDecimal.valueOf(instanceLessonsTotal[0]);
                BigDecimal numberOfLessonInstancesWithSummary = BigDecimal.valueOf(0);
                BigDecimal percentageOfLessonsWithNotTaughtSummary = BigDecimal.valueOf(0);
                BigDecimal numberOfLessonInstancesWithNotTaughtSummary = BigDecimal.valueOf(0);
                if (instanceLessonsTotal[0] == 0) {
                    continue;
                }
                if (instanceLessonsTotal[1] != 0) {
                    numberOfLessonInstancesWithSummary = BigDecimal.valueOf(instanceLessonsTotal[1]);
                    result =
                            numberOfLessonInstancesWithSummary.divide(numberOfLessonInstances, 3, BigDecimal.ROUND_CEILING)
                                    .multiply(BigDecimal.valueOf(100));
                }
                if (instanceLessonsTotal[2] != 0) {
                    numberOfLessonInstancesWithNotTaughtSummary = BigDecimal.valueOf(instanceLessonsTotal[2]);
                    percentageOfLessonsWithNotTaughtSummary =
                            numberOfLessonInstancesWithNotTaughtSummary.divide(numberOfLessonInstances, 3,
                                    BigDecimal.ROUND_CEILING).multiply(BigDecimal.valueOf(100));
                }
                SummaryControlCategory resumeClassification = getResumeClassification(result);
                Map<SummaryControlCategory, List<ExecutionCourseSummaryElement>> departmentResumeMap =
                        departmentSummariesElement.getExecutionCoursesResume();
                List<ExecutionCourseSummaryElement> executionCoursesSummary = null;
                if (departmentResumeMap == null) {
                    departmentResumeMap = new HashMap<SummaryControlCategory, List<ExecutionCourseSummaryElement>>();
                    executionCoursesSummary = new ArrayList<ExecutionCourseSummaryElement>();
                    ExecutionCourseSummaryElement executionCourseSummaryElement =
                            new ExecutionCourseSummaryElement(executionCourse, numberOfLessonInstances,
                                    numberOfLessonInstancesWithSummary, result, numberOfLessonInstancesWithNotTaughtSummary,
                                    percentageOfLessonsWithNotTaughtSummary);
                    executionCoursesSummary.add(executionCourseSummaryElement);
                    departmentResumeMap.put(resumeClassification, executionCoursesSummary);
                    departmentSummariesElement.setExecutionCoursesResume(departmentResumeMap);
                } else {
                    executionCoursesSummary = departmentResumeMap.get(resumeClassification);
                    if (executionCoursesSummary == null) {
                        executionCoursesSummary = new ArrayList<ExecutionCourseSummaryElement>();
                        ExecutionCourseSummaryElement executionCourseSummaryElement =
                                new ExecutionCourseSummaryElement(executionCourse, numberOfLessonInstances,
                                        numberOfLessonInstancesWithSummary, result, numberOfLessonInstancesWithNotTaughtSummary,
                                        percentageOfLessonsWithNotTaughtSummary);
                        executionCoursesSummary.add(executionCourseSummaryElement);
                        departmentResumeMap.put(resumeClassification, executionCoursesSummary);
                    } else {
                        ExecutionCourseSummaryElement executionCourseSummaryElement =
                                new ExecutionCourseSummaryElement(executionCourse, numberOfLessonInstances,
                                        numberOfLessonInstancesWithSummary, result, numberOfLessonInstancesWithNotTaughtSummary,
                                        percentageOfLessonsWithNotTaughtSummary);
                        executionCoursesSummary.add(executionCourseSummaryElement);
                    }

                }
            }
        }
        return departmentSummariesElement;
    }

    private Set<ExecutionCourse> getDepartmentExecutionCourses(Department department, ExecutionSemester executionSemester) {
        Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
        List<Teacher> allDepartmentTeachers = department.getAllTeachers(executionSemester);

        for (Teacher teacher : allDepartmentTeachers) {
            for (Professorship professorship : teacher.getProfessorships()) {
                if (professorship.belongsToExecutionPeriod(executionSemester)
                        && !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()) {
                    executionCourses.add(professorship.getExecutionCourse());
                }
            }
        }
        return executionCourses;
    }

    private SummaryControlCategory getResumeClassification(BigDecimal result) {
        if (result.compareTo(BigDecimal.valueOf(20)) < 0) {
            return SummaryControlCategory.BETWEEN_0_20;
        }
        if (result.compareTo(BigDecimal.valueOf(40)) < 0) {
            return SummaryControlCategory.BETWEEN_20_40;
        }
        if (result.compareTo(BigDecimal.valueOf(60)) < 0) {
            return SummaryControlCategory.BETWEEN_40_60;
        }
        if (result.compareTo(BigDecimal.valueOf(80)) < 0) {
            return SummaryControlCategory.BETWEEN_60_80;
        }
        return SummaryControlCategory.BETWEEN_80_100;
    }

    private BigDecimal getDeclaredLesson(Double percentage, Shift shift, BigDecimal lessonGiven, LocalDate oneWeekBeforeToday) {
        BigDecimal shiftLessonSum = EMPTY;
        for (Lesson lesson : shift.getAssociatedLessonsSet()) {
            shiftLessonSum =
                    shiftLessonSum.add(BigDecimal.valueOf(lesson.getAllLessonDatesUntil(new YearMonthDay(oneWeekBeforeToday))
                            .size()));
        }
        return lessonGiven.add(BigDecimal.valueOf((percentage / 100)).multiply(shiftLessonSum));
    }

    /**
     * Receives a shift and an array of int, with two positions filled in at
     * index 0 is the total number of lessonsInstance at index 1 is the total
     * number of lessonsInstance with a summary For the given shift this values
     * are checked and added in the correspondent position of the array
     * 
     * @param shift
     * @param instanceLessonsTotals
     * @param oneWeekBeforeToday
     */
    private void getInstanceLessonsTotalsByShift(Shift shift, int[] instanceLessonsTotals, LocalDate oneWeekBeforeToday) {
        int numberOfPossibleInstanceLessons = 0;
        int numberOfInstanceLessonsWithSummary = 0;
        int numberOfInstanceLessonsWithNotTaughtSummary = 0;
        for (Lesson lesson : shift.getAssociatedLessonsSet()) {
            List<LessonInstance> allLessonInstanceUntil = lesson.getAllLessonInstancesUntil(oneWeekBeforeToday);
            Set<YearMonthDay> allPossibleDates = lesson.getAllLessonDatesUntil(new YearMonthDay(oneWeekBeforeToday));
            numberOfPossibleInstanceLessons += allPossibleDates.size();
            for (LessonInstance lessonInstance : allLessonInstanceUntil) {
                if (lessonInstance.getSummary() != null) {
                    numberOfInstanceLessonsWithSummary++;
                    if (lessonInstance.getSummary().getTaught() != null && lessonInstance.getSummary().getTaught() == false) {
                        numberOfInstanceLessonsWithNotTaughtSummary++;
                    }
                }
            }
        }
        instanceLessonsTotals[0] = instanceLessonsTotals[0] + numberOfPossibleInstanceLessons;
        instanceLessonsTotals[1] = instanceLessonsTotals[1] + numberOfInstanceLessonsWithSummary;
        instanceLessonsTotals[2] = instanceLessonsTotals[2] + numberOfInstanceLessonsWithNotTaughtSummary;
    }

    private BigDecimal getSummariesGiven(Professorship professorship, Shift shift, BigDecimal summariesGiven,
            LocalDate oneWeekBeforeToday) {
        for (Summary summary : shift.getAssociatedSummariesSet()) {
            if (summary.getProfessorship() != null && summary.getProfessorship() == professorship && !summary.getIsExtraLesson()
                    && !summary.getLessonInstance().getBeginDateTime().toLocalDate().isAfter(oneWeekBeforeToday)) {
                summariesGiven = summariesGiven.add(BigDecimal.ONE);
            }
        }
        return summariesGiven;
    }

    private BigDecimal getNotTaughtSummaries(Professorship professorship, Shift shift, BigDecimal notTaughtSummaries,
            LocalDate oneWeekBeforeToday) {
        for (Summary summary : shift.getAssociatedSummariesSet()) {
            if (summary.getProfessorship() != null && summary.getProfessorship() == professorship && !summary.getIsExtraLesson()
                    && !summary.getLessonInstance().getBeginDateTime().toLocalDate().isAfter(oneWeekBeforeToday)) {
                if (summary.getTaught() != null && summary.getTaught() == false) {
                    notTaughtSummaries = notTaughtSummaries.add(BigDecimal.ONE);
                }
            }
        }
        return notTaughtSummaries;
    }

    private BigDecimal getDifference(BigDecimal lessonHours, BigDecimal summaryHours) {
        Double difference;
        difference = (1 - ((lessonHours.doubleValue() - summaryHours.doubleValue()) / lessonHours.doubleValue())) * 100;
        if (difference.isNaN() || difference.isInfinite()) {
            difference = 0.0;
        }
        return BigDecimal.valueOf(difference).setScale(2, RoundingMode.HALF_UP);
    }

    private String getSiglas(Professorship professorship) {
        ExecutionCourse executionCourse = professorship.getExecutionCourse();
        int numberOfCurricularCourse = executionCourse.getAssociatedCurricularCoursesSet().size();

        List<String> siglas = new ArrayList<String>();
        StringBuilder buffer = new StringBuilder();

        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            String sigla = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
            if (!siglas.contains(sigla)) {
                if (numberOfCurricularCourse < executionCourse.getAssociatedCurricularCoursesSet().size()) {
                    buffer.append(",");
                }
                buffer.append(sigla);
                siglas.add(sigla);
            }
            numberOfCurricularCourse--;
        }
        return buffer.toString();
    }

    private List<LabelValueBean> getAllDepartments(Collection<Department> allDepartments) {
        List<LabelValueBean> departments = new ArrayList<LabelValueBean>();
        for (Department department : allDepartments) {
            LabelValueBean labelValueBean = new LabelValueBean();
            labelValueBean.setValue(department.getExternalId().toString());
            labelValueBean.setLabel(department.getRealName());
            departments.add(labelValueBean);
        }
        Collections.sort(departments, new BeanComparator("label"));
        return departments;
    }

    protected void readAndSaveAllDepartments(HttpServletRequest request) throws FenixServiceException {
        Collection<Department> allDepartments = rootDomainObject.getDepartmentsSet();
        List<LabelValueBean> departments = getAllDepartments(allDepartments);
        request.setAttribute("allDepartments", allDepartments);
        request.setAttribute("departments", departments);
    }

    /**
     * Method responsible for exporting 'departmentSummaryResume' to excel file
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String departmentID = request.getParameter("departmentID");
        String executionSemesterID = request.getParameter("executionSemesterID");
        String categoryControl = request.getParameter("categoryControl");

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionSemesterID);
        final Department department = FenixFramework.getDomainObject(departmentID);
        SummaryControlCategory summaryControlCategory = null;
        String controlCategory = null;
        if (!StringUtils.isEmpty(categoryControl)) {
            summaryControlCategory = SummaryControlCategory.valueOf(categoryControl);
            controlCategory = BundleUtil.getString(Bundle.ENUMERATION, summaryControlCategory.toString());
        } else {
            controlCategory = "0-100";
        }

        DepartmentSummaryElement departmentSummaryResume = getDepartmentSummaryResume(executionSemester, department);
        departmentSummaryResume.setSummaryControlCategory(summaryControlCategory);

        if (departmentSummaryResume != null) {
            String sigla = departmentSummaryResume.getDepartment().getAcronym();
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
            String date = fmt.print(dt);

            final String filename =
                    BundleUtil.getString(Bundle.APPLICATION, "link.summaries.control").replaceAll(" ", "_") + "_"
                            + controlCategory + "_" + sigla + "_" + date + ".xls";

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            ServletOutputStream writer = response.getOutputStream();
            exportToXls(departmentSummaryResume, departmentSummaryResume.getDepartment(), executionSemester, writer);
            writer.flush();
            response.flushBuffer();
        }

        return null;
    }

    private void exportToXls(DepartmentSummaryElement departmentSummaryResume, final Department department,
            final ExecutionSemester executionSemester, final OutputStream os) throws IOException {
        final StyledExcelSpreadsheet spreadsheet =
                new StyledExcelSpreadsheet(BundleUtil.getString(Bundle.APPLICATION, "link.summaries.control"));

        fillSpreadSheet(departmentSummaryResume, department, executionSemester, spreadsheet);
        spreadsheet.getWorkbook().write(os);
    }

    /**
     * Method responsible for filling the spreadsheet with department summary
     * information
     * 
     * @param departmentSummaryResume
     * @param department
     * @param semester
     * @param sheet
     */
    private void fillSpreadSheet(DepartmentSummaryElement departmentSummaryResume, Department department,
            ExecutionSemester semester, final StyledExcelSpreadsheet sheet) {
        setHeaders(sheet);
        int counter = 0;
        List<ExecutionCourseSummaryElement> executionCourses = departmentSummaryResume.getExecutionCourses();

        // Iterate on all executionCourses and print them
        for (ExecutionCourseSummaryElement executionCourse : executionCourses) {
            counter = 0;
            List<DetailSummaryElement> executionCoursesResume =
                    getExecutionCourseResume(executionCourse.getExecutionCourse().getExecutionPeriod(), executionCourse
                            .getExecutionCourse().getProfessorshipsSet());

            int lessons = executionCourse.getNumberOfLessonInstances().intValue();
            int lessonsWithSummaries = executionCourse.getNumberOfLessonInstancesWithSummary().intValue();
            double lessonsWithSummariesPercentage = executionCourse.getPercentageOfLessonsWithSummary().doubleValue();
            int lessonsWithNotTaughtSummaries = executionCourse.getNumberOfLessonInstancesWithNotTaughtSummary().intValue();
            double lessonsWithNotTaughtSummariesPercentage =
                    executionCourse.getPercentageOfLessonsWithNotTaughtSummary().doubleValue();
            for (DetailSummaryElement detailSummaryElement : executionCoursesResume) {
                if (counter == 0) {
                    sheet.newRow();
                    sheet.addCell(semester.getName(), sheet.getExcelStyle().getLabelStyle());
                    sheet.addCell(department.getName(), sheet.getExcelStyle().getLabelStyle());
                    sheet.addCell(executionCourse.getExecutionCourse().getName(), sheet.getExcelStyle().getLabelStyle());
                    sheet.addCell(lessons, sheet.getExcelStyle().getLabelStyle());
                    sheet.addCell(lessonsWithSummaries, sheet.getExcelStyle().getLabelStyle());
                    sheet.addCell(lessonsWithSummariesPercentage, sheet.getExcelStyle().getLabelStyle());
                    sheet.addCell(lessonsWithNotTaughtSummaries, sheet.getExcelStyle().getLabelStyle());
                    sheet.addCell(lessonsWithNotTaughtSummariesPercentage, sheet.getExcelStyle().getLabelStyle());

                }
                sheet.newRow();
                sheet.addCell(null);
                sheet.addCell(null);
                sheet.addCell(null);
                sheet.addCell(detailSummaryElement.getDeclaredLessons());
                sheet.addCell(detailSummaryElement.getGivenSummaries());
                sheet.addCell(detailSummaryElement.getGivenSummariesPercentage());
                sheet.addCell(detailSummaryElement.getGivenNotTaughtSummaries());
                sheet.addCell(detailSummaryElement.getGivenNotTaughtSummariesPercentage());

                sheet.addCell(detailSummaryElement.getTeacherName());
                sheet.addCell(detailSummaryElement.getTeacherId());
                sheet.addCell(detailSummaryElement.getTeacherEmail());
                counter++;

            }
        }
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newHeaderRow();

        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.semester"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.department"), 10000);
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.course"), 10000);
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.lessons"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.lessons.summaries"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.lessons.summaries.percentage"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.lessons.notTaught.summaries"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.lessons.notTaught.summaries.percentage"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.professorName"), 10000);
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.professorUsername"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.excel.professorEmail"), 10000);
    }

}