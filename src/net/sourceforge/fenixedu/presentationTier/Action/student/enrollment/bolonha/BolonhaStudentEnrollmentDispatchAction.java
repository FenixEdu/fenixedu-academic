package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha;

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
import org.apache.struts.action.DynaActionForm;

public class BolonhaStudentEnrollmentDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final List<Registration> registrations = getActiveRegistrationsToEnrolByStudent(request);

	if (registrations.size() == 1) {
	    final Registration registration = registrations.iterator().next();
	    return prepareShowDegreeModulesToEnrol(mapping, form, request, response, registration
		    .getLastStudentCurricularPlan());
	}

	return prepareChooseRegistration(mapping, form, request, response);

    }

    public ActionForward prepareChooseRegistration(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registrationsToEnrol", getActiveRegistrationsToEnrolByStudent(request));

	return mapping.findForward("chooseRegistration");
    }

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Registration registration = getRegistration((DynaActionForm) form);
	if (!getLoggedPerson(request).getStudent().hasRegistrations(registration)) {
	    return mapping.findForward("notAuthorized");
	}

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response, registration
		.getLastStudentCurricularPlan());

    }

    private Registration getRegistration(DynaActionForm form) {
	return rootDomainObject.readRegistrationByOID((Integer) form.get("registrationId"));
    }

    private ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    final StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriod() == null) {
	    return mapping.findForward("showOutOfEnrollmentPeriodMessage");
	}

	request.setAttribute("bolonhaStudentEnrollmentBean", new BolonhaStudentEnrollmentBean(
		studentCurricularPlan, ExecutionPeriod.readActualExecutionPeriod()));

	return mapping.findForward("showDegreeModulesToEnrol");

    }

    private List<Registration> getActiveRegistrationsToEnrolByStudent(final HttpServletRequest request) {
	return getLoggedPerson(request).getStudent().getActiveRegistrationsToEnrolByStudent();
    }

    public ActionForward enrolInDegreeModules(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();

	try {
	    executeService("EnrolBolonhaStudent", getLoggedPerson(request), bolonhaStudentEnrollmentBean
		    .getStudentCurricularPlan(), bolonhaStudentEnrollmentBean.getExecutionPeriod(),
		    bolonhaStudentEnrollmentBean.getDegreeModulesToEnrol(), bolonhaStudentEnrollmentBean
			    .getCurriculumModulesToRemove());
	} catch (EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages(request, ex);

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

    private void addRuleResultMessagesToActionMessages(HttpServletRequest request,
	    EnrollmentDomainException ex) {
	for (final RuleResult ruleResult : ex.getFalseRuleResults()) {
	    for (final RuleResultMessage message : ruleResult.getMessages()) {
		addActionMessage(request, message.getMessage(), message.getArgs());
	    }
	}
    }

    private BolonhaStudentEnrollmentBean getBolonhaStudentEnrollmentBeanFromViewState() {
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
	    executeService("EnrolBolonhaStudent", getLoggedPerson(request),
		    optionalStudentEnrollmentBean.getStudentCurricularPlan(),
		    optionalStudentEnrollmentBean.getExecutionPeriod(),
		    buildOptionalDegreeModuleToEnrolList(optionalStudentEnrollmentBean),
		    Collections.EMPTY_LIST);
	} catch (EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages(request, ex);

	    request.setAttribute("optionalEnrolmentBean", optionalStudentEnrollmentBean);

	    return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("optionalEnrolmentBean", optionalStudentEnrollmentBean);

	    return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
	}

	return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		optionalStudentEnrollmentBean.getStudentCurricularPlan());
    }

    private List<DegreeModuleToEnrol> buildOptionalDegreeModuleToEnrolList(
	    final BolonhaStudentOptionalEnrollmentBean optionalStudentEnrollmentBean) {
	final DegreeModuleToEnrol selectedDegreeModuleToEnrol = optionalStudentEnrollmentBean
		.getSelectedDegreeModuleToEnrol();
	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = new OptionalDegreeModuleToEnrol(
		selectedDegreeModuleToEnrol.getCurriculumGroup(), selectedDegreeModuleToEnrol
			.getContext(), optionalStudentEnrollmentBean
			.getSelectedOptionalCurricularCourse());

	final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	result.add(optionalDegreeModuleToEnrol);

	return result;
    }

    public ActionForward cancelChooseOptionalCurricularCourseToEnrol(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
		getStudentCurricularPlan(request));
    }

    private StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getRequestParameterAsInteger(request,
		"studentCurricularPlanId"));
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
    
    public ActionForward showEnrollmentInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("showEnrollmentInstructions");
    }

}
