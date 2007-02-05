package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AutoEvaluationTeacherExpectationManagementAction extends FenixDispatchAction {

	public ActionForward showPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		 ExecutionYear year = getYear(request);
   
		 setAutoAvaliationPeriod(request,year);
		 request.setAttribute("bean", new ExecutionYearBean(year));
		 
		 return mapping.findForward("showDefinitionPeriod");
	}
	

	private ExecutionYear getYear(HttpServletRequest request) {
		 IViewState viewState = RenderUtils.getViewState("executionYear");
		 ExecutionYear year;
		 if(viewState!=null) {
			 year = (ExecutionYear) viewState.getMetaObject().getObject();
		 }
		 else {
			 String id = request.getParameter("executionYearId");
			 year = id!=null ? (ExecutionYear) RootDomainObject.readDomainObjectByOID(ExecutionYear.class,Integer.valueOf(id)) : ExecutionYear.readCurrentExecutionYear();
		 }
		 return year;
	}


	private void setAutoAvaliationPeriod(HttpServletRequest request, ExecutionYear year) {
		request.setAttribute("period", getDepartment(request).getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(year));
	}


	public ActionForward createPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String executionYearID = request.getParameter("executionYearId");
		if(executionYearID==null) {
			return showPeriod(mapping, form, request, response);
		}
		ExecutionYear year = (ExecutionYear) RootDomainObject.readDomainObjectByOID(ExecutionYear.class, Integer.valueOf(executionYearID));
		request.setAttribute("executionYear", year);
		request.setAttribute("department", getDepartment(request));
		return mapping.findForward("createDefinitionPeriod");
	}
	
	public ActionForward editPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String executionYearID = request.getParameter("executionYearId");
		if(executionYearID != null) {
			ExecutionYear executionYear = (ExecutionYear) RootDomainObject.readDomainObjectByOID(ExecutionYear.class, Integer.valueOf(executionYearID));
			setAutoAvaliationPeriod(request, executionYear);
		}
		return mapping.findForward("editDefinitionPeriod");
	}

	private Department getDepartment(HttpServletRequest request) {
		return getLoggedPerson(request).getEmployee().getCurrentDepartmentWorkingPlace();
	}

}
