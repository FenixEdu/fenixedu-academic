/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeCurricularPlanEquivalencePlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.ShiftToEnrol;
import org.fenixedu.academic.service.filter.enrollment.ClassEnrollmentAuthorizationFilter;
import org.fenixedu.academic.service.filter.enrollment.ClassEnrollmentAuthorizationFilter.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan;
import org.fenixedu.academic.service.services.enrollment.shift.ReadShiftsToEnroll;
import org.fenixedu.academic.service.services.enrollment.shift.UnEnrollStudentFromShift;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentEnrollApp;
import org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.academic.util.ExecutionDegreesFormat;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = StudentEnrollApp.class, path = "shift-enrollment", titleKey = "link.shift.enrolment")
@Mapping(module = "student", path = "/studentShiftEnrollmentManager", input = "/studentShiftEnrollmentManager.do?method=prepare",
        formBean = "studentShiftEnrollmentForm", validate = false)
@Forwards(value = {
        @Forward(name = "showEnrollmentPage",
                path = "/student/studentShiftEnrollmentManagerLookup.do?method=proceedToShiftEnrolment"),
        @Forward(name = "chooseRegistration", path = "/student/enrollment/shifts/chooseRegistration.jsp"),
        @Forward(name = "showShiftsEnrollment", path = "/student/enrollment/showShiftsEnrollment.jsp"),
        @Forward(name = "prepareEnrollmentViewWarning", path = "/student/enrollment/prepareEnrollmentViewWarning.jsp"),
        @Forward(name = "selectCourses", path = "/student/enrollment/showCoursesByDegree.jsp"),
        @Forward(name = "shiftEnrollmentCannotProceed", path = "/student/enrollment/bolonha/shiftEnrollmentCannotProceed.jsp") })
@Exceptions({
        @ExceptionHandling(
                type = ClassEnrollmentAuthorizationFilter.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan.class,
                key = "error.message.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = DomainException.class, handler = FenixDomainExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = ClassEnrollmentAuthorizationFilter.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan.class,
                key = "error.message.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan",
                handler = FenixErrorExceptionHandler.class, scope = "request") })
public class ShiftStudentEnrollmentManagerDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final Student student = getUserView(request).getPerson().getStudent();

        final List<Registration> toEnrol = student.getRegistrationsToEnrolInShiftByStudent();
        if (toEnrol.size() == 1) {
            request.setAttribute("registrationOID", toEnrol.iterator().next().getExternalId());
            return prepareStartViewWarning(mapping, form, request, response);
        } else {
            request.setAttribute("toEnrol", toEnrol);
            return mapping.findForward("chooseRegistration");
        }
    }

    private Registration getAndSetRegistration(final HttpServletRequest request) {
        String registrationOID = (String) request.getAttribute("registrationOID");
        if (StringUtils.isEmpty(registrationOID)) {
            registrationOID = request.getParameter("registrationOID");
        }
        final Registration registration = FenixFramework.getDomainObject(registrationOID);
        if (!getUserView(request).getPerson().getStudent().getRegistrationsToEnrolInShiftByStudent().contains(registration)) {
            return null;
        }

        request.setAttribute("registration", registration);
        request.setAttribute("registrationOID", registration.getExternalId().toString());
        return registration;
    }

    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String executionSemesterID = (String) getFromRequest(request, "executionSemesterID");
        if (!StringUtils.isEmpty(executionSemesterID)) {
            request.setAttribute("executionSemesterID", executionSemesterID);
        } else {
            request.setAttribute("executionSemesterID", ExecutionSemester.readActualExecutionSemester().getExternalId());
        }
        if (getAndSetRegistration(request) == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        } else {
            return mapping.findForward("prepareEnrollmentViewWarning");
        }
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        generateToken(request);
        saveToken(request);
        return prepareShiftEnrollment(mapping, form, request, response);
    }

    public ActionForward prepareShiftEnrollment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        final String classID = request.getParameter("classId");
        if (classID != null) {
            request.setAttribute("classId", classID);
            return mapping.findForward("showEnrollmentPage");
        }

        ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterID");
        request.setAttribute("executionSemesterID", executionSemester.getExternalId());
        if (readAndSetSelectCoursesParameter(request) == null) {
            return prepareShiftEnrolmentInformation(mapping, request, registration, executionSemester);
        } else {
            return prepareSelectCoursesInformation(mapping, actionForm, request, registration, executionSemester);
        }
    }

    private ActionForward prepareSelectCoursesInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, final Registration registration, final ExecutionSemester executionSemester) {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final List<ExecutionDegree> executionDegrees =
                executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType::isPreBolonhaDegree);
        executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType::isBolonhaDegree));
        executionDegrees
                .addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType::isIntegratedMasterDegree));
        executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType::isBolonhaMasterDegree));

        if (executionDegrees.isEmpty()) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        final ExecutionDegree selectedExecutionDegree =
                getSelectedExecutionDegree(form, registration, executionSemester, executionDegrees);
        if (selectedExecutionDegree == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        request.setAttribute("selectedExecutionDegree", selectedExecutionDegree);
        form.set("degree", selectedExecutionDegree.getExternalId());

        sortExecutionDegreesByDegreeName(executionDegrees);
        request.setAttribute("executionDegrees", ExecutionDegreesFormat.buildLabelValueBeansForExecutionDegree(executionDegrees));

        request.setAttribute("attendingExecutionCourses", registration.getAttendingExecutionCoursesFor(executionSemester));
        request.setAttribute("executionCoursesFromExecutionDegree", selectedExecutionDegree.getDegreeCurricularPlan()
                .getExecutionCoursesByExecutionPeriod(executionSemester));

        return mapping.findForward("selectCourses");
    }

    private void sortExecutionDegreesByDegreeName(List<ExecutionDegree> result) {
        Collections.sort(result, ExecutionDegree.COMPARATOR_BY_DEGREE_NAME);
    }

    private ActionForward prepareShiftEnrolmentInformation(ActionMapping mapping, HttpServletRequest request,
            final Registration registration, final ExecutionSemester executionSemester) throws FenixServiceException {

        final List<ShiftToEnrol> shiftsToEnrol;
        try {
            shiftsToEnrol = ReadShiftsToEnroll.runReadShiftsToEnroll(registration, executionSemester);
        } catch (OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan exception) {
            addActionMessage(request, "error.enrollment.period.closed", exception.getArgs());
            return mapping.getInputForward();
        } catch (FenixServiceException exception) {
            addActionMessage(request, exception.getMessage());
            return mapping.getInputForward();
        } catch (DomainException de) {
            addActionMessage(request, de.getLocalizedMessage(), false);
            return mapping.getInputForward();
        }

        request.setAttribute("numberOfExecutionCoursesHavingNotEnroledShifts",
                registration.getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(executionSemester));

        request.setAttribute("shiftsToEnrolFromEnroledExecutionCourses", getShiftsToEnrolByEnroledState(shiftsToEnrol, true));
        request.setAttribute("shiftsToEnrolFromUnenroledExecutionCourses", getShiftsToEnrolByEnroledState(shiftsToEnrol, false));

        final List<Shift> studentShifts = registration.getShiftsFor(executionSemester);
        request.setAttribute("studentShifts", studentShifts);
        sortStudentShifts(studentShifts);

        addSelectableSemesters(request, registration);
        return mapping.findForward("showShiftsEnrollment");
    }

    private void addSelectableSemesters(HttpServletRequest request, Registration registration) {
        List<ExecutionSemester> openedEnrolmentPeriodsSemesters =
                registration.getLastDegreeCurricularPlan().getEnrolmentPeriodsSet().stream()
                        .filter(ep -> ep.isValid() && ep.isForClasses()).map(ep -> ep.getExecutionPeriod()).distinct()
                        .sorted(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR).collect(Collectors.toList());
        if (openedEnrolmentPeriodsSemesters.size() > 1) {
            request.setAttribute("openedEnrolmentPeriodsSemesters", openedEnrolmentPeriodsSemesters);
        }
    }

    private void sortStudentShifts(List<Shift> studentShifts) {
        Collections.sort(studentShifts, Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
    }

    private List<ShiftToEnrol> getShiftsToEnrolByEnroledState(final List<ShiftToEnrol> shiftsToEnrol, boolean enroled) {
        List<ShiftToEnrol> result = new ArrayList<ShiftToEnrol>();
        for (final ShiftToEnrol shiftToEnrol : shiftsToEnrol) {
            if (shiftToEnrol.isEnrolled() == enroled) {
                result.add(shiftToEnrol);
            }
        }
        return result;
    }

    private ExecutionDegree getSelectedExecutionDegree(final DynaActionForm form, final Registration registration,
            final ExecutionSemester executionSemester, final List<ExecutionDegree> executionDegrees) {

        final String executionDegreeIdChosen = (String) form.get("degree");
        final ExecutionDegree executionDegreeChosen =
                (!StringUtils.isEmpty(executionDegreeIdChosen) ? (ExecutionDegree) FenixFramework
                        .getDomainObject(executionDegreeIdChosen) : null);
        if (executionDegreeChosen != null && executionDegreeChosen.getExecutionYear() == executionSemester.getExecutionYear()) {
            return executionDegreeChosen;
        } else {
            return searchForExecutionDegreeInStudent(registration, executionSemester);
        }
    }

    private ExecutionDegree searchForExecutionDegreeInStudent(final Registration registration,
            final ExecutionSemester executionSemester) {
        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return null;
        }
        for (final ExecutionDegree executionDegree : studentCurricularPlan.getDegreeCurricularPlan().getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionSemester.getExecutionYear()) {
                return executionDegree;
            }
        }
        for (final DegreeCurricularPlan degreeCurricularPlan : studentCurricularPlan.getDegree().getDegreeCurricularPlansSet()) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() == executionSemester.getExecutionYear()) {
                    return executionDegree;
                }
            }
        }
        for (final DegreeCurricularPlanEquivalencePlan equivalencePlan : studentCurricularPlan.getDegreeCurricularPlan()
                .getTargetEquivalencePlansSet()) {
            final DegreeCurricularPlan otherDegreeCurricularPlan = equivalencePlan.getDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : otherDegreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() == executionSemester.getExecutionYear()) {
                    return executionDegree;
                }
            }
        }
        return null;
    }

    private String readAndSetSelectCoursesParameter(final HttpServletRequest request) {
        final String selectCourses = request.getParameter("selectCourses");
        if (selectCourses != null) {
            request.setAttribute("selectCourses", selectCourses);
        }
        return selectCourses;
    }

    public ActionForward unEnroleStudentFromShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        final String shiftId = request.getParameter("shiftId");
        final String executionCourseID = request.getParameter("executionCourseID");
        if (!StringUtils.isEmpty(executionCourseID)) {
            request.setAttribute("executionCourseID", executionCourseID);
        }

        try {
            UnEnrollStudentFromShift.runUnEnrollStudentFromShift(registration, shiftId,
                    getDomainObject(request, "executionSemesterID"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return start(mapping, form, request, response);
    }

}