package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AutoEvaluationTeacherExpectationAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String executionYearID = request.getParameter("executionYearId");
	if (executionYearID != null) {
	    ExecutionYear executionYear = (ExecutionYear) RootDomainObject.readDomainObjectByOID(ExecutionYear.class, Integer.valueOf(executionYearID));
	    request.setAttribute("expectation", getTeacherExpectationForGivenYearInRequest(request, executionYear));
	}
	return mapping.findForward("editAutoEvaluation");
    }

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ExecutionYear year = getYear(request);
	request.setAttribute("expectation", getTeacherExpectationForGivenYearInRequest(request, year));
	request.setAttribute("bean", new ExecutionYearBean(year));	
	return mapping.findForward("showAutoEvaluation");
    }

    private TeacherPersonalExpectation getTeacherExpectationForGivenYearInRequest(HttpServletRequest request, ExecutionYear year) {	
	Person person = getLoggedPerson(request);
	return person.getTeacher().getTeacherPersonalExpectationByExecutionYear(year);
    }
    
    private ExecutionYear getYear(HttpServletRequest request) {
	
	IViewState viewState = RenderUtils.getViewState("executionYear");
	ExecutionYear year;
	if (viewState != null) {
	    year = (ExecutionYear) viewState.getMetaObject().getObject();
	} else {
	    String id = request.getParameter("executionYearId");
	    year = id != null ? (ExecutionYear) RootDomainObject.readDomainObjectByOID(
		    ExecutionYear.class, Integer.valueOf(id)) : ExecutionYear.readCurrentExecutionYear();
	}
	return year;
    }
}
