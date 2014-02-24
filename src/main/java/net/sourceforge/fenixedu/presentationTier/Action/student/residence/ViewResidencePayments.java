package net.sourceforge.fenixedu.presentationTier.Action.student.residence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = StudentViewApp.class, path = "residence-payments", titleKey = "link.title.residencePayments")
@Mapping(path = "/viewResidencePayments", module = "student")
@Forwards({ @Forward(name = "showEvents", path = "/student/residenceServices/showResidenceEvents.jsp"),
        @Forward(name = "eventDetails", path = "/student/residenceServices/showDetails.jsp") })
public class ViewResidencePayments extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        request.setAttribute("person", person);
        request.setAttribute("payedEntries", person.getPayments(ResidenceEvent.class));
        request.setAttribute("notPayedEvents", person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, false));

        return mapping.findForward("showEvents");
    }

    public ActionForward showEventDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String eventOID = request.getParameter("event");
        ResidenceEvent residenceEvent = FenixFramework.getDomainObject(eventOID);

        request.setAttribute("person", getLoggedPerson(request));
        request.setAttribute("event", residenceEvent);
        request.setAttribute("entryDTOs", residenceEvent.calculateEntries());
        request.setAttribute("accountingEventPaymentCodes", residenceEvent.getNonProcessedPaymentCodes());

        return mapping.findForward("eventDetails");
    }
}
