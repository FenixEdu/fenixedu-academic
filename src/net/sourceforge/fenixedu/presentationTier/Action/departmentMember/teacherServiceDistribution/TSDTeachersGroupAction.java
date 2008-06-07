package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class TSDTeachersGroupAction extends FenixDispatchAction {
			
	public ActionForward prepareForTSDTeachersGroupServices(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);		
		
		return listTSDTeachers(mapping, form, request, response);
	}
	
	public ActionForward listTSDTeachers(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;
	
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		
		List<TSDTeacher> tsdTeachersList = new ArrayList<TSDTeacher>(selectedTeacherServiceDistribution.getTSDTeachers());
		List<TSDCourse> tsdCoursesList = new ArrayList<TSDCourse>(selectedTeacherServiceDistribution.getTSDCompetenceAndVirtualCourses());
		Collections.sort(tsdTeachersList, new BeanComparator("name"));
		Collections.sort(tsdCoursesList, new BeanComparator("name"));
		
		request.setAttribute("tsdTeachersList", tsdTeachersList);
		request.setAttribute("tsdCoursesList", tsdCoursesList);		
		request.setAttribute("tsdProcess", selectedTeacherServiceDistribution.getTSDProcessPhase().getTSDProcess());
		
		return mapping.findForward("listTSDTeachers");
	}
	

	
	public ActionForward showDepartmentTeachers(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		TSDProcess distribution = selectedTeacherServiceDistribution.getTSDProcessPhase().getTSDProcess();
		Department selectedDepartment = getSelectedDepartment(dynaForm, distribution.getDepartment());
					
		List<Teacher> teachersList = selectedTeacherServiceDistribution.getDepartmentTeachersNotInGrouping(selectedDepartment);
						
		if(!teachersList.isEmpty()){
			Collections.sort(teachersList, new BeanComparator("person.name"));
			request.setAttribute("teachersList", teachersList);
		}
		
		List<Department> departmentList = new ArrayList<Department>(rootDomainObject.getDepartments());
		Collections.sort(departmentList, new BeanComparator("realName"));
		
		dynaForm.set("department", selectedDepartment.getIdInternal());
		request.setAttribute("departmentList", departmentList);
		request.setAttribute("tsdProcess", distribution);
		
		return mapping.findForward("showTeachersList");
	}
	
	public ActionForward showDepartmentCourses(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		TSDProcess distribution = selectedTeacherServiceDistribution.getTSDProcessPhase().getTSDProcess();
		Department selectedDepartment = getSelectedDepartment(dynaForm, distribution.getDepartment());
						
		List<CompetenceCourse> coursesList = selectedTeacherServiceDistribution.getDepartmentCompetenceCoursesNotInGrouping(selectedDepartment);
						
		if(!coursesList.isEmpty()){
			Collections.sort(coursesList, new BeanComparator("name"));
			request.setAttribute("coursesList", coursesList);
		}
		
		List<Department> departmentList = new ArrayList<Department>(rootDomainObject.getDepartments());
		Collections.sort(departmentList, new BeanComparator("realName"));
		
		dynaForm.set("department", selectedDepartment.getIdInternal());
		
		request.setAttribute("departmentList", departmentList);
		request.setAttribute("tsdProcess", distribution);
		
		return mapping.findForward("showCoursesList");
	}
		
	public ActionForward removeTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = UserView.getUser();
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TSDTeacher selectedTSDTeacher = getSelectedTSDTeacher(dynaForm); 
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
			
		ServiceUtils.executeService("RemoveTeacherFromTeacherServiceDistributions", 
				new Object[] { selectedTeacherServiceDistribution.getIdInternal(), selectedTSDTeacher.getIdInternal()});
		
		return listTSDTeachers(mapping, form, request, response);
	}
	
	
	public ActionForward removeCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = UserView.getUser();
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TSDCourse selectedCourse = getSelectedTSDCourse(dynaForm); 
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
			
		ServiceUtils.executeService("RemoveCourseFromTeacherServiceDistribution", 
				new Object[] { selectedTeacherServiceDistribution.getIdInternal(), selectedCourse.getIdInternal()});
		
		return listTSDTeachers(mapping, form, request, response);
	}
	
	
	public ActionForward addTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = UserView.getUser();
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		Teacher selectedTeacher = getSelectedTeacher(dynaForm); 
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
				
		ServiceUtils.executeService("AddTeacherToTeacherServiceDistribution", new Object[] { 
				selectedTeacherServiceDistribution.getIdInternal(), selectedTeacher.getIdInternal()});
				
		return listTSDTeachers(mapping, form, request, response);
	}
	
	public ActionForward addCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = UserView.getUser();
		DynaActionForm dynaForm = (DynaActionForm) form;
		 
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		CompetenceCourse selectedCourse = getSelectedCompetenceCourse(dynaForm);
				
		ServiceUtils.executeService("AddCourseToTeacherServiceDistribution", new Object[] { 
				selectedTeacherServiceDistribution.getIdInternal(), selectedCourse.getIdInternal()});
		
		return listTSDTeachers(mapping, form, request, response);
	}
	
	
	public ActionForward showFormToCreateTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
	
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		List<Category> categoriesList = new ArrayList<Category>(rootDomainObject.readAllDomainObjects(Category.class));
		Collections.sort(categoriesList, new BeanComparator("shortName"));
		request.setAttribute("categoriesList", categoriesList);
		
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);

		request.setAttribute("tsdProcess", selectedTeacherServiceDistribution.getTSDProcessPhase().getTSDProcess());
		
		return mapping.findForward("showFormToCreateTeacher");
	}
	
	public ActionForward showFormToCreateCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
	
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		//List<CurricularYear> curricularyearsList = new ArrayList<CurricularYear>(rootDomainObject.readAllDomainObjects(CurricularYear.class));
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		TSDProcess process = selectedTeacherServiceDistribution.getTSDProcessPhase().getTSDProcess();
		Department department = process.getDepartment();
		ExecutionYear year = process.getExecutionPeriods().get(0).getExecutionYear();
		
		List<DegreeCurricularPlan> departmentPlansList = new ArrayList<DegreeCurricularPlan>();
		
		for(DegreeCurricularPlan plan : DegreeCurricularPlan.readBolonhaDegreeCurricularPlans()){
			Degree degree = plan.getDegree();
			
			if(plan.getCurricularCoursesWithExecutionIn(year).size() > 0 && degree.getDepartmentsCount() > 0){
				if(degree.getDepartments().contains(department)){
					departmentPlansList.add(plan);
				}
			}
		}
				
		Collections.sort(departmentPlansList, new BeanComparator("name"));
		//Collections.sort(curricularyearsList, new BeanComparator("year"));
		
		request.setAttribute("curricularPlansList", departmentPlansList);
		request.setAttribute("shiftsList", ShiftType.values());
		request.setAttribute("tsdProcess", selectedTeacherServiceDistribution.getTSDProcessPhase().getTSDProcess());
		request.setAttribute("executionPeriodsList", selectedTeacherServiceDistribution.getTSDProcessPhase().
				getTSDProcess().getOrderedExecutionPeriods());
		
		
		return mapping.findForward("showFormToCreateCourse");
	}
		
	public ActionForward createTSDTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = UserView.getUser();		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		Category selectedCategory = getSelectedCategory(dynaForm);
		String teacherName = (String) dynaForm.get("name");
		Integer requiredHours =  Integer.parseInt((String) dynaForm.get("hours"));
		
		Object[] parameters = new Object[] {
			teacherName,
			selectedCategory.getIdInternal(),
			requiredHours,
			selectedTeacherServiceDistribution.getIdInternal()
		};
		
		Boolean teacherSuccessfullyCreated = (Boolean) ServiceUtils.executeService("CreateTSDTeacher", parameters);
						
		if(!teacherSuccessfullyCreated){
			request.setAttribute("creationFailure", true);
			return showFormToCreateTeacher(mapping, form, request, response);
		}

		return listTSDTeachers(mapping, form, request, response);
	}
	
	public ActionForward createTSDCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = UserView.getUser();		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		Integer executionPeriodId = (Integer) dynaForm.get("executionPeriod");
		String courseName = (String) dynaForm.get("name");
		String[] degreeCurricularPlansIdArray = (String[]) dynaForm.get("curricularPlansArray");
		String[] shiftTypesArray = (String[]) dynaForm.get("shiftTypesArray");
				
		Object[] parameters = new Object[] {
			courseName,
			selectedTeacherServiceDistribution.getIdInternal(),
			executionPeriodId,
			shiftTypesArray,
			degreeCurricularPlansIdArray
		};
		
		TSDCourse course = (TSDCourse) executeService("CreateTSDVirtualGroup", parameters);
		
		dynaForm.set("tsdCourse", course.getIdInternal());
						
		return listTSDTeachers(mapping, form, request, response);
	}
	
	public ActionForward loadTSDCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
				
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TSDCourse course = getSelectedTSDCourse(dynaForm);
		
		if(course != null){
			request.setAttribute("tsdVirtualCourse", course);
		}
						
		return showFormToCreateCourse(mapping, form, request, response);
	}

	private TeacherServiceDistribution getSelectedTeacherServiceDistribution(DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		Integer selectedTeacherServiceDistributionId = (Integer) dynaForm.get("tsd");
		TeacherServiceDistribution selectedTeacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(selectedTeacherServiceDistributionId);
		
		return selectedTeacherServiceDistribution;
	}

	private TeacherServiceDistribution getSelectedTeacherServiceDistribution(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
		Integer selectedTeacherServiceDistributionId = Integer.valueOf(request.getParameter("tsd"));
		TeacherServiceDistribution selectedTeacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(selectedTeacherServiceDistributionId);
		
		return selectedTeacherServiceDistribution;
	}
	
	private Integer getFromRequestAndSetOnFormTeacherServiceDistributionId(HttpServletRequest request, DynaActionForm dynaForm) {
		Integer tsdId = new Integer(request.getParameter("tsdID"));
		dynaForm.set("tsd", tsdId);
		return tsdId;
	}

	private Department getSelectedDepartment(DynaActionForm dynaForm, Department distributionDepartment) throws FenixServiceException, FenixFilterException {
		Integer selectedDepartmentId = (Integer) dynaForm.get("department");
		Department selectedDepartment = rootDomainObject.readDepartmentByOID(selectedDepartmentId);
		
		if(selectedDepartment == null) {
			return distributionDepartment;
		}
		
		return selectedDepartment;
	}

	private TSDTeacher getSelectedTSDTeacher(DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		Integer selectedTSDTeacherId = (Integer) dynaForm.get("tsdTeacher");
		TSDTeacher selectedTSDTeacher = rootDomainObject.readTSDTeacherByOID(selectedTSDTeacherId);
		
		return selectedTSDTeacher;
	}
	
	private Teacher getSelectedTeacher(DynaActionForm dynaForm) {
		Integer selectedTeacherId = (Integer) dynaForm.get("teacher");
		Teacher selectedTeacher = rootDomainObject.readTeacherByOID(selectedTeacherId);
		
		return selectedTeacher;
	}
	
	private TSDCourse getSelectedTSDCourse(DynaActionForm dynaForm) {
		Integer selectedCourseId = (Integer) dynaForm.get("tsdCourse");
		return rootDomainObject.readTSDCourseByOID(selectedCourseId);
	}
	
	private CompetenceCourse getSelectedCompetenceCourse(DynaActionForm dynaForm) {
		Integer selectedCourseId = (Integer) dynaForm.get("tsdCourse");
		CompetenceCourse course = rootDomainObject.readCompetenceCourseByOID(selectedCourseId);
		
		return course;
	}
	
		
	private Category getSelectedCategory(DynaActionForm dynaForm) {
		Integer selectedCategoryId = (Integer) dynaForm.get("category");
		
		Category selectedCategory = rootDomainObject.readCategoryByOID(selectedCategoryId);
		
		return selectedCategory;
	}
	
}
