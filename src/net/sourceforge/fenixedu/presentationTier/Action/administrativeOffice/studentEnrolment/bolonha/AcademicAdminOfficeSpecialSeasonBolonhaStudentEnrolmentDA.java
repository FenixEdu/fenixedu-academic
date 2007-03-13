package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.SpecialSeasonBolonhaStudentEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.SpecialSeasonCodeBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AcademicAdminOfficeSpecialSeasonBolonhaStudentEnrolmentDA extends
	AcademicAdminOfficeBolonhaStudentEnrollmentDA {

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
	return CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT;
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod) {
	request.setAttribute("action", getAction());
	request.setAttribute("bolonhaStudentEnrollmentBean", new SpecialSeasonBolonhaStudentEnrolmentBean(
		studentCurricularPlan, executionPeriod));

	return mapping.findForward("showDegreeModulesToEnrol");
    }
    
    @Override
    protected String getAction() {
        return "/specialSeasonBolonhaStudentEnrollment.do";
    }
    
    public ActionForward changeSpecialSeasonCodePostBack(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	SpecialSeasonCodeBean specialSeasonCodeBean = (SpecialSeasonCodeBean) RenderUtils
		.getViewState().getMetaObject().getObject();
	RenderUtils.invalidateViewState();

	try {
	    ServiceUtils.executeService(getUserView(request), "ChangeSpecialSeasonCode", new Object[] {
		specialSeasonCodeBean.getStudentCurricularPlan().getRegistration(),
		specialSeasonCodeBean.getExecutionPeriod().getExecutionYear(),
		specialSeasonCodeBean.getSpecialSeasonCode() });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    specialSeasonCodeBean.
		setSpecialSeasonCode(specialSeasonCodeBean.getStudentCurricularPlan().getRegistration().getSpecialSeasonCodeByExecutionYear(specialSeasonCodeBean.getExecutionPeriod().getExecutionYear()));
	}

	return prepareChangeSpecialSeasonCode(mapping, form, request, response, specialSeasonCodeBean);
    }
    
    public ActionForward changeSpecialSeasonCode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	ExecutionPeriod executionPeriod = getExecutionPeriod(request);
	SpecialSeasonCodeBean specialSeasonCodeBean = new SpecialSeasonCodeBean(studentCurricularPlan, executionPeriod);
	specialSeasonCodeBean.
	setSpecialSeasonCode(studentCurricularPlan.getRegistration().getSpecialSeasonCodeByExecutionYear(executionPeriod.getExecutionYear()));
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
	
	request.setAttribute("studentCurricularPlan", seasonCodeBean
		.getStudentCurricularPlan());
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
