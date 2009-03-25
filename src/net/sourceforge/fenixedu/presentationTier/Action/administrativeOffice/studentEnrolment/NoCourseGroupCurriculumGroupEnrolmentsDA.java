package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices.CreateExtraEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

abstract public class NoCourseGroupCurriculumGroupEnrolmentsDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("actionName", getActionName());
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final NoCourseGroupEnrolmentBean bean = createNoCourseGroupEnrolmentBean(getStudentCurricularPlan(request),
		getExecutionSemester(request));
	return showExtraEnrolments(bean, mapping, actionForm, request, response);
    }

    protected StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(Integer.valueOf(request.getParameter("scpID")));
    }

    protected ExecutionSemester getExecutionSemester(final HttpServletRequest request) {
	return rootDomainObject.readExecutionSemesterByOID(Integer.valueOf(request.getParameter("executionPeriodID")));
    }

    protected ActionForward showExtraEnrolments(NoCourseGroupEnrolmentBean bean, ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("enrolmentBean", bean);

	final NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = bean.getNoCourseGroupCurriculumGroup();
	if (noCourseGroupCurriculumGroup != null) {
	    bean.setCurriculumGroup(noCourseGroupCurriculumGroup);
	    if (noCourseGroupCurriculumGroup.hasAnyEnrolments()) {
		request.setAttribute("enrolments", noCourseGroupCurriculumGroup);
	    }
	}

	return mapping.findForward("showExtraEnrolments");
    }

    abstract protected NoCourseGroupEnrolmentBean createNoCourseGroupEnrolmentBean(
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester);

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("enrolmentBean", getNoCourseGroupEnrolmentBean());
	RenderUtils.invalidateViewState();
	return mapping.findForward("chooseExtraEnrolment");
    }

    protected NoCourseGroupEnrolmentBean getNoCourseGroupEnrolmentBean() {
	return (NoCourseGroupEnrolmentBean) getRenderedObject("enrolmentBean");
    }

    public ActionForward chooseCurricular(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("enrolmentBean", getNoCourseGroupEnrolmentBean());
	RenderUtils.invalidateViewState();
	return mapping.findForward("chooseExtraEnrolment");
    }

    public ActionForward enrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final NoCourseGroupCurriculumGroupType groupType = NoCourseGroupCurriculumGroupType.valueOf(request.getParameter("type"));
	final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	final ExecutionSemester executionPeriod = getExecutionSemester(request);
	final CurricularCourse curricularCourse = getOptionalCurricularCourse(request);

	try {
	    CreateExtraEnrolment.run(studentCurricularPlan, executionPeriod, curricularCourse, groupType);

	} catch (final IllegalDataAccessException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	    setExtraEnrolmentInformation(request, studentCurricularPlan, executionPeriod);
	    return mapping.findForward("chooseExtraEnrolment");

	} catch (final EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages("enrolmentError", request, ex.getFalseResult());
	    setExtraEnrolmentInformation(request, studentCurricularPlan, executionPeriod);
	    return mapping.findForward("chooseExtraEnrolment");

	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage(), e.getArgs());
	    setExtraEnrolmentInformation(request, studentCurricularPlan, executionPeriod);
	    return mapping.findForward("chooseExtraEnrolment");
	}

	final NoCourseGroupEnrolmentBean bean = createNoCourseGroupEnrolmentBean(studentCurricularPlan, executionPeriod);
	return showExtraEnrolments(bean, mapping, actionForm, request, response);
    }

    private void setExtraEnrolmentInformation(HttpServletRequest request, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionPeriod) {
	final NoCourseGroupEnrolmentBean enrolmentBean = createNoCourseGroupEnrolmentBean(studentCurricularPlan, executionPeriod);
	enrolmentBean.setDegreeType(DegreeType.valueOf(request.getParameter("degreeType")));
	enrolmentBean.setDegree(rootDomainObject.readDegreeByOID(Integer.valueOf(request.getParameter("degreeID"))));
	enrolmentBean.setDegreeCurricularPlan(rootDomainObject.readDegreeCurricularPlanByOID(Integer.valueOf(request
		.getParameter("dcpID"))));

	request.setAttribute("enrolmentBean", enrolmentBean);
    }

    private CurricularCourse getOptionalCurricularCourse(HttpServletRequest request) {
	return (CurricularCourse) rootDomainObject.readDegreeModuleByOID(Integer.valueOf(request.getParameter("optionalCCID")));
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Enrolment enrolment = getEnrolment(request);
	final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	final ExecutionSemester executionSemester = getExecutionSemester(request);

	try {
	    studentCurricularPlan.removeCurriculumModulesFromNoCourseGroupCurriculumGroup(Collections
		    .<CurriculumModule> singletonList(enrolment), executionSemester, getGroupType(), AccessControl.getPerson());

	} catch (final IllegalDataAccessException e) {
	    addActionMessage("error", request, "error.notAuthorized");

	} catch (EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages("e", request, ex.getFalseResult());

	} catch (DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	}

	return showExtraEnrolments(createNoCourseGroupEnrolmentBean(studentCurricularPlan, executionSemester), mapping,
		actionForm, request, response);
    }

    protected Enrolment getEnrolment(HttpServletRequest request) {
	return (Enrolment) rootDomainObject.readCurriculumModuleByOID(Integer.valueOf(request.getParameter("enrolment")));
    }

    public ActionForward back(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final NoCourseGroupEnrolmentBean bean = getNoCourseGroupEnrolmentBean();

	final StudentEnrolmentBean enrolmentBean = new StudentEnrolmentBean();
	enrolmentBean.setStudentCurricularPlan(bean.getStudentCurricularPlan());
	enrolmentBean.setExecutionPeriod(bean.getExecutionPeriod());
	request.setAttribute("studentEnrolmentBean", enrolmentBean);

	return mapping.findForward("showDegreeModulesToEnrol");
    }

    public ActionForward back2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return showExtraEnrolments(getNoCourseGroupEnrolmentBean(), mapping, actionForm, request, response);
    }

    abstract protected String getActionName();

    abstract protected NoCourseGroupCurriculumGroupType getGroupType();
}