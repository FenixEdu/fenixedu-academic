package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowAllTeacherCreditsResumeAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

public class DepartmentMemberShowAllTeacherCreditsResumeAction extends ShowAllTeacherCreditsResumeAction {

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = UserView.getUser();
	Teacher teacher = userView.getPerson().getTeacher();
	readAllTeacherCredits(request, teacher);
	return mapping.findForward("show-all-credits-resume");
    }
}
