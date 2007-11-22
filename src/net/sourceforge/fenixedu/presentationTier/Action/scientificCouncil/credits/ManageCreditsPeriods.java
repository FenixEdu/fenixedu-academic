package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacherCredits.TeacherCreditsPeriodBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageCreditsPeriods extends FenixDispatchAction {

    public ActionForward showPeriods(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	TeacherCreditsPeriodBean bean = (TeacherCreditsPeriodBean) getRenderedObject("teacherCreditsBeanID");
	if(bean == null) {
	    ExecutionPeriod executionPeriod = getExecutionPeriodToEditPeriod(request);
	    if(executionPeriod == null) {
		bean = new TeacherCreditsPeriodBean(ExecutionPeriod.readActualExecutionPeriod());	    
	    } else {
		bean = new TeacherCreditsPeriodBean(executionPeriod);
	    }
	} else {
	    bean.refreshDates();   
	}	
	
	request.setAttribute("teacherCreditsBean", bean);	
	return mapping.findForward("show-credits-periods");       
    }

    public ActionForward prepareEditTeacherCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ExecutionPeriod executionPeriod = getExecutionPeriodToEditPeriod(request);	
	request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionPeriod, true));
	return mapping.findForward("edit-teacher-credits-periods");        
    }

    public ActionForward prepareEditDepartmentAdmOfficeCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ExecutionPeriod executionPeriod = getExecutionPeriodToEditPeriod(request);	
	request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionPeriod, false));		
	return mapping.findForward("edit-teacher-credits-periods"); 
    }    
    
    public ActionForward editPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	TeacherCreditsPeriodBean bean = (TeacherCreditsPeriodBean) getRenderedObject("teacherCreditsBeanID");

	try{
	    executeService("CreateTeacherCreditsFillingPeriod", new Object[] {bean});
	
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("teacherCreditsBean", bean);
	    return mapping.findForward("edit-teacher-credits-periods"); 
	}
	
	request.setAttribute("teacherCreditsBean", bean);
	RenderUtils.invalidateViewState("teacherCreditsBeanID");
	return mapping.findForward("show-credits-periods");      
    }
    
    private ExecutionPeriod getExecutionPeriodToEditPeriod(HttpServletRequest request) {
	String parameter = request.getParameter("executionPeriodId");
	Integer executionPeriodId = parameter != null ? Integer.valueOf(parameter) : null;
	return rootDomainObject.readExecutionPeriodByOID(executionPeriodId);	
    }             
}
