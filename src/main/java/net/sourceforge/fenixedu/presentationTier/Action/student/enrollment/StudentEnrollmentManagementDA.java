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
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha.EnrolInAffinityCycle;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.CycleEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentEnrollApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentEnrollApp.class, path = "courses", titleKey = "link.student.enrollment")
@Mapping(module = "student", path = "/studentEnrollmentManagement")
@Forwards(value = {
        @Forward(name = "notAuthorized", path = "/student/notAuthorized_bd.jsp"),
        @Forward(name = "chooseRegistration", path = "/student/enrollment/chooseRegistration.jsp"),
        @Forward(name = "choosePersonalDataAuthorizationChoice",
                path = "/student/enrollment/choosePersonalDataAuthorizationChoice.jsp"),
        @Forward(name = "proceedToEnrolment", path = "/student/bolonhaStudentEnrollment.do?method=showWelcome"),
        @Forward(name = "showAffinityToEnrol", path = "/student/enrollment/bolonha/showAffinityToEnrol.jsp"),
        @Forward(name = "selectAffinityToEnrol", path = "/student/enrollment/bolonha/selectAffinityToEnrol.jsp"),
        @Forward(name = "enrollmentCannotProceed", path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp") })
public class StudentEnrollmentManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final Student student = getLoggedStudent(request);
        if (!student.hasFilledAuthorizationInformationInCurrentExecutionYear()) {
            request.setAttribute("student", student);
            return mapping.findForward("choosePersonalDataAuthorizationChoice");
        }

        if (student.hasInquiriesToRespond()) {
            addActionMessage(request, "message.student.cannotEnroll.inquiriesNotAnswered");
            return mapping.findForward("enrollmentCannotProceed");
        }

        final List<Registration> registrationsToEnrol = getRegistrationsToEnrolByStudent(request);
        if (registrationsToEnrol.size() == 1) {
            final Registration registration = registrationsToEnrol.iterator().next();
            return getActionForwardForRegistration(mapping, request, registration);
        }

        request.setAttribute("registrationsToEnrol", registrationsToEnrol);
        request.setAttribute("registrationsToChooseSecondCycle", getRegistrationsToChooseSecondCycle(student));

        return mapping.findForward("chooseRegistration");
    }

    // TODO: refactor this method
    private List<Registration> getRegistrationsToChooseSecondCycle(final Student student) {
        final List<Registration> result = new ArrayList<Registration>();

        for (final Registration registration : student.getRegistrationsSet()) {

            if (!registration.isBolonha() || !registration.isConcluded()) {
                continue;
            }

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            if (studentCurricularPlan.getDegreeType() != DegreeType.BOLONHA_MASTER_DEGREE) {

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
            final Registration registration) {

        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();

        // ----------------------------------------------------------------------
        // ---------------------------------------------
        // TODO: refactor this code, should be more generic
        // ----------------------------------------------------------------------
        // ---------------------------------------------

        if (!studentCurricularPlan.isActive() && !studentCurricularPlan.getRegistration().isConcluded()) {
            request.setAttribute("registrationsToEnrol", Collections.singletonList(registration));
            addActionMessage(request, "error.studentCurricularPlan.is.not.active.or.concluded");
            return mapping.findForward("chooseRegistration");
        }

        if (studentCurricularPlan.getDegreeType() == DegreeType.BOLONHA_MASTER_DEGREE) {
            request.setAttribute("registration", registration);
            return mapping.findForward("proceedToEnrolment");

        } else {
            final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();

            if (firstCycle == null || !firstCycle.isConcluded()) {
                request.setAttribute("registration", registration);
                return mapping.findForward("proceedToEnrolment");

            } else {
                final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
                if (secondCycle == null) {
                    return prepareSelectAffinityToEnrol(mapping, request, studentCurricularPlan, executionSemester);

                } else if (secondCycle.isExternal()) {
                    final Student student = studentCurricularPlan.getRegistration().getStudent();
                    final Registration newRegistration =
                            student.getActiveRegistrationFor(secondCycle.getDegreeCurricularPlanOfDegreeModule());

                    if (newRegistration != null) {
                        request.setAttribute("registration", newRegistration);
                        return mapping.findForward("proceedToEnrolment");
                    }

                    return showAffinityToEnrol(mapping, request, studentCurricularPlan, executionSemester, secondCycle);

                } else {
                    request.setAttribute("registration", registration);
                    return mapping.findForward("proceedToEnrolment");
                }
            }
        }

        // ----------------------------------------------------------------------
        // ---------------------------------------------
        // TODO: refactor this code, should be more generic
        // ----------------------------------------------------------------------
        // ---------------------------------------------

    }

    private ActionForward prepareSelectAffinityToEnrol(final ActionMapping mapping, final HttpServletRequest request,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {

        if (!canContinueToEnrolment(request, studentCurricularPlan, executionSemester)) {
            return mapping.findForward("enrollmentCannotProceed");
        }

        request.setAttribute("cycleEnrolmentBean", new CycleEnrolmentBean(studentCurricularPlan, executionSemester,
                CycleType.FIRST_CYCLE, CycleType.SECOND_CYCLE));

        return mapping.findForward("selectAffinityToEnrol");
    }

    private ActionForward showAffinityToEnrol(final ActionMapping mapping, final HttpServletRequest request,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final CycleCurriculumGroup curriculumGroup) {

        if (!canContinueToEnrolment(request, studentCurricularPlan, executionSemester)) {
            return mapping.findForward("enrollmentCannotProceed");
        }

        request.setAttribute("cycleEnrolmentBean", new CycleEnrolmentBean(studentCurricularPlan, executionSemester,
                curriculumGroup.getCycleCourseGroup()));

        return mapping.findForward("showAffinityToEnrol");
    }

    public ActionForward showAffinityToEnrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final CycleEnrolmentBean bean = getCycleEnrolmentBeanFromViewState();

        if (!canContinueToEnrolment(request, bean.getStudentCurricularPlan(), bean.getExecutionPeriod())) {
            return mapping.findForward("enrollmentCannotProceed");
        }

        request.setAttribute("cycleEnrolmentBean", bean);
        return mapping.findForward("showAffinityToEnrol");
    }

    private boolean canContinueToEnrolment(final HttpServletRequest request, final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester) {

        final EnrolmentPreConditionResult result =
                StudentCurricularPlanEnrolmentPreConditions.checkPreConditionsToEnrol(studentCurricularPlan, executionSemester);

        if (!result.isValid()) {

            addActionMessage(request, result.message(), result.args());
            return false;

        } else {
            return true;
        }
    }

    public ActionForward enrolInAffinityCycle(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final CycleEnrolmentBean cycleEnrolmentBean = getCycleEnrolmentBeanFromViewState();

        try {
            final Registration registration =
                    EnrolInAffinityCycle.run(getLoggedPerson(request), cycleEnrolmentBean.getStudentCurricularPlan(),
                            cycleEnrolmentBean.getCycleCourseGroupToEnrol(), cycleEnrolmentBean.getExecutionPeriod());

            request.setAttribute("registration", registration);
        } catch (final IllegalDataAccessException e) {
            addActionMessage(request, "error.NotAuthorized");
            request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
            return mapping.findForward("showAffinityToEnrol");

        } catch (final DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
            return mapping.findForward("showAffinityToEnrol");
        }

        return mapping.findForward("proceedToEnrolment");
    }

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getRegistration(request);
        if (!registrationBelongsToRegistrationsToEnrol(request, registration)
                && !getRegistrationsToChooseSecondCycle(registration.getStudent()).contains(registration)) {
            return mapping.findForward("notAuthorized");
        }

        return getActionForwardForRegistration(mapping, request, registration);
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

    private CycleEnrolmentBean getCycleEnrolmentBeanFromViewState() {
        return getRenderedObject();
    }
}