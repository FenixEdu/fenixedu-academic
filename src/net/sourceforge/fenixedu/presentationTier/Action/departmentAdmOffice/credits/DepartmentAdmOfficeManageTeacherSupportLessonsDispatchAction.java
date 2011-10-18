package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/supportLessonsManagement", input = "/supportLessonsManagement.do?method=prepareEdit&page=0", attribute = "supportLessonForm", formBean = "supportLessonForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "successfull-delete", path = "/supportLessonsManagement.do?method=showSupportLessons&page=0"),
	@Forward(name = "successfull-edit", path = "/supportLessonsManagement.do?method=showSupportLessons&page=0"),
	@Forward(name = "edit-support-lesson", path = "edit-support-lesson"),
	@Forward(name = "list-support-lessons", path = "show-teacher-execution-course-support-lessons"),
	@Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
@Exceptions(value = {
	@ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction.InvalidPeriodException.class, key = "message.invalidPeriod", handler = org.apache.struts.action.ExceptionHandler.class, path = "/supportLessonsManagement.do?method=prepareEdit&page=0", scope = "request"),
	@ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class DepartmentAdmOfficeManageTeacherSupportLessonsDispatchAction extends ManageTeacherSupportLessonsDispatchAction {

    public ActionForward showSupportLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm supportLessonForm = (DynaActionForm) form;
	Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");
	Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

	if (professorship == null
		|| professorship.getTeacher() == null
		|| !isTeacherOfManageableDepartments(professorship.getTeacher(), professorship.getExecutionCourse()
			.getExecutionPeriod(), request)) {
	    return mapping.findForward("teacher-not-found");
	}

	getSupportLessons(request, professorship);
	return mapping.findForward("list-support-lessons");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm supportLessonForm = (DynaActionForm) form;
	Integer supportLesssonID = (Integer) supportLessonForm.get("supportLessonID");
	Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");

	Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

	if (professorship == null
		|| professorship.getTeacher() == null
		|| !isTeacherOfManageableDepartments(professorship.getTeacher(), professorship.getExecutionCourse()
			.getExecutionPeriod(), request)) {

	    return mapping.findForward("teacher-not-found");
	}

	SupportLesson supportLesson = null;
	if (supportLesssonID != null && supportLesssonID != 0) {
	    supportLesson = rootDomainObject.readSupportLessonByOID(supportLesssonID);
	    if (!professorship.getSupportLessons().contains(supportLesson)) {
		return mapping.findForward("teacher-not-found");
	    }
	}

	prepareToEdit(supportLesson, professorship, supportLessonForm, request);
	return mapping.findForward("edit-support-lesson");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
	    HttpServletRequest request) {
	IUserView userView = UserView.getUser();
	List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
	List<Unit> workingPlacesByPeriod = teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay());
	for (Unit unit : workingPlacesByPeriod) {
	    DepartmentUnit departmentUnit = unit.getDepartmentUnit();
	    Department teacherDepartment = departmentUnit != null ? departmentUnit.getDepartment() : null;
	    if (manageableDepartments.contains(teacherDepartment)) {
		return true;
	    }
	}
	return false;
    }

    public ActionForward editSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
	    InvalidPeriodException {

	editSupportLesson(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
	return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	deleteSupportLesson(request, form, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
	return mapping.findForward("successfull-delete");
    }
}
