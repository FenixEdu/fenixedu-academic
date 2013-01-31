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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Forwards({
		@Forward(
				name = "showEnrollmentInstructions",
				path = "/student/enrollment/bolonha/showEnrollmentInstructions.jsp",
				tileProperties = @Tile(title = "private.student.subscribe.groups")),

		@Forward(
				name = "enrollmentCannotProceed",
				path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp",
				tileProperties = @Tile(title = "private.student.subscribe.groups"))

})
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
