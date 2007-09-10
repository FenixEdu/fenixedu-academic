package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.delegate.DelegateCurricularCourseBean;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewStudentsDispatchAction extends FenixDispatchAction {

	@Override
	protected Object getFromRequest(HttpServletRequest request, String id) {
		if(RenderUtils.getViewState(id) != null)
			return RenderUtils.getViewState(id).getMetaObject().getObject();
		else if(request.getParameter(id) != null)
			return request.getParameter(id);
		else
			return request.getAttribute(id);
	}
	
	public ActionForward showStudents(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final Person person = getLoggedPerson(request);
		
		List<Student> students = new ArrayList<Student>();
		if(person.hasStudent()) {
			final Student student = person.getStudent();
			final Degree degree = student.getLastActiveRegistration().getDegree();
			final PersonFunction yearDelegateFunction = degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(student, 
					FunctionType.DELEGATE_OF_YEAR);
			
			if(yearDelegateFunction != null) {
				final ExecutionYear delegateExecutionYear = ExecutionYear.getExecutionYearByDate(yearDelegateFunction.getBeginDate());
				
				students.addAll(person.getStudent().getStudentsResponsibleForGivenFunctionType(FunctionType.DELEGATE_OF_YEAR,
						delegateExecutionYear));
				
				Collections.sort(students,Student.NUMBER_COMPARATOR);
			}
		}
		request.setAttribute("studentsList", students);
		request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());
		return mapping.findForward("showStudents");
	}
	
	public ActionForward prepareShowStudentsByCurricularCourse(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {		
		final Person person = getLoggedPerson(request);

		request.setAttribute("curricularCoursesList", getCurricularCourses(person));
		request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());
		return mapping.findForward("showStudents");
	}
	
	public ActionForward showStudentsByCurricularCourse(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final Person person = getLoggedPerson(request);
		final PersonFunction delegateFunction = getPersonFunction(person);
			
		if(delegateFunction != null) {
			final Integer curricularCourseID = Integer.parseInt(request.getParameter("curricularCourseID"));
			final CurricularCourse curricularCourse = (CurricularCourse)rootDomainObject.readDegreeModuleByOID(curricularCourseID);
			final Integer curricularYear = Integer.parseInt(request.getParameter("curricularYear"));
			final Integer executionPeriodOID = Integer.parseInt(request.getParameter("executionPeriodOID"));
			final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodOID);
			final ExecutionYear executionYear = ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate());
			
			DelegateCurricularCourseBean bean = new DelegateCurricularCourseBean(curricularCourse, executionYear, curricularYear, executionPeriod);
			bean.calculateEnrolledStudents();
			request.setAttribute("selectedCurricularCourseBean", bean);
		}
		
		request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());
		return mapping.findForward("showStudents");
	}
	
	/* AUXILIARY METHODS */
	
	private PersonFunction getPersonFunction(Person person) {
		if(person.hasStudent()) {
			final Student student = person.getStudent(); 
			final Degree degree = student.getLastActiveRegistration().getDegree();
			return degree.getMostSignificantDelegateFunctionForStudent(student);
		}
		else {
			return person.getActiveGGAEDelegatePersonFunction();	
		}
	}
	
	private Set<CurricularCourse> getDegreesCurricularCoursesFromCoordinatorRoles(List<Coordinator> coordinators, ExecutionYear executionYear) {
		Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
		for(Coordinator coordinator : coordinators) {
			final Degree degree = coordinator.getExecutionDegree().getDegree();
			for(CurricularCourse curricularCourse : degree.getAllCurricularCourses(executionYear)) {
				if(!curricularCourses.contains(curricularCourse)) {
					curricularCourses.add(curricularCourse);
				}
			}
			
		}
		return curricularCourses;
	}
	
	private List<DelegateCurricularCourseBean> getCurricularCourses(final Person person) {
		List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();
		
		final PersonFunction delegateFunction = getPersonFunction(person);
		if(delegateFunction != null) {
			if(person.hasStudent()) {
				Set<CurricularCourse> curricularCourses = person.getStudent().getCurricularCoursesResponsibleForByFunctionType(
						delegateFunction.getFunction().getFunctionType());
				return getCurricularCoursesBeans(delegateFunction, curricularCourses);
			}
			else if (person.hasAnyCoordinators()){
				Set<CurricularCourse> curricularCourses = getDegreesCurricularCoursesFromCoordinatorRoles(person.getCoordinators(),
						ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()));
				return getCurricularCoursesBeans(delegateFunction, curricularCourses);
			}
		}
		return result;
	}
	
	private List<DelegateCurricularCourseBean> getCurricularCoursesBeans(PersonFunction delegateFunction, Set<CurricularCourse> curricularCourses) {
		final FunctionType delegateFunctionType = delegateFunction.getFunction().getFunctionType();
		final ExecutionYear executionYear = ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate());
		
		List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();

		for (CurricularCourse curricularCourse : curricularCourses) {
			for(ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
				if(curricularCourse.hasAnyExecutionCourseIn(executionPeriod)) {
					for(DegreeModuleScope scope : curricularCourse.getDegreeModuleScopes()) {
						if(!scope.getCurricularSemester().equals(executionPeriod.getSemester())) {
							continue;
						}
							
						if(delegateFunctionType.equals(FunctionType.DELEGATE_OF_YEAR) && !scopeBelongsToDelegateCurricularYear(scope,
								delegateFunction.getCurricularYear().getYear())) {
							continue;
						}
						DelegateCurricularCourseBean bean = new DelegateCurricularCourseBean(curricularCourse, executionYear,
								scope.getCurricularYear(), executionPeriod);
						bean.calculateEnrolledStudents();
						result.add(bean);
					}
				}
				
				
			}
		}
		Collections.sort(result, DelegateCurricularCourseBean.CURRICULAR_COURSE_COMPARATOR_BY_CURRICULAR_YEAR_AND_CURRICULAR_SEMESTER);
		
		return result;
	}
	
	private boolean scopeBelongsToDelegateCurricularYear (DegreeModuleScope scope, Integer curricularYear) {
		if(scope.getCurricularYear().equals(curricularYear)) {
			return true;
		}
		return false;
	}
}
