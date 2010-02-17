package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class BolonhaStudentEnrollmentDispatchAction extends AbstractBolonhaStudentEnrollmentDA {

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final Registration registration = (Registration) request.getAttribute("registration");
	return prepareShowDegreeModulesToEnrol(mapping, form, request, response, registration.getLastStudentCurricularPlan(),
		ExecutionSemester.readActualExecutionSemester());
    }

    /*
     * Refactor this (similar code already exists in
     * StudentCurricularPlanEnrolment) ?
     */
    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {

	final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

	if (executionSemester.isFirstOfYear() && hasSpecialSeason(studentCurricularPlan, executionSemester)) {

	    if (!degreeCurricularPlan.hasOpenEnrolmentPeriodInCurricularCoursesSpecialSeason(executionSemester)) {
		addOutOfPeriodMessage(request, "message.out.curricular.course.enrolment.period.specialSeason",
			degreeCurricularPlan.getNextEnrolmentPeriodInCurricularCoursesSpecialSeason());
		return mapping.findForward("enrollmentCannotProceed");
	    }
	} else if (executionSemester.isFirstOfYear()
		&& studentCurricularPlan.getRegistration().hasFlunkedState(executionSemester.getExecutionYear())
		&& studentCurricularPlan.getRegistration().hasRegisteredActiveState()) {

	    if (!degreeCurricularPlan.hasOpenEnrolmentPeriodInCurricularCoursesSpecialSeason(executionSemester)) {
		addOutOfPeriodMessage(request, "message.out.curricular.course.enrolment.period.flunked.students",
			degreeCurricularPlan.getNextEnrolmentPeriodInCurricularCoursesSpecialSeason());
		return mapping.findForward("enrollmentCannotProceed");
	    }

	} else if (!degreeCurricularPlan.hasOpenEnrolmentPeriodInCurricularCoursesFor(executionSemester)) {
	    addOutOfPeriodMessage(request, "message.out.curricular.course.enrolment.period.normal", degreeCurricularPlan
		    .getNextEnrolmentPeriod());
	    return mapping.findForward("enrollmentCannotProceed");
	}

	if (hasAnyDebts(request, studentCurricularPlan)) {
	    return mapping.findForward("enrollmentCannotProceed");
	}

	return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response, studentCurricularPlan, executionSemester);
    }

    private boolean hasAnyDebts(HttpServletRequest request, StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.getRegistration().getStudent().isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt()) {
	    addActionMessage(request, "error.message.debts.from.past.years.not.payed");
	    return true;
	}

	if (studentCurricularPlan.getPerson().hasAnyResidencePaymentsInDebtForPreviousYear()) {
	    addActionMessage(request, "error.StudentCurricularPlan.cannot.enrol.with.residence.debts");
	    return true;
	}

	return false;
    }

    private boolean hasSpecialSeason(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {
	if (studentCurricularPlan.hasSpecialSeasonFor(executionSemester)) {
	    return true;
	}

	final Registration registration = studentCurricularPlan.getRegistration();

	return registration.hasSourceRegistration()
		&& registration.getSourceRegistration().getLastStudentCurricularPlan().hasSpecialSeasonFor(executionSemester);
    }

    private void addOutOfPeriodMessage(final HttpServletRequest request, final String message,
	    final EnrolmentPeriod nextEnrollmentPeriod) {
	if (nextEnrollmentPeriod != null) {
	    addActionMessage(request, message, nextEnrollmentPeriod.getStartDateDateTime().toString("dd/MM/yyyy HH:mm"),
		    nextEnrollmentPeriod.getEndDateDateTime().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
	} else {
	    addActionMessage(request, "message.out.curricular.course.enrolment.period.default");
	}
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
