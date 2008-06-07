package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
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

import pt.ist.fenixWebFramework.security.UserView;

public class DepartmentMemberManageCreditsNotes extends ManageCreditsNotes {

    public ActionForward viewNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String teacherNumber = request.getParameter("teacherNumber");
	String executionPeriodId = request.getParameter("executionPeriodId");
	String noteType = request.getParameter("noteType");

	Teacher teacher = Teacher.readByNumber(Integer.valueOf(teacherNumber));

	if (teacher == null || teacher != getLoggedTeacher(request)) {
	    createNewActionMessage(request);
	    return mapping.findForward("teacher-not-found");
	}

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(Integer.valueOf(executionPeriodId));
	getNote(actionForm, teacher, executionSemester, noteType);

	return mapping.findForward("show-note");
    }

    public ActionForward editNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	Integer teacherId = (Integer) dynaActionForm.get("teacherId");
	Integer executionPeriodId = (Integer) dynaActionForm.get("executionPeriodId");
	String noteType = dynaActionForm.getString("noteType");

	return editNote(request, dynaActionForm, teacherId, executionPeriodId, RoleType.DEPARTMENT_MEMBER, mapping, noteType);
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
	IUserView userView = UserView.getUser();
	return userView.getPerson().getTeacher();
    }

    private void createNewActionMessage(HttpServletRequest request) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("", new ActionMessage("message.invalid.teacher"));
	saveMessages(request, actionMessages);
    }
}
