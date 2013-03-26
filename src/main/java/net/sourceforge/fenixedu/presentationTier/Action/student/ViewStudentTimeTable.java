package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
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
            throws FenixActionException, FenixFilterException, FenixServiceException {

        List<Registration> registrations = getUserView(request).getPerson().getStudent().getActiveRegistrations();
        if (registrations.size() == 1) {
            return forwardToShowTimeTable(registrations.get(0), mapping, request);
        } else {
            request.setAttribute("registrations", registrations);
            return mapping.findForward("chooseRegistration");
        }
    }

    public ActionForward showTimeTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        return forwardToShowTimeTable(getRegistration(actionForm, request), mapping, request);
    }

    protected ActionForward forwardToShowTimeTableForSupervisor(Registration registration, ActionMapping mapping,
            HttpServletRequest request) throws FenixActionException, FenixFilterException, FenixServiceException {

        return forwardToShowTimeTable(registration, mapping, request);
    }

    public static ActionForward forwardToShowTimeTable(Registration registration, ActionMapping mapping,
            HttpServletRequest request) throws FenixActionException, FenixFilterException, FenixServiceException {

        List<InfoLesson> infoLessons = ReadStudentTimeTable.run(registration);

        request.setAttribute("person", registration.getPerson());
        request.setAttribute("infoLessons", infoLessons);
        request.setAttribute("registrationId", registration.getIdInternal());
        return mapping.findForward("showTimeTable");
    }

    private Registration getRegistration(final ActionForm form, final HttpServletRequest request) {
        Integer registrationId = (Integer) ((DynaActionForm) form).get("registrationId");
        if (registrationId == null && !StringUtils.isEmpty(request.getParameter("registrationId"))) {
            registrationId = Integer.valueOf(request.getParameter("registrationId"));
        }
        return rootDomainObject.readRegistrationByOID(registrationId);
    }
}