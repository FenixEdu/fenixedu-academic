package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment.specialSeason.ChangeSpecialSeasonCode;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.SpecialSeasonBolonhaStudentEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.SpecialSeasonCodeBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/specialSeasonBolonhaStudentEnrollment", module = "academicAdminOffice", formBean = "bolonhaStudentEnrollmentForm")
@Forwards( {
	@Forward(name = "showDegreeModulesToEnrol", path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
	@Forward(name = "showStudentEnrollmentMenu", path = "/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules"),
	@Forward(name = "changeSpecialSeasonCode", path = "/academicAdminOffice/student/enrollment/bolonha/chooseSpecialSeasonCode.jsp") })
public class AcademicAdminOfficeSpecialSeasonBolonhaStudentEnrolmentDA extends AcademicAdminOfficeBolonhaStudentEnrollmentDA {

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
	return CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT;
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
	request.setAttribute("action", getAction());
	request.setAttribute("bolonhaStudentEnrollmentBean", new SpecialSeasonBolonhaStudentEnrolmentBean(studentCurricularPlan,
		executionSemester));

	addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), request);

	return mapping.findForward("showDegreeModulesToEnrol");
    }

    private void addDebtsWarningMessages(final Student student, final HttpServletRequest request) {

	if (hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(student)) {
	    addActionMessage("warning", request, "registration.has.not.payed.insurance.fees");
	}

	if (hasAnyGratuityDebt(student)) {
	    addActionMessage("warning", request, "registration.has.not.payed.gratuities");
	}
    }

    private boolean hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(final Student student) {
	for (final Event event : student.getPerson().getEvents()) {
	    if ((event instanceof AdministrativeOfficeFeeAndInsuranceEvent || event instanceof InsuranceEvent) && event.isOpen()) {
		return true;
	    }
	}

	return false;
    }

    private boolean hasAnyGratuityDebt(final Student student) {
	for (final Registration registration : student.getRegistrations()) {
	    for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		for (final GratuityEvent gratuityEvent : studentCurricularPlan.getGratuityEvents()) {
		    if (gratuityEvent.isInDebt()) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    @Override
    protected String getAction() {
	return "/specialSeasonBolonhaStudentEnrollment.do";
    }

    public ActionForward changeSpecialSeasonCodePostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	SpecialSeasonCodeBean specialSeasonCodeBean = (SpecialSeasonCodeBean) RenderUtils.getViewState().getMetaObject()
		.getObject();
	RenderUtils.invalidateViewState();

	try {
	    ChangeSpecialSeasonCode.run(specialSeasonCodeBean.getStudentCurricularPlan().getRegistration(), specialSeasonCodeBean
		    .getExecutionPeriod().getExecutionYear(), specialSeasonCodeBean.getSpecialSeasonCode());
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    specialSeasonCodeBean.setSpecialSeasonCode(specialSeasonCodeBean.getStudentCurricularPlan().getRegistration()
		    .getSpecialSeasonCodeByExecutionYear(specialSeasonCodeBean.getExecutionPeriod().getExecutionYear()));
	}

	return prepareChangeSpecialSeasonCode(mapping, form, request, response, specialSeasonCodeBean);
    }

    public ActionForward changeSpecialSeasonCode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	ExecutionSemester executionSemester = getExecutionPeriod(request);
	SpecialSeasonCodeBean specialSeasonCodeBean = new SpecialSeasonCodeBean(studentCurricularPlan, executionSemester);
	specialSeasonCodeBean.setSpecialSeasonCode(studentCurricularPlan.getRegistration().getSpecialSeasonCodeByExecutionYear(
		executionSemester.getExecutionYear()));
	return prepareChangeSpecialSeasonCode(mapping, form, request, response, specialSeasonCodeBean);
    }

    private ActionForward prepareChangeSpecialSeasonCode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, SpecialSeasonCodeBean specialSeasonCodeBean) {
	request.setAttribute("specialSeasonCodeBean", specialSeasonCodeBean);
	return mapping.findForward("changeSpecialSeasonCode");
    }

    public ActionForward backFromChooseSpeciaSeasonCode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SpecialSeasonCodeBean seasonCodeBean = (SpecialSeasonCodeBean) getRenderedObject();

	request.setAttribute("studentCurricularPlan", seasonCodeBean.getStudentCurricularPlan());
	request.setAttribute("executionPeriod", seasonCodeBean.getExecutionPeriod());

	return mapping.findForward("showStudentEnrollmentMenu");
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	SpecialSeasonCodeBean seasonCodeBean = (SpecialSeasonCodeBean) getRenderedObject();
	return prepareShowDegreeModulesToEnrol(mapping, form, request, response, seasonCodeBean.getStudentCurricularPlan(),
		seasonCodeBean.getExecutionPeriod());
    }

}