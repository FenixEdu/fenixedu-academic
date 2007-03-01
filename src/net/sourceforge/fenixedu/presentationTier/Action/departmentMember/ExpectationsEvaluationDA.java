package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExpectationsEvaluationDA extends FenixDispatchAction {

    public ActionForward chooseTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	    HttpServletResponse response) {
	
	Teacher teacher = getLoggedTeacher(request);
	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	
	readAndSetEvaluatedTeachersWithExpectations(request, teacher, executionYear);
	
	return mapping.findForward("chooseTeacher");
    }
       
    public ActionForward chooseTeacherInSelectedExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	    HttpServletResponse response) {
	
	Teacher teacher = getLoggedTeacher(request);
	IViewState viewState = RenderUtils.getViewState("executionYear");
	ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();	
	
	readAndSetEvaluatedTeachersWithExpectations(request, teacher, executionYear);	
	
	return mapping.findForward("chooseTeacher");
    }
    
    public ActionForward chooseTeacherInExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	    HttpServletResponse response) {
	
	Teacher teacher = getLoggedTeacher(request);
	ExecutionYear executionYear = getExecutionYearFromParameter(request);	
	
	readAndSetEvaluatedTeachersWithExpectations(request, teacher, executionYear);	
	
	return mapping.findForward("chooseTeacher");
    }
    
    public ActionForward prepareEditExpectationEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	    HttpServletResponse response) {
		
	TeacherPersonalExpectation expectation = getTeacherPersonalExpectationFromParameter(request);	
	if(expectation != null) {
	    Teacher teacher = getLoggedTeacher(request);
	    ExecutionYear executionYear = expectation.getExecutionYear();
            if(teacher.hasExpectationEvaluatedTeacher(expectation.getTeacher(), executionYear)) {
        	request.setAttribute("teacherPersonalExpectation", expectation);
            }
	}
	return mapping.findForward("prepareEditEvaluation");
    }
    
    private TeacherPersonalExpectation getTeacherPersonalExpectationFromParameter(final HttpServletRequest request) {
	final String teacherPersonalExpectationIDString = request.getParameter("teacherPersonalExpectationID");
	final Integer teacherPersonalExpectationID = Integer.valueOf(teacherPersonalExpectationIDString);
	return rootDomainObject.readTeacherPersonalExpectationByOID(teacherPersonalExpectationID);
    } 
    
    private ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
	final String executionYearIDString = request.getParameter("executionYearID");
	final Integer executionYearID = Integer.valueOf(executionYearIDString);
	return rootDomainObject.readExecutionYearByOID(executionYearID);
    } 
        
    private void readAndSetEvaluatedTeachersWithExpectations(HttpServletRequest request, Teacher teacher, ExecutionYear executionYear) {
	Map<Teacher, TeacherPersonalExpectation> result = new TreeMap<Teacher, TeacherPersonalExpectation>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
	List<ExpectationEvaluationGroup> groups = teacher.getEvaluatedExpectationEvaluationGroups(executionYear);	
	for (ExpectationEvaluationGroup group : groups) {
	    TeacherPersonalExpectation evaluatedTeacherExpectation = group.getEvaluated().getTeacherPersonalExpectationByExecutionYear(executionYear);
	    result.put(group.getEvaluated(), evaluatedTeacherExpectation);	    
	}	
	request.setAttribute("evaluatedTeachers", result);
	request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
    }
    
    private Teacher getLoggedTeacher(HttpServletRequest request) {
	Person loggedPerson = getLoggedPerson(request);	
	return (loggedPerson != null) ? loggedPerson.getTeacher() : null;
    }
}
