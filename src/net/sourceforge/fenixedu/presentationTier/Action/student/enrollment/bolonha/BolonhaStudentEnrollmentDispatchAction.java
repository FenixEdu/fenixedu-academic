package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BolonhaStudentEnrollmentDispatchAction extends AbstractBolonhaStudentEnrollmentDA {

    private static final int[] CURRICULAR_YEARS_FOR_CURRICULAR_COURSES = { 1 };

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Registration registration = (Registration) request.getAttribute("registration");

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response, registration
		.getLastStudentCurricularPlan(), ExecutionPeriod.readActualExecutionPeriod());

    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod) {

	if (studentCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriod() == null) {
	    final EnrolmentPeriod nextEnrollmentPeriod = studentCurricularPlan.getDegreeCurricularPlan()
		    .getNextEnrolmentPeriod();
	    if (nextEnrollmentPeriod != null) {
		addActionMessage(request, "message.out.curricular.course.enrolment.period",
			nextEnrollmentPeriod.getStartDateDateTime().toString(
				DateFormatUtil.DEFAULT_DATE_FORMAT), nextEnrollmentPeriod
				.getEndDateDateTime().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
	    } else {
		addActionMessage(request, "message.out.curricular.course.enrolment.period.default");
	    }

	    return mapping.findForward("enrollmentCannotProceed");
	}

	if (!studentCurricularPlan.getRegistration().getPayedTuition()) {
	    addActionMessage(request, "error.message.tuitionNotPayed");
	    
	    return mapping.findForward("enrollmentCannotProceed");
	}

	return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		studentCurricularPlan, executionPeriod);
    }

    public ActionForward showEnrollmentInstructions(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("showEnrollmentInstructions");
    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
	return CURRICULAR_YEARS_FOR_CURRICULAR_COURSES;
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm actionForm) {
	return CurricularRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT;
    }
    
    @Override
    protected String getAction() {
        return "";
    }

}
