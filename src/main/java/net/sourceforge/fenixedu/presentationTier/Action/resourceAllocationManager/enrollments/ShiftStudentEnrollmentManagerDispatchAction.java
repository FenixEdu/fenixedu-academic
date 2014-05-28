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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.enrollments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter.InquiriesNotAnswered;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.ReadShiftsToEnroll;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.UnEnrollStudentFromShift;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.ShiftToEnrol;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ExecutionPeriodDA;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "resourceAllocationManager", path = "/studentShiftEnrollmentManager",
        input = "/studentShiftEnrollmentManager.do?method=prepare", formBean = "studentShiftEnrollmentForm",
        functionality = ExecutionPeriodDA.class, validate = false)
@Forwards({
        @Forward(name = "showEnrollmentPage",
                path = "/resourceAllocationManager/studentShiftEnrollmentManagerLookup.do?method=proceedToShiftEnrolment"),
        @Forward(name = "chooseRegistration", path = "/student/enrollment/shifts/chooseRegistration.jsp"),
        @Forward(name = "showShiftsEnrollment", path = "/student/enrollment/showShiftsEnrollment.jsp"),
        @Forward(name = "selectCourses", path = "/student/showCoursesByDegree.jsp"),
        @Forward(name = "shiftEnrollmentCannotProceed", path = "/student/enrollment/bolonha/shiftEnrollmentCannotProceed.jsp"),
        @Forward(name = "chooseStudent", path = "/resourceAllocationManager/chooseExecutionPeriod.do?method=prepare") })
@Exceptions({
        @ExceptionHandling(type = CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan.class,
                key = "error.message.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = InquiriesNotAnswered.class, key = "message.student.cannotEnroll.inquiriesNotAnswered",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan.class,
                key = "error.message.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan",
                handler = FenixErrorExceptionHandler.class, scope = "request") })
public class ShiftStudentEnrollmentManagerDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final Registration registration = getDomainObject(request, "registrationOID");
        final Student student = registration.getPerson().getStudent();

        if (student.hasInquiriesToRespond()) {
            addActionMessage(request, "message.student.cannotEnroll.shifts.inquiriesNotAnswered");
            return mapping.findForward("shiftEnrollmentCannotProceed");
        }

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
        final Registration registration = getDomainObject(request, "registrationOID");
        final Student student = registration.getPerson().getStudent();

        if (!student.getRegistrationsToEnrolInShiftByStudent().contains(registration)) {
            return null;
        }

        request.setAttribute("registration", registration);
        request.setAttribute("registrationOID", registration.getExternalId().toString());
        request.setAttribute("ram", Boolean.TRUE);
        return registration;
    }

    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (getAndSetRegistration(request) == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        } else {
            return start(mapping, form, request, response);
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

        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
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
                executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType.DEGREE);
        executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_DEGREE));
        executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(
                DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
        executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_MASTER_DEGREE));

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
        request.setAttribute(
                "executionDegrees",
                ExecutionDegreesFormat.buildLabelValueBeansForExecutionDegree(executionDegrees,
                        getResources(request, "ENUMERATION_RESOURCES"), request));

        request.setAttribute("attendingExecutionCourses", registration.getAttendingExecutionCoursesFor(executionSemester));
        request.setAttribute("executionCoursesFromExecutionDegree", selectedExecutionDegree.getDegreeCurricularPlan()
                .getExecutionCoursesByExecutionPeriod(executionSemester));

        return mapping.findForward("selectCourses");
    }

    private void sortExecutionDegreesByDegreeName(List<ExecutionDegree> result) {
        Collections.sort(result, ExecutionDegree.COMPARATOR_BY_DEGREE_NAME);
    }

    private ActionForward prepareShiftEnrolmentInformation(ActionMapping mapping, HttpServletRequest request,
            final Registration registration, final ExecutionSemester executionSemester) {

        try {
            final List<ShiftToEnrol> shiftsToEnrol = ReadShiftsToEnroll.runReadShiftsToEnroll(registration);

            request.setAttribute("numberOfExecutionCoursesHavingNotEnroledShifts",
                    registration.getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(executionSemester));

            request.setAttribute("shiftsToEnrolFromEnroledExecutionCourses", getShiftsToEnrolByEnroledState(shiftsToEnrol, true));
            request.setAttribute("shiftsToEnrolFromUnenroledExecutionCourses",
                    getShiftsToEnrolByEnroledState(shiftsToEnrol, false));

            final List<Shift> studentShifts = registration.getShiftsFor(executionSemester);
            request.setAttribute("studentShifts", studentShifts);
            sortStudentShifts(studentShifts);

            return mapping.findForward("showShiftsEnrollment");

        } catch (NotAuthorizedException ffe) {
            addActionMessage(request, ffe.getMessage());
            return mapping.findForward("chooseStudent");
        } catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage());
            return mapping.findForward("chooseStudent");
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
        final ExecutionDegree executionDegreeChosen = FenixFramework.getDomainObject(executionDegreeIdChosen);
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
            UnEnrollStudentFromShift.runUnEnrollStudentFromShift(registration, shiftId);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return start(mapping, form, request, response);
    }

}