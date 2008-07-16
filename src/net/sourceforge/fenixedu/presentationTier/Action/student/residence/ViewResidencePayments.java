package net.sourceforge.fenixedu.presentationTier.Action.student.residence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;


@Mapping(path = "/viewResidencePayments", module = "student")
@Forwards( { @Forward(name = "showEvents", path = "show-student-residence-payments"),
	@Forward(name = "eventDetails", path = "view-event-details") })
public class ViewResidencePayments extends FenixDispatchAction {

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
	
	long eventOID = Long.parseLong(request.getParameter("event"));
	ResidenceEvent residenceEvent = (ResidenceEvent) DomainObject.fromOID(eventOID);
	
	request.setAttribute("person", getLoggedPerson(request));
	request.setAttribute("event", residenceEvent);
	request.setAttribute("entryDTOs", residenceEvent.calculateEntries());
	request.setAttribute("accountingEventPaymentCodes", residenceEvent.getNonProcessedPaymentCodes());
	
	return mapping.findForward("eventDetails");
    }
}
