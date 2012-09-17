package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

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
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/supportLessonsManagement", input = "/supportLessonsManagement.do?method=prepareEdit&page=0", attribute = "supportLessonForm", formBean = "supportLessonForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "successfull-delete", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
	@Forward(name = "successfull-edit", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
	@Forward(name = "edit-support-lesson", path = "/credits/supportLessons/editSupportLesson.jsp"),
	@Forward(name = "list-support-lessons", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
	@Forward(name = "teacher-not-found", path = "/credits.do?method=viewAnnualTeachingCredits") })
@Exceptions(value = {
	@ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction.InvalidPeriodException.class, key = "message.invalidPeriod", handler = org.apache.struts.action.ExceptionHandler.class, path = "/supportLessonsManagement.do?method=prepareEdit&page=0", scope = "request"),
	@ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class DepartmentMemberManageTeacherSupportLessonsDispatchAction extends ManageTeacherSupportLessonsDispatchAction {

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
