package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ListTeachersPersonalExpectationsDA extends FenixDispatchAction {

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
	request.setAttribute("noEdit", true);
	request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
	return mapping.findForward("seeTeacherPersonalExpectationsByYear");
    }    
    
    protected ActionForward readAndSetList(ActionMapping mapping, HttpServletRequest request, ExecutionYear executionYear) {
	Department department = getDepartment(request);
	List<Teacher> allCurrentTeachers = department.getAllCurrentTeachers();		
	Map<Teacher, TeacherPersonalExpectation> result = new TreeMap<Teacher, TeacherPersonalExpectation>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
	
	for (Teacher teacher: allCurrentTeachers) {
	    TeacherPersonalExpectation teacherPersonalExpectation = teacher.getTeacherPersonalExpectationByExecutionYear(executionYear);
	    result.put(teacher, teacherPersonalExpectation);
	}
	
	request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
	request.setAttribute("teachersPersonalExpectations", result);	
	return mapping.findForward("listTeacherPersonalExpectations");
    }
    
    protected TeacherPersonalExpectation getTeacherPersonalExpectationFromParameter(final HttpServletRequest request) {
	final String teacherPersonalExpectationIDString = request.getParameter("teacherPersonalExpectationID");
	final Integer teacherPersonalExpectationID = Integer.valueOf(teacherPersonalExpectationIDString);
	return rootDomainObject.readTeacherPersonalExpectationByOID(teacherPersonalExpectationID);
    }   
    
    protected ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
	final String executionYearIDString = request.getParameter("executionYearId");
	final Integer executionYearID = Integer.valueOf(executionYearIDString);
	return rootDomainObject.readExecutionYearByOID(executionYearID);
    }   
        
    private Department getDepartment(HttpServletRequest request) {
	return getLoggedPerson(request).getEmployee().getCurrentDepartmentWorkingPlace();
    }
}
