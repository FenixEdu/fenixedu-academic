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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.EnrolmentBlocker;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentEnrollApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentEnrollApp.class, path = "courses", titleKey = "link.student.enrollment")
@Mapping(module = "student", path = "/studentEnrollmentManagement")
@Forwards(value = {
        @Forward(name = "notAuthorized", path = "/student/notAuthorized_bd.jsp"),
        @Forward(name = "chooseRegistration", path = "/student/enrollment/chooseRegistration.jsp"),
        @Forward(name = "choosePersonalDataAuthorizationChoice",
                path = "/student/enrollment/choosePersonalDataAuthorizationChoice.jsp"),
        @Forward(name = "proceedToEnrolment", path = "/student/bolonhaStudentEnrollment.do?method=showWelcome"),
        @Forward(name = "chooseSemester", path = "/student/enrollment/chooseSemester.jsp"),
        @Forward(name = "enrollmentCannotProceed", path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp") })
public class StudentEnrollmentManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final Student student = getLoggedStudent(request);
        if (!student.hasFilledAuthorizationInformationInCurrentExecutionYear()) {
            request.setAttribute("student", student);
            return mapping.findForward("choosePersonalDataAuthorizationChoice");
        }
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        request.setAttribute("executionSemester", executionSemester);
        final List<Registration> registrationsToEnrol = getRegistrationsToEnrolByStudent(request);
        if (registrationsToEnrol.size() == 1) {
            final Registration registration = registrationsToEnrol.iterator().next();
            request.setAttribute("registration", registration);
            executionSemester = getSemesterWithValidEnrolmentPeriod(executionSemester, registration);
            request.setAttribute("executionSemester", executionSemester);
            return getActionForwardForRegistration(mapping, request, registration, executionSemester);
        } else {
            request.setAttribute("registrationsToEnrol", registrationsToEnrol);
            request.setAttribute("registrationsToChooseSecondCycle", getRegistrationsToChooseSecondCycle(student));
            return mapping.findForward("chooseRegistration");
        }
    }

    private ExecutionSemester getSemesterWithValidEnrolmentPeriod(final ExecutionSemester executionSemester, final Registration registration) {
        final StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult conditionResult = StudentCurricularPlanEnrolmentPreConditions.checkEnrolmentPeriods(registration.getLastStudentCurricularPlan(), executionSemester);
        if (conditionResult.isValid()) {
            return executionSemester;
        } else {
            final ExecutionSemester semesterWithCorrectEnrolmentPeriod = getNextSemesterWithValidEnrolmentPeriod(executionSemester.getNextExecutionPeriod(), registration);
            return semesterWithCorrectEnrolmentPeriod == null ? executionSemester : semesterWithCorrectEnrolmentPeriod;
        }
    }

    private ExecutionSemester getNextSemesterWithValidEnrolmentPeriod(final ExecutionSemester executionSemester, final Registration registration) {
        if (executionSemester == null) {
            return null;
        } else {
            final StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult conditionResult = StudentCurricularPlanEnrolmentPreConditions.checkEnrolmentPeriods(registration.getLastStudentCurricularPlan(), executionSemester);
            if (conditionResult.isValid()) {
                return executionSemester;
            } else {
                return getNextSemesterWithValidEnrolmentPeriod(executionSemester.getNextExecutionPeriod(), registration);
            }
        }
    }

    // TODO: refactor this method
    private List<Registration> getRegistrationsToChooseSecondCycle(final Student student) {
        final List<Registration> result = new ArrayList<Registration>();

        for (final Registration registration : student.getRegistrationsSet()) {

            if (!registration.isBolonha() || !registration.isConcluded()) {
                continue;
            }

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            if (!studentCurricularPlan.getDegreeType().isBolonhaMasterDegree()) {

                final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
                if (firstCycle != null && firstCycle.isConcluded()
                        && !studentCurricularPlan.hasAnyActiveRegistrationWithFirstCycleAffinity()) {
                    result.add(registration);
                }
            }
        }

        return result;
    }

    private ActionForward getActionForwardForRegistration(ActionMapping mapping, HttpServletRequest request,
            final Registration registration, ExecutionSemester executionSemester) {

        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
        request.setAttribute("executionSemesterID", executionSemester.getExternalId());

        if (EnrolmentBlocker.enrolmentBlocker.isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(studentCurricularPlan, ExecutionYear.readCurrentExecutionYear())) {
            request.setAttribute("debtsMessage",
                    "error.StudentCurricularPlan.cannot.enrol.with.debts.for.previous.execution.years");
        }

        if (studentCurricularPlan.getPerson().hasAnyResidencePaymentsInDebtForPreviousYear()) {
            request.setAttribute("debtsMessage", "error.StudentCurricularPlan.cannot.enrol.with.residence.debts");
        }

        if (!studentCurricularPlan.isActive() && !studentCurricularPlan.getRegistration().isConcluded()) {
            request.setAttribute("registrationsToEnrol", Collections.singletonList(registration));
            addActionMessage(request, "error.studentCurricularPlan.is.not.active.or.concluded");
            return mapping.findForward("chooseRegistration");
        }

        request.setAttribute("registration", registration);
        return mapping.findForward("proceedToEnrolment");
    }

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getRegistration(request);
        if (!registrationBelongsToRegistrationsToEnrol(request, registration)
                && !getRegistrationsToChooseSecondCycle(registration.getStudent()).contains(registration)) {
            return mapping.findForward("notAuthorized");
        }

        ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterID");
        executionSemester = getSemesterWithValidEnrolmentPeriod(executionSemester, registration);
        request.setAttribute("executionSemester", executionSemester);
        return getActionForwardForRegistration(mapping, request, registration, executionSemester);
    }

    private boolean registrationBelongsToRegistrationsToEnrol(HttpServletRequest request, final Registration registration) {
        return getRegistrationsToEnrolByStudent(request).contains(registration);
    }

    private Registration getRegistration(final HttpServletRequest request) {
        return getRegistrationFrom(request, "registrationId");
    }

    private Registration getRegistrationFrom(final HttpServletRequest request, final String parameterName) {
        return getDomainObject(request, parameterName);
    }

    public ActionForward choosePersonalDataAuthorizationChoice(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return prepare(mapping, form, request, response);
    }

    private List<Registration> getRegistrationsToEnrolByStudent(final HttpServletRequest request) {
        return getLoggedStudent(request).getRegistrationsToEnrolByStudent();
    }

    private Student getLoggedStudent(final HttpServletRequest request) {
        return getLoggedPerson(request).getStudent();
    }

}