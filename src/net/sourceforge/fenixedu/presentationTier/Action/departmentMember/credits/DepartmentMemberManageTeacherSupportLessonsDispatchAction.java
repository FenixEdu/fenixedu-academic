package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class DepartmentMemberManageTeacherSupportLessonsDispatchAction extends ManageTeacherSupportLessonsDispatchAction {

    public ActionForward showSupportLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm supportLessonForm = (DynaActionForm) form;
	Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");
	Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

	Teacher loggedTeacher = getLoggedTeacher(request);

	if (professorship == null || professorship.getTeacher() != loggedTeacher) {
	    createNewActionMessage(request);
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

	if (professorship == null || professorship.getTeacher() != getLoggedTeacher(request)) {
	    createNewActionMessage(request);
	    return mapping.findForward("teacher-not-found");
	}

	SupportLesson supportLesson = null;
	if (supportLesssonID != null && supportLesssonID != 0) {
	    supportLesson = rootDomainObject.readSupportLessonByOID(supportLesssonID);
	    if (!professorship.getSupportLessons().contains(supportLesson)) {
		createNewActionMessage(request);
		return mapping.findForward("teacher-not-found");
	    }
	}

	prepareToEdit(supportLesson, professorship, supportLessonForm, request);
	return mapping.findForward("edit-support-lesson");
    }

    private void createNewActionMessage(HttpServletRequest request) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("", new ActionMessage("message.invalid.teacher"));
	saveMessages(request, actionMessages);
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
	IUserView userView = UserView.getUser();
	return userView.getPerson().getTeacher();
    }

    public ActionForward editSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
	    InvalidPeriodException {

	editSupportLesson(form, request, RoleType.DEPARTMENT_MEMBER);
	return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	deleteSupportLesson(request, form, RoleType.DEPARTMENT_MEMBER);
	return mapping.findForward("successfull-delete");
    }
}
