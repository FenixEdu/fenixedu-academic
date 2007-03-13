package net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResultMessage;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public abstract class AbstractBolonhaStudentEnrollmentDA extends FenixDispatchAction {

    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod) {

	request.setAttribute("bolonhaStudentEnrollmentBean", new BolonhaStudentEnrollmentBean(
		studentCurricularPlan, executionPeriod, getCurricularYearForCurricularCourses(), getCurricularRuleLevel((DynaActionForm) form)));

	return mapping.findForward("showDegreeModulesToEnrol");

    }
    
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean) {

	request.setAttribute("bolonhaStudentEnrollmentBean", bolonhaStudentEnrollmentBean);
	request.setAttribute("action", getAction());

	return mapping.findForward("showDegreeModulesToEnrol");
    }


    public ActionForward enrolInDegreeModules(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();
	try {
	    final List<RuleResult> ruleResults = (List<RuleResult>) executeService(
		    "EnrolBolonhaStudent", getLoggedPerson(request), bolonhaStudentEnrollmentBean
			    .getStudentCurricularPlan(), bolonhaStudentEnrollmentBean
			    .getExecutionPeriod(), bolonhaStudentEnrollmentBean
			    .getDegreeModulesToEvaluate(), bolonhaStudentEnrollmentBean
			    .getCurriculumModulesToRemove(),
			    bolonhaStudentEnrollmentBean.getCurricularRuleLevel());

	    if (!bolonhaStudentEnrollmentBean.getDegreeModulesToEvaluate().isEmpty()
		    || !bolonhaStudentEnrollmentBean.getCurriculumModulesToRemove().isEmpty()) {
		addActionMessage("success", request, "label.save.success");
	    }

	    addRuleResultMessagesToActionMessages("warning", request, ruleResults);

	} catch (EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages("error", request, ex.getFalseRuleResults());
	    
	    return prepareShowDegreeModulesToEnrol(mapping, form, request, response, bolonhaStudentEnrollmentBean);

	} catch (DomainException ex) {
	    addActionMessage("error", request, ex.getKey(), ex.getArgs());
	    
	    return prepareShowDegreeModulesToEnrol(mapping, form, request, response, bolonhaStudentEnrollmentBean);
	}

	RenderUtils.invalidateViewState();

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		bolonhaStudentEnrollmentBean.getStudentCurricularPlan(), bolonhaStudentEnrollmentBean
			.getExecutionPeriod());
    }

    private void addRuleResultMessagesToActionMessages(final String propertyName,
	    final HttpServletRequest request, final List<RuleResult> ruleResults) {

	for (final RuleResult ruleResult : ruleResults) {
	    for (final RuleResultMessage message : ruleResult.getMessages()) {
		if (message.isToTranslate()) {
		    addActionMessage(propertyName, request, message.getMessage(), message.getArgs());
		} else {
		    addActionMessageLiteral(propertyName, request, message.getMessage());
		}
	    }
	}
    }

    protected BolonhaStudentEnrollmentBean getBolonhaStudentEnrollmentBeanFromViewState() {
	return (BolonhaStudentEnrollmentBean) getRenderedObject("bolonhaStudentEnrolments");
    }

    public ActionForward prepareChooseOptionalCurricularCourseToEnrol(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();
	request.setAttribute("optionalEnrolmentBean", new BolonhaStudentOptionalEnrollmentBean(
		bolonhaStudentEnrollmentBean.getStudentCurricularPlan(), bolonhaStudentEnrollmentBean
			.getExecutionPeriod(), bolonhaStudentEnrollmentBean
			.getOptionalDegreeModuleToEnrol()));

	return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
    }

    public ActionForward enrolInOptionalCurricularCourse(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final BolonhaStudentOptionalEnrollmentBean optionalStudentEnrollmentBean = getBolonhaStudentOptionalEnrollmentBeanFromViewState();
	try {
	    final List<RuleResult> ruleResults = (List<RuleResult>) executeService(
		    "EnrolBolonhaStudent", getLoggedPerson(request), optionalStudentEnrollmentBean
			    .getStudentCurricularPlan(), optionalStudentEnrollmentBean
			    .getExecutionPeriod(),
		    buildOptionalDegreeModuleToEnrolList(optionalStudentEnrollmentBean),
		    Collections.EMPTY_LIST, getCurricularRuleLevel(form));

	    addRuleResultMessagesToActionMessages("warning", request, ruleResults);

	} catch (EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages("error", request, ex.getFalseRuleResults());
	    request.setAttribute("optionalEnrolmentBean", optionalStudentEnrollmentBean);

	    return mapping.findForward("chooseOptionalCurricularCourseToEnrol");

	} catch (DomainException ex) {
	    addActionMessage("error", request, ex.getKey(), ex.getArgs());
	    request.setAttribute("optionalEnrolmentBean", optionalStudentEnrollmentBean);

	    return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
	}

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		optionalStudentEnrollmentBean.getStudentCurricularPlan(), optionalStudentEnrollmentBean
			.getExecutionPeriod());
    }

    private List<DegreeModuleToEnrol> buildOptionalDegreeModuleToEnrolList(
	    final BolonhaStudentOptionalEnrollmentBean optionalStudentEnrollmentBean) {
	final IDegreeModuleToEvaluate selectedDegreeModuleToEnrol = optionalStudentEnrollmentBean
		.getSelectedDegreeModuleToEnrol();
	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = new OptionalDegreeModuleToEnrol(
		selectedDegreeModuleToEnrol.getCurriculumGroup(), selectedDegreeModuleToEnrol
			.getContext(), optionalStudentEnrollmentBean.getExecutionPeriod(), optionalStudentEnrollmentBean
			.getSelectedOptionalCurricularCourse());

	final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	result.add(optionalDegreeModuleToEnrol);

	return result;
    }

    public ActionForward cancelChooseOptionalCurricularCourseToEnrol(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final BolonhaStudentOptionalEnrollmentBean bolonhaStudentOptionalEnrollmentBean = getBolonhaStudentOptionalEnrollmentBeanFromViewState();
	return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		bolonhaStudentOptionalEnrollmentBean.getStudentCurricularPlan(),
		bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());
    }

    protected BolonhaStudentOptionalEnrollmentBean getBolonhaStudentOptionalEnrollmentBeanFromViewState() {
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

    abstract public ActionForward prepare(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response);

    abstract protected int[] getCurricularYearForCurricularCourses();

    abstract protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm form);
    
    abstract protected String getAction();

}
