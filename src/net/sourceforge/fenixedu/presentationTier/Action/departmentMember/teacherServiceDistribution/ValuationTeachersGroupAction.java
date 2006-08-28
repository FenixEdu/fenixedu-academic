package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ValuationTeachersGroupAction extends FenixDispatchAction {
			
	public ActionForward prepareForValuationTeachersGroupServices(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		getFromRequestAndSetOnFormValuationGroupingId(request, dynaForm);		
		
		return listValuationTeachers(mapping, form, request, response);
	}
	
	public ActionForward listValuationTeachers(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;
	
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
		
		List<ValuationTeacher> valuationTeachersList = new ArrayList<ValuationTeacher>(selectedValuationGrouping.getValuationTeachers());
		List<ValuationCompetenceCourse> valuationCoursesList = new ArrayList<ValuationCompetenceCourse>(selectedValuationGrouping.getValuationCompetenceCourses());
		Collections.sort(valuationTeachersList, new BeanComparator("name"));
		Collections.sort(valuationCoursesList, new BeanComparator("name"));
		
		request.setAttribute("valuationTeachersList", valuationTeachersList);
		request.setAttribute("valuationCoursesList", valuationCoursesList);		
		request.setAttribute("teacherServiceDistribution", selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution());
		
		return mapping.findForward("listValuationTeachers");
	}
	

	
	public ActionForward showDepartmentTeachers(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
		TeacherServiceDistribution distribution = selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution();
		Department selectedDepartment = getSelectedDepartment(dynaForm, distribution.getDepartment());
					
		List<Teacher> teachersList = selectedValuationGrouping.getDepartmentTeachersNotInGrouping(selectedDepartment);
						
		if(!teachersList.isEmpty()){
			Collections.sort(teachersList, new BeanComparator("person.nome"));
			request.setAttribute("teachersList", teachersList);
		}
		
		List<Department> departmentList = new ArrayList<Department>(rootDomainObject.getDepartments());
		Collections.sort(departmentList, new BeanComparator("realName"));
		
		dynaForm.set("department", selectedDepartment.getIdInternal());
		request.setAttribute("departmentList", departmentList);
		request.setAttribute("teacherServiceDistribution", distribution);
		
		return mapping.findForward("showTeachersList");
	}
	
	public ActionForward showDepartmentCourses(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
		TeacherServiceDistribution distribution = selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution();
		Department selectedDepartment = getSelectedDepartment(dynaForm, distribution.getDepartment());
						
		List<CompetenceCourse> coursesList = selectedValuationGrouping.getDepartmentCompetenceCoursesNotInGrouping(selectedDepartment);
						
		if(!coursesList.isEmpty()){
			Collections.sort(coursesList, new BeanComparator("name"));
			request.setAttribute("coursesList", coursesList);
		}
		
		List<Department> departmentList = new ArrayList<Department>(rootDomainObject.getDepartments());
		Collections.sort(departmentList, new BeanComparator("realName"));
		
		dynaForm.set("department", selectedDepartment.getIdInternal());
		
		request.setAttribute("departmentList", departmentList);
		request.setAttribute("teacherServiceDistribution", distribution);
		
		return mapping.findForward("showCoursesList");
	}
		
	public ActionForward removeTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationTeacher selectedValuationTeacher = getSelectedValuationTeacher(dynaForm); 
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
			
		ServiceUtils.executeService(userView, "RemoveTeacherFromValuationGroupings", 
				new Object[] { selectedValuationGrouping.getIdInternal(), selectedValuationTeacher.getIdInternal()});
		
		return listValuationTeachers(mapping, form, request, response);
	}
	
	
	public ActionForward removeCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationCompetenceCourse selectedCourse = getSelectedValuationCompetenceCourse(dynaForm); 
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
			
		ServiceUtils.executeService(userView, "RemoveCourseFromValuationGrouping", 
				new Object[] { selectedValuationGrouping.getIdInternal(), selectedCourse.getIdInternal()});
		
		return listValuationTeachers(mapping, form, request, response);
	}
	
	
	public ActionForward addTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		Teacher selectedTeacher = getSelectedTeacher(dynaForm); 
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
				
		ServiceUtils.executeService(userView, "AddTeacherToValuationGrouping", new Object[] { 
				selectedValuationGrouping.getIdInternal(), selectedTeacher.getIdInternal()});
				
		return listValuationTeachers(mapping, form, request, response);
	}
	
	public ActionForward addCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		 
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
		CompetenceCourse selectedCourse = getSelectedCompetenceCourse(dynaForm);
				
		ServiceUtils.executeService(userView, "AddCourseToValuationGrouping", new Object[] { 
				selectedValuationGrouping.getIdInternal(), selectedCourse.getIdInternal()});
		
		return listValuationTeachers(mapping, form, request, response);
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
		
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);

		request.setAttribute("teacherServiceDistribution", selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution());
		
		return mapping.findForward("showFormToCreateTeacher");
	}
	
	public ActionForward showFormToCreateCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
	
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		List<DegreeCurricularPlan> curricularPlansList = DegreeCurricularPlan.readByCurricularStage(CurricularStage.APPROVED); 
		List<CurricularYear> curricularyearsList = new ArrayList<CurricularYear>(rootDomainObject.readAllDomainObjects(CurricularYear.class));
		
		Collections.sort(curricularPlansList, new BeanComparator("name"));
		Collections.sort(curricularyearsList, new BeanComparator("year"));
		
		request.setAttribute("curricularPlansList", curricularPlansList);
		request.setAttribute("curricularYearsList", curricularyearsList);
		
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);

		request.setAttribute("teacherServiceDistribution", selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution());
		request.setAttribute("competenceCoursesList", selectedValuationGrouping.getGhostValuationCompetenceCourses());
		request.setAttribute("executionPeriodsList", selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution().getOrderedExecutionPeriods());
		
		
		return mapping.findForward("showFormToCreateCourse");
	}
	
	public ActionForward createValuationTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
		Category selectedCategory = getSelectedCategory(dynaForm);
		String teacherName = (String) dynaForm.get("name");
		Integer requiredHours =  Integer.parseInt((String) dynaForm.get("hours"));
		
		Object[] parameters = new Object[] {
			teacherName,
			selectedCategory.getIdInternal(),
			requiredHours,
			selectedValuationGrouping.getIdInternal()
		};
		
		Boolean teacherSuccessfullyCreated = (Boolean) ServiceUtils.executeService(userView, "CreateValuationTeacher", parameters);
						
		if(!teacherSuccessfullyCreated){
			request.setAttribute("creationFailure", true);
			return showFormToCreateTeacher(mapping, form, request, response);
		}

		return listValuationTeachers(mapping, form, request, response);
	}
	
	public ActionForward createValuationCompetenceCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(dynaForm);
		String courseName = (String) dynaForm.get("name");
				
		Object[] parameters = new Object[] {
			courseName,
			selectedValuationGrouping.getIdInternal()
		};
		
		ValuationCompetenceCourse valuationCompetenceCourse = (ValuationCompetenceCourse) ServiceUtils.executeService(
				userView, "CreateValuationCompetenceCourse", parameters);
		
		dynaForm.set("valuationCourse", valuationCompetenceCourse.getIdInternal());
						
		return loadValuationCompetenceCourse(mapping, form, request, response);
	}

	private ValuationGrouping getSelectedValuationGrouping(DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		Integer selectedValuationGroupingId = (Integer) dynaForm.get("valuationGrouping");
		ValuationGrouping selectedValuationGrouping = rootDomainObject.readValuationGroupingByOID(selectedValuationGroupingId);
		
		return selectedValuationGrouping;
	}

	private Integer getFromRequestAndSetOnFormValuationGroupingId(HttpServletRequest request, DynaActionForm dynaForm) {
		Integer valuationGroupingId = new Integer(request.getParameter("valuationGroupingID"));
		dynaForm.set("valuationGrouping", valuationGroupingId);
		return valuationGroupingId;
	}

	private Department getSelectedDepartment(DynaActionForm dynaForm, Department distributionDepartment) throws FenixServiceException, FenixFilterException {
		Integer selectedDepartmentId = (Integer) dynaForm.get("department");
		Department selectedDepartment = rootDomainObject.readDepartmentByOID(selectedDepartmentId);
		
		if(selectedDepartment == null) {
			return distributionDepartment;
		}
		
		return selectedDepartment;
	}

	private ValuationTeacher getSelectedValuationTeacher(DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		Integer selectedValuationTeacherId = (Integer) dynaForm.get("valuationTeacher");
		ValuationTeacher selectedValuationTeacher = rootDomainObject.readValuationTeacherByOID(selectedValuationTeacherId);
		
		return selectedValuationTeacher;
	}
	
	private Teacher getSelectedTeacher(DynaActionForm dynaForm) {
		Integer selectedTeacherId = (Integer) dynaForm.get("teacher");
		Teacher selectedTeacher = rootDomainObject.readTeacherByOID(selectedTeacherId);
		
		return selectedTeacher;
	}
	
	private CompetenceCourse getSelectedCompetenceCourse(DynaActionForm dynaForm) {
		Integer selectedCourseId = (Integer) dynaForm.get("valuationCourse");
		CompetenceCourse course = rootDomainObject.readCompetenceCourseByOID(selectedCourseId);
		
		return course;
	}
	
	private ValuationCompetenceCourse getSelectedValuationCompetenceCourse(DynaActionForm dynaForm) {
		Integer selectedCourseId = (Integer) dynaForm.get("valuationCourse");
		ValuationCompetenceCourse course = rootDomainObject.readValuationCompetenceCourseByOID(selectedCourseId);
		
		return course;
	}
	
	private Category getSelectedCategory(DynaActionForm dynaForm) {
		Integer selectedCategoryId = (Integer) dynaForm.get("category");
		
		Category selectedCategory = rootDomainObject.readCategoryByOID(selectedCategoryId);
		
		return selectedCategory;
	}
	
	public ActionForward loadValuationCompetenceCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
				
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationCompetenceCourse valuationCompetenceCourse = getSelectedValuationCompetenceCourse(dynaForm);
		
		if(valuationCompetenceCourse != null){
			if(valuationCompetenceCourse.getAssociatedValuationCurricularCourses().size() > 0){
				request.setAttribute("curricularCoursesList", valuationCompetenceCourse.getAssociatedValuationCurricularCourses());
			}
			request.setAttribute("competenceCourseName", valuationCompetenceCourse.getName());
		}
						
		return showFormToCreateCourse(mapping, form, request, response);
	}
	
	
	public ActionForward addValuationCurricularCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
				
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationCompetenceCourse valuationCompetenceCourse = getSelectedValuationCompetenceCourse(dynaForm);
		String courseAcronym = (String) dynaForm.get("courseName");
		Double theoreticalHours = Double.parseDouble((String) dynaForm.get("theoreticalHours"));
		Double praticalHours = Double.parseDouble((String) dynaForm.get("praticalHours"));
		Double theoPratHours = Double.parseDouble((String) dynaForm.get("theoPratHours"));
		Double laboratorialHours = Double.parseDouble((String) dynaForm.get("laboratorialHours"));
		Integer executionPeriodId = (Integer) dynaForm.get("executionPeriod");
		Integer curricularPlanId = (Integer) dynaForm.get("degreeCurricularPlan");		
		String[] curricularYearsIdArray = (String[]) dynaForm.get("curricularYearsArray");
						
		Object[] parameters = new Object[] {
				valuationCompetenceCourse.getIdInternal(), 
				curricularYearsIdArray,
				curricularPlanId,
				executionPeriodId,
				courseAcronym,
				theoreticalHours,
				praticalHours,
				theoPratHours,
				laboratorialHours
		};
		
		ServiceUtils.executeService(SessionUtils.getUserView(request), "CreateValuationCurricularCourse", parameters);
						
		return loadValuationCompetenceCourse(mapping, form, request, response);
	}
	
	
	public ActionForward removeValuationCurricularCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		Integer valuationCurricularCourseId = (Integer) dynaForm.get("valuationCurricularCourse");
			
		ServiceUtils.executeService(userView, "DeleteValuationCurricularCourse",new Object[] {valuationCurricularCourseId});
		
		return loadValuationCompetenceCourse(mapping, form, request, response);
	}
}
