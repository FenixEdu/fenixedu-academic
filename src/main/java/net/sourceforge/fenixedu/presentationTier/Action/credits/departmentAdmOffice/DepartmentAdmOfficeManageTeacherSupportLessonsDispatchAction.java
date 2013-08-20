package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
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

import org.apache.commons.lang.StringUtils;
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
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "departmentAdmOffice", path = "/supportLessonsManagement",
        input = "/supportLessonsManagement.do?method=prepareEdit&page=0", attribute = "supportLessonForm",
        formBean = "supportLessonForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "successfull-delete", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "successfull-edit", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "edit-support-lesson", path = "/credits/supportLessons/editSupportLesson.jsp"),
        @Forward(name = "list-support-lessons", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "teacher-not-found", path = "/credits.do?method=viewAnnualTeachingCredits") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction.InvalidPeriodException.class,
                        key = "message.invalidPeriod", handler = org.apache.struts.action.ExceptionHandler.class,
                        path = "/supportLessonsManagement.do?method=prepareEdit&page=0", scope = "request"),
                @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class,
                        scope = "request") })
public class DepartmentAdmOfficeManageTeacherSupportLessonsDispatchAction extends ManageTeacherSupportLessonsDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        String supportLesssonID = (String) supportLessonForm.get("supportLessonID");

        Professorship professorship = getDomainObject(supportLessonForm, "professorshipID");

        if (professorship == null
                || professorship.getTeacher() == null
                || !isTeacherOfManageableDepartments(professorship.getTeacher(), professorship.getExecutionCourse()
                        .getExecutionPeriod(), request)) {

            return mapping.findForward("teacher-not-found");
        }

        SupportLesson supportLesson = null;
        if (!StringUtils.isEmpty(supportLesssonID)) {
            supportLesson = AbstractDomainObject.fromExternalId(supportLesssonID);
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
        List<Unit> workingPlacesByPeriod =
                teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
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
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, InvalidPeriodException {

        editSupportLesson(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        deleteSupportLesson(request, form, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-delete");
    }
}
