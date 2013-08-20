package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageCreditsNotes;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "departmentAdmOffice", path = "/manageCreditsNotes", attribute = "creditsNotesForm",
        formBean = "creditsNotesForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-note", path = "show-note"),
        @Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0"),
        @Forward(name = "edit-note", path = "/showFullTeacherCreditsSheet.do?method=showTeacherCredits&page=0") })
public class DepartmentAdmOfficeManageCreditsNotes extends ManageCreditsNotes {

    public ActionForward viewNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Teacher teacher = AbstractDomainObject.fromExternalId(request.getParameter("teacherId"));
        String executionPeriodId = request.getParameter("executionPeriodId");
        String noteType = request.getParameter("noteType");
        ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodId);

        if (getTeacherOfManageableDepartments(teacher, executionSemester, request) == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }

        getNote(actionForm, teacher, executionSemester, noteType);

        return mapping.findForward("show-note");
    }

    public ActionForward editNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Teacher teacher = AbstractDomainObject.fromExternalId((String) dynaActionForm.get("teacherId"));
        String executionPeriodId = (String) dynaActionForm.get("executionPeriodId");
        String noteType = dynaActionForm.getString("noteType");

        return editNote(request, dynaActionForm, teacher, executionPeriodId, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, mapping,
                noteType);
    }

    private Boolean getTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
            HttpServletRequest request) {
        IUserView userView = UserView.getUser();
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        Department teacherWorkingDepartment = teacher.getCurrentWorkingDepartment();
        if (teacherWorkingDepartment != null) {
            return manageableDepartments.contains(teacherWorkingDepartment);
        }
        return false;
    }
}
