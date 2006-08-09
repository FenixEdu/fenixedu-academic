package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherAdviseServiceDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class DepartmentMemberManageTeacherAdviseServiceDispatchAction extends
        ManageTeacherAdviseServiceDispatchAction {

    public ActionForward showTeacherAdvises(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        final Integer executionPeriodID = (Integer) dynaForm.get("executionPeriodId");
        final ExecutionPeriod executionPeriod = rootDomainObject
                .readExecutionPeriodByOID(executionPeriodID);

        Integer teacherNumber = Integer.valueOf(dynaForm.getString("teacherNumber"));
        Teacher teacher = Teacher.readByNumber(teacherNumber);

        if (teacher == null || getLoggedTeacher(request) != teacher) {
            createNewActionMessage(request);
            return mapping.findForward("teacher-not-found");
        }

        getAdviseServices(request, dynaForm, executionPeriod, teacher);
        return mapping.findForward("list-teacher-advise-services");
    }

    private void createNewActionMessage(HttpServletRequest request) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage("message.invalid.teacher"));
        saveMessages(request, actionMessages);
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        IUserView userView = SessionUtils.getUserView(request);
        return userView.getPerson().getTeacher();
    }
    
    public ActionForward editAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        return editAdviseService(form, request, mapping, RoleType.DEPARTMENT_MEMBER);
    }

    public ActionForward deleteAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        deleteAdviseService(request, RoleType.DEPARTMENT_MEMBER);
        return mapping.findForward("successfull-delete");

    }
}
