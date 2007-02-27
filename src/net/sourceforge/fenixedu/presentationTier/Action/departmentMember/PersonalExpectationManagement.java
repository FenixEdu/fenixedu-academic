package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.TeacherPersonalExpectationBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PersonalExpectationManagement extends FenixDispatchAction {

    public ActionForward viewTeacherPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	    HttpServletResponse response) {
	
	Teacher teacher = getLoggedTeacher(request);	
	if(teacher != null) {
	    ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();	    	   
	    readAndSetTeacherPersonalExpectationByExecutionYear(request, teacher, executionYear);
	    request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
	}	
	return mapping.findForward("viewTeacherPersonalExpectations");	
    }   
             
    public ActionForward viewTeacherPersonalExpectationInSelectedExecutionYear(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) {

	IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationInSelectedExecutionYear");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        if(bean != null) {
            ExecutionYear executionYear = bean.getExecutionYear();
            Teacher teacher = bean.getTeacher();            	   
            readAndSetTeacherPersonalExpectationByExecutionYear(request, teacher, executionYear);
            request.setAttribute("teacherPersonalExpectationBean", bean);
        }	
	return mapping.findForward("viewTeacherPersonalExpectations");	
    }   
           
    public ActionForward prepareDefineTeacherPersonalExpection(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) {
	
	Teacher teacher = getLoggedTeacher(request);	
	if(teacher != null) {
	    ExecutionYear executionYear = getExecutionYearFromParameter(request);
	    if(teacher.getTeacherPersonalExpectationByExecutionYear(executionYear) == null) {
		request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));		
	    }
	}	
	return mapping.findForward("manageEducationExpectations");
    }
    
    public ActionForward prepareManageResearchAndDevelopment(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) {
	
        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithEducationMainFocus");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();	    
        request.setAttribute("teacherPersonalExpectationBean", bean);  	   	
	return mapping.findForward("manageResearchAndDevelopmentExpectations");
    }
    
    public ActionForward prepareManageUniversityServices(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) {
	
        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithOrientationMainFocus");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();	    
        request.setAttribute("teacherPersonalExpectationBean", bean);  	   	
	return mapping.findForward("manageUniversityServicesExpectations");
    }    
    
    public ActionForward prepareManageProfessionalActivities(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) {
	
        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithMainFocusUniversityServices");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();	    
        request.setAttribute("teacherPersonalExpectationBean", bean);  	   	
	return mapping.findForward("manageProfessionalActivitiesExpectations");
    }    
    
    public ActionForward createTeacherPersonalExpectations(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithMainFocusProfessionalActivities");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();	            
        TeacherPersonalExpectation teacherPersonalExpectation = null;
        
        try {
           teacherPersonalExpectation = (TeacherPersonalExpectation) executeService("InsertTeacherPersonalExpectation", new Object[] {bean});
           
        } catch(DomainException exception) {            
            saveMessages(request, exception);            
        }        
                
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
	request.setAttribute("teacherPersonalExpectationBean", bean);        
	return mapping.findForward("viewTeacherPersonalExpectations");
    }    
    
    public ActionForward prepareEditEducationExpectations(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, FenixActionException {
	
	TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
	checkPeriodToEdit(request, teacherPersonalExpectation);
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);	
	return mapping.findForward("manageEducationExpectations");
    }
    
    public ActionForward editEducationExpectations (ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithEducationMainFocus", mapping);
    }
    
    public ActionForward prepareEditResearchAndDevelopmentExpectations(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, FenixActionException {
	
	TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);	
	checkPeriodToEdit(request, teacherPersonalExpectation);	
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);	
	return mapping.findForward("manageResearchAndDevelopmentExpectations");
    }
       
    public ActionForward editResearchAndDevelopmentExpectations (ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithOrientationMainFocus", mapping);
    }
    
    public ActionForward prepareEditUniversityServicesExpectations(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, FenixActionException {
	
	TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
	checkPeriodToEdit(request, teacherPersonalExpectation);
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);	
	return mapping.findForward("manageUniversityServicesExpectations");
    }
    
    public ActionForward editUniversityServicesExpectations (ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithMainFocusUniversityServices", mapping);
    }
    
    public ActionForward prepareEditProfessionalActivitiesExpectations(ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, FenixActionException {
	
	TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
	checkPeriodToEdit(request, teacherPersonalExpectation);
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);	
	return mapping.findForward("manageProfessionalActivitiesExpectations");
    }
    
    public ActionForward editProfessionalActivitiesExpectations (ActionMapping mapping, ActionForm form, 
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithMainFocusProfessionalActivities", mapping);
    }
    
    // Private Methods
    
    private ActionForward viewTeacherPersonalExpectation(HttpServletRequest request, String viewStateName, ActionMapping mapping) {
	IViewState viewState = RenderUtils.getViewState(viewStateName);
        TeacherPersonalExpectation teacherPersonalExpectation = (TeacherPersonalExpectation) viewState.getMetaObject().getObject();        
        Teacher teacher = teacherPersonalExpectation.getTeacher();
        ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();       
        
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
	request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
	
	return mapping.findForward("viewTeacherPersonalExpectations");	
    }
         
    private void readAndSetTeacherPersonalExpectationByExecutionYear(HttpServletRequest request, Teacher teacher, ExecutionYear executionYear) {	
	TeacherPersonalExpectation teacherPersonalExpectation = teacher.getTeacherPersonalExpectationByExecutionYear(executionYear);	    	
	Department department = teacher.getCurrentWorkingDepartment();
	TeacherExpectationDefinitionPeriod period = department.getTeacherExpectationDefinitionPeriodForExecutionYear(executionYear);
	request.setAttribute("periodOpen", period != null ? period.isPeriodOpen().booleanValue() : false);
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
    }  
    
    private Teacher getLoggedTeacher(HttpServletRequest request) {
	Person loggedPerson = getLoggedPerson(request);	
	return (loggedPerson != null) ? loggedPerson.getTeacher() : null;
    }
    
    private ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
	final String executionYearIDString = request.getParameter("executionYearID");
	final Integer executionYearID = Integer.valueOf(executionYearIDString);
	return rootDomainObject.readExecutionYearByOID(executionYearID);
    } 
    
    private TeacherPersonalExpectation getTeacherPersonalExpectationFromParameter(final HttpServletRequest request) {
	final String teacherPersonalExpectationIDString = request.getParameter("teacherPersonalExpectationID");
	final Integer teacherPersonalExpectationID = Integer.valueOf(teacherPersonalExpectationIDString);
	return rootDomainObject.readTeacherPersonalExpectationByOID(teacherPersonalExpectationID);
    } 
               
    private void saveMessages(HttpServletRequest request, DomainException e) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
	saveMessages(request, actionMessages);
    }
    
    private Department getDepartment(HttpServletRequest request) {
	return getLoggedPerson(request).getTeacher().getCurrentWorkingDepartment();
    }
    
    private void checkPeriodToEdit(HttpServletRequest request, TeacherPersonalExpectation teacherPersonalExpectation) throws FenixActionException {
	ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();
	Department department = getDepartment(request);	
	TeacherExpectationDefinitionPeriod period = department.getTeacherExpectationDefinitionPeriodForExecutionYear(executionYear);
	if(period == null || !period.isPeriodOpen()) {
	   throw new FenixActionException();
	}
    }
}
