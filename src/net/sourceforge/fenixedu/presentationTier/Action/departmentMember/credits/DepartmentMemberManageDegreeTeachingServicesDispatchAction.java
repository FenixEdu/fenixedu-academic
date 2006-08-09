package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class DepartmentMemberManageDegreeTeachingServicesDispatchAction extends ManageDegreeTeachingServicesDispatchAction {

    public ActionForward showTeachingServiceDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer professorshipID = (Integer) dynaForm.get("professorshipID");
        Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

        if (professorship == null || professorship.getTeacher() != getLoggedTeacher(request)) {
            createNewActionMessage(request);
            return mapping.findForward("teacher-not-found");
        }

        teachingServiceDetailsProcess(professorship, request, dynaForm);
        return mapping.findForward("show-teaching-service-percentages");
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
    
    public ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {        
        return updateTeachingServices(mapping, form, request, RoleType.DEPARTMENT_MEMBER);
    }
}
