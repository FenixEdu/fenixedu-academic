package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsVisualizationPeriod;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.ListTeachersPersonalExpectationsDA;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ListOtherTeachersPersonalExpectationsDA extends ListTeachersPersonalExpectationsDA {
    
    public ActionForward listTeachersPersonalExpectationsForSelectedExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("executionYear");
	ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();		
	return readAndSetList(mapping, request, executionYear);
    }
     
    public ActionForward listTeachersPersonalExpectationsByExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ExecutionYear executionYear = getExecutionYearFromParameter(request);			
	return readAndSetList(mapping, request, executionYear); 
    }
    
    public ActionForward listTeachersPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();	
	return readAndSetList(mapping, request, executionYear);
    }
    
    public ActionForward seeTeacherPersonalExpectation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	
	TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
	ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();
	Department department = getDepartment(request);	
	TeacherPersonalExpectationsVisualizationPeriod visualizationPeriod = 
	    department.getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(executionYear);
	
	request.setAttribute("noEdit", true);
	if(visualizationPeriod == null || !visualizationPeriod.isPeriodOpen()) {
	    throw new FenixActionException();
	} else {
	    return super.seeTeacherPersonalExpectation(mapping, form, request, response);
	}	
    }    
    
    private Department getDepartment(HttpServletRequest request) {
	return getLoggedPerson(request).getTeacher().getCurrentWorkingDepartment();
    }
    
    protected ActionForward readAndSetList(ActionMapping mapping, HttpServletRequest request, ExecutionYear executionYear) {
	Department department = getDepartment(request);	
	TeacherPersonalExpectationsVisualizationPeriod visualizationPeriod = 
	    department.getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(executionYear);
	
	if(visualizationPeriod == null || !visualizationPeriod.isPeriodOpen()) {
            request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));            
            return mapping.findForward("listTeacherPersonalExpectations");            
	} else {
	    return super.readAndSetList(mapping, request, executionYear);
	}
    }
}
