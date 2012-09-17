package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherInstitutionWorkingTimeDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/institutionWorkingTimeManagement", input = "/institutionWorkingTimeManagement.do?method=prepareEdit&page=0", attribute = "teacherInstitutionWorkingTimeForm", formBean = "teacherInstitutionWorkingTimeForm", scope = "request", parameter = "method")
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class DepartmentMemberManageTeacherInstitutionWorkingTimeDispatchAction extends
	ManageTeacherInstitutionWorkingTimeDispatchAction {

    @Override
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	InstitutionWorkTime institutionWorkTime = DomainObject.fromExternalId((String) getFromRequest(request,
		"institutionWorkTimeOid"));
	Teacher teacher = institutionWorkTime.getTeacherService().getTeacher();
	if (teacher == null || teacher != getLoggedTeacher(request)) {
	    createNewActionMessage(request);
	    return mapping.findForward("viewAnnualTeachingCredits");
	}
	request.setAttribute("institutionWorkTime", institutionWorkTime);
	return mapping.findForward("edit-institution-work-time");
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

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws NumberFormatException, FenixFilterException, FenixServiceException {
	return delete(mapping, form, request, response, RoleType.DEPARTMENT_MEMBER);
    }

}
