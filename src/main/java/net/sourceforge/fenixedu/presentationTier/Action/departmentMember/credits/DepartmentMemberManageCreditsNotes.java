package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageCreditsNotes;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentMember", path = "/manageCreditsNotes", attribute = "creditsNotesForm",
        formBean = "creditsNotesForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-note", path = "/credits/notes/listCreditsNotes.jsp"),
        @Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0"),
        @Forward(name = "edit-note", path = "/showFullTeacherCreditsSheet.do?method=showTeacherCredits&page=0") })
public class DepartmentMemberManageCreditsNotes extends ManageCreditsNotes {

    public ActionForward viewNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Teacher teacher = FenixFramework.getDomainObject(request.getParameter("teacherId"));
        String executionPeriodId = request.getParameter("executionPeriodId");
        String noteType = request.getParameter("noteType");

        if (teacher == null || teacher != getLoggedTeacher(request)) {
            createNewActionMessage(request);
            return mapping.findForward("teacher-not-found");
        }

        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        getNote(actionForm, teacher, executionSemester, noteType);

        return mapping.findForward("show-note");
    }

    public ActionForward editNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Teacher teacher = FenixFramework.getDomainObject((String) dynaActionForm.get("teacherId"));
        String executionPeriodId = (String) dynaActionForm.get("executionPeriodId");
        String noteType = dynaActionForm.getString("noteType");

        return editNote(request, dynaActionForm, teacher, executionPeriodId, RoleType.DEPARTMENT_MEMBER, mapping, noteType);
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        User userView = Authenticate.getUser();
        return userView.getPerson().getTeacher();
    }

    private void createNewActionMessage(HttpServletRequest request) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage("message.invalid.teacher"));
        saveMessages(request, actionMessages);
    }
}
