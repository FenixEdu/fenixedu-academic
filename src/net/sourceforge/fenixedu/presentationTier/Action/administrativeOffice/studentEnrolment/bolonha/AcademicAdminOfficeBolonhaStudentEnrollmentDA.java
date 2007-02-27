package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class AcademicAdminOfficeBolonhaStudentEnrollmentDA extends AbstractBolonhaStudentEnrollmentDA {

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		getStudentCurricularPlan(request), getExecutionPeriod(request));

    }

    private StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getRequestParameterAsInteger(request,
		"scpID"));
    }

    private ExecutionPeriod getExecutionPeriod(final HttpServletRequest request) {
	return rootDomainObject.readExecutionPeriodByOID(getRequestParameterAsInteger(request,
		"executionPeriodID"));
    }

    private Boolean getWithRules(final ActionForm form) {
	return (Boolean) ((DynaActionForm) form).get("withRules");
    }

    public ActionForward backToStudentEnrollments(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();
	request.setAttribute("studentCurricularPlan", bolonhaStudentEnrollmentBean
		.getStudentCurricularPlan());
	request.setAttribute("executionPeriod", bolonhaStudentEnrollmentBean.getExecutionPeriod());

	return mapping.findForward("showStudentEnrollmentMenu");

    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
	return null;
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm form) {
	return getWithRules(form) ? CurricularRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT
		: CurricularRuleLevel.ENROLMENT_NO_RULES;
    }

}
