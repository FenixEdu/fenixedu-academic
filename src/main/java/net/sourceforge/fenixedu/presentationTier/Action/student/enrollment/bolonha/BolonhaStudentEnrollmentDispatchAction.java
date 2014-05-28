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
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;
import net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.StudentEnrollmentManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "student", path = "/bolonhaStudentEnrollment", functionality = StudentEnrollmentManagementDA.class)
@Forwards(value = {
        @Forward(name = "notAuthorized", path = "/student/notAuthorized_bd.jsp"),
        @Forward(name = "chooseOptionalCurricularCourseToEnrol",
                path = "/student/enrollment/bolonha/chooseOptionalCurricularCourseToEnrol.jsp"),
        @Forward(name = "showDegreeModulesToEnrol", path = "/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "showEnrollmentInstructions", path = "/student/enrollment/bolonha/showEnrollmentInstructions.jsp"),
        @Forward(name = "chooseCycleCourseGroupToEnrol", path = "/student/enrollment/bolonha/chooseCycleCourseGroupToEnrol.jsp"),
        @Forward(name = "welcome", path = "/student/enrollment/welcome.jsp"),
        @Forward(name = "enrollmentCannotProceed", path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp"),
        @Forward(name = "welcome-dea-degree", path = "/student/phdStudentEnrolment.do?method=showWelcome"),
        @Forward(name = "showEnrollmentInstructions", path = "/student/enrollment/bolonha/showEnrollmentInstructions.jsp"),
        @Forward(name = "enrollmentCannotProceed", path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp") })
public class BolonhaStudentEnrollmentDispatchAction extends AbstractBolonhaStudentEnrollmentDA {

    public ActionForward showWelcome(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = (Registration) request.getAttribute("registration");
        request.setAttribute("registration", registration);
        return findForwardForRegistration(mapping, registration);
    }

    private ActionForward findForwardForRegistration(ActionMapping mapping, Registration registration) {
        if (registration.getDegree().isDEA()) {
            return mapping.findForward("welcome-dea-degree");
        } else {
            return mapping.findForward("welcome");
        }
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final Registration registration = getDomainObject(request, "registrationOid");
        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, registration.getLastStudentCurricularPlan(),
                ExecutionSemester.readActualExecutionSemester());
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {

        final EnrolmentPreConditionResult result =
                StudentCurricularPlanEnrolmentPreConditions.checkPreConditionsToEnrol(studentCurricularPlan, executionSemester);

        if (!result.isValid()) {
            addActionMessage(request, result.message(), result.args());
            return mapping.findForward("enrollmentCannotProceed");
        }

        return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response, studentCurricularPlan, executionSemester);
    }

    public ActionForward showEnrollmentInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("showEnrollmentInstructions");
    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
        return null; // all years
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm actionForm) {
        return CurricularRuleLevel.ENROLMENT_WITH_RULES;
    }

    @Override
    protected String getAction() {
        return "";
    }

}
