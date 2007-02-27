package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TeacherPersonalExpectationsDefinitionPeriodDA extends FenixDispatchAction {

    public ActionForward showPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ExecutionYear year = ExecutionYear.readCurrentExecutionYear();
	readAndSetPeriod(request, year);
	request.setAttribute("bean", new ExecutionYearBean(year));
	return mapping.findForward("showDefinitionPeriod");
    }
    
    public ActionForward showPeriodWithSelectedYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	IViewState viewState = RenderUtils.getViewState("executionYear");
	ExecutionYear year = (ExecutionYear) viewState.getMetaObject().getObject();		
	readAndSetPeriod(request, year);
	request.setAttribute("bean", new ExecutionYearBean(year));
	return mapping.findForward("showDefinitionPeriod");
    }
  
    public ActionForward showPeriodInExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ExecutionYear year = getExecutionYearFromParameter(request);
	readAndSetPeriod(request, year);
	request.setAttribute("bean", new ExecutionYearBean(year));
	return mapping.findForward("showDefinitionPeriod");
    }    
     
    public ActionForward createPeriod(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	ExecutionYear year = getExecutionYearFromParameter(request);		
	request.setAttribute("executionYear", year);
	request.setAttribute("department", getDepartment(request));
	return mapping.findForward("editDefinitionPeriod");
    }

    public ActionForward editPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionYear year = getExecutionYearFromParameter(request);
	readAndSetPeriod(request, year);	
	return mapping.findForward("editDefinitionPeriod");
    }
    
    // Private Methods

    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
	TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = 
	    getDepartment(request).getTeacherExpectationDefinitionPeriodForExecutionYear(year);
	request.setAttribute("period", teacherExpectationDefinitionPeriod);
    }
    
    protected ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
	final String executionYearIDString = request.getParameter("executionYearId");
	final Integer executionYearID = Integer.valueOf(executionYearIDString);
	return rootDomainObject.readExecutionYearByOID(executionYearID);
    }   
    
    protected Department getDepartment(HttpServletRequest request) {
	return getLoggedPerson(request).getEmployee().getCurrentDepartmentWorkingPlace();
    }
}
