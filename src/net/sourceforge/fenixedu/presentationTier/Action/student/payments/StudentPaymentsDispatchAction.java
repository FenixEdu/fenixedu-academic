package net.sourceforge.fenixedu.presentationTier.Action.student.payments;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentPaymentsDispatchAction extends FenixDispatchAction {

    public ActionForward showEvents(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	final Person person = getUserView(request).getPerson();
	
	request.setAttribute("person", person);
	request.setAttribute("notPayedEvents", calculateNotPayedEvents(person));
	request.setAttribute("payedEntries", person.getPayments());
	
	return mapping.findForward("showEvents");
    }
    
    private List<Event> calculateNotPayedEvents(final Person person) {
	
	final List<Event> result = new ArrayList<Event>();
	
	result.addAll(person.getNotPayedEventsPayableOn(null, false));
	result.addAll(person.getNotPayedEventsPayableOn(null, true));
	
	return result;
    }
   
    public ActionForward showEventDetails(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Event event = readEvent(request);
	
	request.setAttribute("person", getUserView(request).getPerson());
	request.setAttribute("event", event);
	request.setAttribute("entryDTOs", event.calculateEntries());
	request.setAttribute("accountingEventPaymentCodes", event.getNonProcessedPaymentCodes());
	
	return mapping.findForward("showEventDetails");
    }

    private Event readEvent(final HttpServletRequest request) {
	
	final Person person = getUserView(request).getPerson();
	final Integer eventId = getIntegerFromRequest(request, "eventId");
	
	for (final Event event : person.getEventsSet()) {
	    if (event.getIdInternal().equals(eventId)) {
		return event;
	    }
	}
	
	return null;
    }

}
