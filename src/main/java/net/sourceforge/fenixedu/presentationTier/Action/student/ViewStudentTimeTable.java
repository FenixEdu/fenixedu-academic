package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author naat
 * @author zenida
 * 
 */
@Mapping(module = "student", path = "/studentTimeTable", input = "/studentTimeTable.do?page=0",
        attribute = "studentTimeTableForm", formBean = "studentTimeTableForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showTimeTable", path = "df.timeTable.show"),
        @Forward(name = "chooseRegistration", path = "/student/timeTable/chooseRegistration.jsp") })
public class ViewStudentTimeTable extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        List<Registration> registrations = getUserView(request).getPerson().getStudent().getActiveRegistrations();
        if (registrations.size() == 1) {
            return forwardToShowTimeTable(registrations.get(0), mapping, request, ExecutionSemester.readActualExecutionSemester());
        } else {
            request.setAttribute("registrations", registrations);
            return mapping.findForward("chooseRegistration");
        }
    }

    public ActionForward showTimeTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        return forwardToShowTimeTable(getRegistration(actionForm, request), mapping, request,
                ExecutionSemester.readActualExecutionSemester());
    }

    protected ActionForward forwardToShowTimeTableForSupervisor(Registration registration, ActionMapping mapping,
            HttpServletRequest request) throws FenixActionException, FenixServiceException {

        return forwardToShowTimeTable(registration, mapping, request, ExecutionSemester.readActualExecutionSemester());
    }

    public static ActionForward forwardToShowTimeTable(Registration registration, ActionMapping mapping,
            HttpServletRequest request, ExecutionSemester executionSemester) throws FenixActionException, FenixServiceException {
        List<InfoShowOccupation> infoLessons = ReadStudentTimeTable.run(registration, executionSemester);

        request.setAttribute("person", registration.getPerson());
        request.setAttribute("infoLessons", infoLessons);
        request.setAttribute("registrationId", registration.getExternalId());
        request.setAttribute("executionSemesterId", executionSemester.getExternalId());
        return mapping.findForward("showTimeTable");
    }

    private Registration getRegistration(final ActionForm form, final HttpServletRequest request) {
        String registrationId = (String) ((DynaActionForm) form).get("registrationId");
        if (StringUtils.isEmpty(registrationId) && !StringUtils.isEmpty(request.getParameter("registrationId"))) {
            registrationId = request.getParameter("registrationId");
        }
        return FenixFramework.getDomainObject(registrationId);
    }
}