package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.commons.student.StudentNumberBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class BolonhaEnrolmentsManagementDA extends AbstractBolonhaStudentEnrollmentDA {

    public ActionForward prepareSearchStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Object renderedObject = getRenderedObject("student-number-bean");
	if (renderedObject == null) {
	    request.setAttribute("studentNumberBean", new StudentNumberBean());
	} else {
	    final StudentNumberBean numberBean = (StudentNumberBean) renderedObject;
	    final Student student = getStudent(numberBean);
	    if (student != null) {
		RenderUtils.invalidateViewState();
		request.setAttribute("registrations", getAllRegistrations(student));
		request.setAttribute("studentNumberBean", numberBean);
	    }
	}

	return mapping.findForward("chooseStudentInformation");
    }

    public ActionForward showAllStudentCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	final StudentNumberBean studentNumberBean = new StudentNumberBean();
	final Student student = getStudent(request);
	studentNumberBean.setNumber(student.getNumber());

	request.setAttribute("studentNumberBean", studentNumberBean);
	request.setAttribute("registrations", getAllRegistrations(student));

	return mapping.findForward("chooseStudentInformation");
    }

    private Student getStudent(final HttpServletRequest request) {
	return rootDomainObject.readStudentByOID(getIntegerFromRequest(request, "studentId"));
    }

    private List<Registration> getAllRegistrations(final Student student) {
	final List<Registration> result = new ArrayList<Registration>();
	result.addAll(student.getRegistrations());
	result.addAll(student.getTransitionRegistrations());
	Collections.sort(result, new ReverseComparator(Registration.COMPARATOR_BY_START_DATE));
	return result;
    }

    private Student getStudent(final StudentNumberBean numberBean) {
	return numberBean.getNumber() != null ? Student.readStudentByNumber(numberBean.getNumber()) : null;
    }

    private StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getIntegerFromRequest(request, "scpId"));
    }
    
    public ActionForward prepareChooseExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	((DynaActionForm)form).set("scpId", getStudentCurricularPlan(request).getIdInternal());
	request.setAttribute("infoExecutionPeriod", new InfoExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod()));
	return mapping.findForward("showExecutionPeriodToEnrol");
    }

    public ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	InfoExecutionPeriod executionPeriodBean = (InfoExecutionPeriod) getRenderedObject("infoExecutionPeriod");
	
	request.setAttribute("bolonhaStudentEnrollmentBean", new BolonhaStudentEnrollmentBean(getStudentCurricularPlan(request),
		executionPeriodBean.getExecutionPeriod(), getCurricularYearForCurricularCourses(),
		getCurricularRuleLevel(form)));
	
	return mapping.findForward("showDegreeModulesToEnrol");
    }
    
    public ActionForward backToAllStudentCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final BolonhaStudentEnrollmentBean bean = (BolonhaStudentEnrollmentBean) getRenderedObject("bolonhaStudentEnrolments");
	request.setAttribute("studentId", bean.getStudentCurricularPlan().getRegistration().getStudent().getIdInternal());
	return showAllStudentCurricularPlans(mapping, form, request, response);
    }
    
    public ActionForward viewStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final DynaActionForm form = (DynaActionForm) actionForm;
	if (StringUtils.isEmpty(form.getString("detailed"))) {
	    form.set("detailed", Boolean.TRUE.toString());
	}
	request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
	
	return mapping.findForward("viewStudentCurriculum");
    }

    @Override
    public ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, BolonhaStudentEnrollmentBean bean) {

	request.setAttribute("bolonhaStudentEnrollmentBean", bean);
	return mapping.findForward("showDegreeModulesToEnrol");
    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
	return null; // all years
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
	return CurricularRuleLevel.ENROLMENT_NO_RULES;
    }

    @Override
    protected String getAction() {
	// unnecessary method
	return null;
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// unnecessary method
	return null;
    }

    public ActionForward prepareTransit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
	return mapping.findForward("transitToBolonha");
    }
    
    public ActionForward transitToBolonha(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	try {
	    executeService("TransitToBolonha", new Object[] { null,
		    studentCurricularPlan.getRegistration().getSourceRegistrationForTransition() });
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return prepareTransit(mapping, actionForm, request, response);
	}
	return showAllStudentCurricularPlans(mapping, actionForm, request, response);
    }
}
