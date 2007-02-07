package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResultMessage;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BolonhaStudentEnrollmentDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final List<Registration> registrations = getRegistrations(request);

	if (registrations.size() > 1) {
	    throw new RuntimeException("TODO: REDIRECT TO CHOOSE REGISTRATION");
	}

	final Registration registration = registrations.iterator().next();

	if (!registration.isActive()) {
	    throw new RuntimeException("TODO: FIX FOR INACTIVE REGISTRATIONS");
	}

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response, registration
		.getLastStudentCurricularPlan());

    }

    private ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    final StudentCurricularPlan studentCurricularPlan) {
	request.setAttribute("bolonhaStudentEnrollmentBean", new BolonhaStudentEnrollmentBean(
		studentCurricularPlan, ExecutionPeriod.readActualExecutionPeriod()));

	return mapping.findForward("showDegreeModulesToEnrol");
    }

    private List<Registration> getRegistrations(final HttpServletRequest request) {
	return getLoggedPerson(request).getStudent().getRegistrations();
    }

    public ActionForward enrolInDegreeModules(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();

	try {
	    executeService(request, "EnrolBolonhaStudent", new Object[] {
		    bolonhaStudentEnrollmentBean.getStudentCurricularPlan(),
		    bolonhaStudentEnrollmentBean.getExecutionPeriod(),
		    bolonhaStudentEnrollmentBean.getDegreeModulesToEnrol(),
		    bolonhaStudentEnrollmentBean.getCurriculumModulesToRemove() });
	} catch (EnrollmentDomainException ex) {
	    for (final RuleResult ruleResult : ex.getFalseRuleResults()) {
		for (final RuleResultMessage message : ruleResult.getMessages()) {
		    addActionMessage(request, message.getMessage(), message.getArgs());
		}
	    }

	    request.setAttribute("bolonhaStudentEnrollmentBean", bolonhaStudentEnrollmentBean);

	    return mapping.findForward("showDegreeModulesToEnrol");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("bolonhaStudentEnrollmentBean", bolonhaStudentEnrollmentBean);

	    return mapping.findForward("showDegreeModulesToEnrol");
	}

	RenderUtils.invalidateViewState();

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		bolonhaStudentEnrollmentBean.getStudentCurricularPlan());
    }

    private BolonhaStudentEnrollmentBean getBolonhaStudentEnrollmentBeanFromViewState() {
	return (BolonhaStudentEnrollmentBean) getRenderedObject("bolonhaStudentEnrolments");
    }

    public ActionForward prepareChooseOptionalCurricularCourseToEnrol(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();
	request.setAttribute("bolonhaStudentEnrollmentBean", bolonhaStudentEnrollmentBean);
	request.setAttribute("optionalEnrolmentBean", new BolonhaStudentOptionalEnrollmentBean(
		bolonhaStudentEnrollmentBean.getExecutionPeriod(), bolonhaStudentEnrollmentBean
			.getOptionalDegreeModuleToEnrol()));

	return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
    }

    public ActionForward chooseOptionalCurricularCourseToEnrol(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final BolonhaStudentOptionalEnrollmentBean optionalStudentEnrollmentBean = getBolonhaStudentOptionalEnrollmentBeanFromViewState();

	final DegreeModuleToEnrol selectedDegreeModuleToEnrol = optionalStudentEnrollmentBean
		.getSelectedDegreeModuleToEnrol();
	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = new OptionalDegreeModuleToEnrol(
		selectedDegreeModuleToEnrol.getCurriculumGroup(), selectedDegreeModuleToEnrol
			.getContext(), optionalStudentEnrollmentBean
			.getSelectedOptionalCurricularCourse());

	request.setAttribute("bolonhaStudentEnrollmentBean",
		getBolonhaStudentEnrollmentBeanFromViewState());

	return mapping.findForward("showDegreeModulesToEnrol");
    }

    public ActionForward cancelChooseOptionalCurricularCourseToEnrol(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("bolonhaStudentEnrollmentBean",
		getBolonhaStudentEnrollmentBeanFromViewState());

	return mapping.findForward("showDegreeModulesToEnrol");
    }

    private BolonhaStudentOptionalEnrollmentBean getBolonhaStudentOptionalEnrollmentBeanFromViewState() {
	return (BolonhaStudentOptionalEnrollmentBean) getRenderedObject("optionalEnrolment");
    }

    public ActionForward updateParametersToSearchOptionalCurricularCourses(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("bolonhaStudentEnrollmentBean",
		getBolonhaStudentEnrollmentBeanFromViewState());
	request.setAttribute("optionalEnrolmentBean",
		getBolonhaStudentOptionalEnrollmentBeanFromViewState());
	RenderUtils.invalidateViewState("optionalEnrolment");

	return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
    }

}
