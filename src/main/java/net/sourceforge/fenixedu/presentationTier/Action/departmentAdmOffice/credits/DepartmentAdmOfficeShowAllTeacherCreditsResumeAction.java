package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowAllTeacherCreditsResumeAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/showAllTeacherCreditsResume", attribute = "teacherSearchForm",
        formBean = "teacherSearchForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "search-teacher-form", path = "search-for-teacher-credits"),
        @Forward(name = "teacher-not-found", path = "search-for-teacher-credits"),
        @Forward(name = "show-all-credits-resume", path = "showAllCreditsResume") })
@Exceptions(value = { @ExceptionHandling(type = java.lang.NumberFormatException.class, key = "errors.invalid.teacher-number",
        handler = org.apache.struts.action.ExceptionHandler.class,
        path = "/showAllTeacherCreditsResume.do?method=prepareTeacherSearch&page=0", scope = "request") })
public class DepartmentAdmOfficeShowAllTeacherCreditsResumeAction extends ShowAllTeacherCreditsResumeAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherCreditsResume");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        Teacher teacher = Teacher.readByIstId(dynaActionForm.getString("teacherId").trim());

        if (teacher == null || !isTeacherOfManageableDepartments(teacher, executionSemester, request)) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            dynaActionForm.set("method", "showTeacherCreditsResume");
            return mapping.findForward("teacher-not-found");
        }

        readAllTeacherCredits(request, teacher);
        return mapping.findForward("show-all-credits-resume");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
            HttpServletRequest request) {
        User userView = Authenticate.getUser();
        Collection<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        List<Unit> workingPlacesByPeriod =
                teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
                        executionSemester.getEndDateYearMonthDay());
        for (Unit unit : workingPlacesByPeriod) {
            DepartmentUnit departmentUnit = unit.getDepartmentUnit();
            Department teacherDepartment = departmentUnit != null ? departmentUnit.getDepartment() : null;
            if (teacherDepartment != null && manageableDepartments.contains(teacherDepartment)) {
                return true;
            }
        }
        return false;
    }
}
