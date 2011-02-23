package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

import org.apache.struts.util.MessageResources;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ClosePunctualRoomsOccupationRequest extends FenixService {
    
    public static final MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(PunctualRoomsOccupationRequest request, Person person) {
	if (request != null) {
	    request.closeRequestAndAssociateOwnerOnlyForEmployees(new DateTime(), person);
	    sendCloseRequestMessage(request);
	    // not for deploy ATM
	    /*final Person requestor = request.getRequestor();
	    final String personName = requestor.getName();
	    final String contactString = requestor.getDefaultPhoneNumber() + "/" + requestor.getDefaultEmailAddressValue();
	    sendMessageToSpaceManagers(request.getGenericEvents(),messages.getMessage("message.room.reservation.spacemanager.requestor", personName,contactString));*/
	}
    }
    
    private static Map<Building, List<GenericEvent>> getEventsByBuilding(Collection<GenericEvent> events) {
	final Map<Building, List<GenericEvent>> eventsByBuldings = new HashMap<Building, List<GenericEvent>>();
	for(GenericEvent event : events) {
	    for (GenericEventSpaceOccupation space : event.getGenericEventSpaceOccupations()) {
		final Building building = space.getRoom().getSpaceBuilding();
		List<GenericEvent> eventsForThisBuilding = eventsByBuldings.get(building);
		if (eventsForThisBuilding == null) {
		    eventsForThisBuilding = new ArrayList<GenericEvent>();
		    eventsByBuldings.put(building,eventsForThisBuilding);
		}
		eventsForThisBuilding.add(event);
	    }
	}
	return eventsByBuldings;
    }

    @SuppressWarnings("unchecked")
    public static void sendMessageToSpaceManagers(Collection<GenericEvent> events, String description) {
	final String separator = getSeparator(100);
	final Map<Building, List<GenericEvent>> eventsbyBuilding = getEventsByBuilding(events);
	for (Building building : eventsbyBuilding.keySet()) {
	    final Group spaceManagementAccessGroup = building.getSpaceManagementAccessGroup();
	    final String emails = building.getSpaceInformation().getEmails();
	    if (spaceManagementAccessGroup != null || (emails != null && !emails.isEmpty())) {
		String body = messages.getMessage("message.room.reservation.spacemanager.body");
		for (GenericEvent event : eventsbyBuilding.get(building)) {
		    body += "\t";
		    for (GenericEventSpaceOccupation space : event.getGenericEventSpaceOccupations()) {
			final AllocatableSpace eventRoom = space.getRoom();
			final Building eventBuilding = eventRoom.getSpaceBuilding();
			if (eventBuilding.equals(building)) {
			    body += eventRoom.getIdentification() + ",";
			}
		    }
		    body = body.substring(0, body.length()-1);
		    body += messages.getMessage("message.room.reservation.spacemanager.body.sep") + event.getPeriodPrettyPrint() + "\n";
		}
		body += separator + "\n";
		body += description;
		body += separator + "\n";
		final Collection<Recipient> recipients = spaceManagementAccessGroup == null ? Collections.EMPTY_LIST : Recipient.newInstance(spaceManagementAccessGroup).asCollection();
		sendMessage(recipients,
			    emails,
			    messages.getMessage("message.room.reservation.spacemanager.subject"), 
			    body);
	    }
	}
    }
    
    private static void sendCloseRequestMessage(PunctualRoomsOccupationRequest roomsReserveRequest) {
	MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");
	String body = messages.getMessage("message.room.reservation.solved") + "\n\n"
		+ messages.getMessage("message.room.reservation.request.number") + "\n" + roomsReserveRequest.getIdentification()
		+ "\n\n";
	body += messages.getMessage("message.room.reservation.request") + "\n";
	if (roomsReserveRequest.getSubject() != null) {
	    body += roomsReserveRequest.getSubject();
	} else {
	    body += "-";
	}
	body += "\n\n" + messages.getMessage("label.rooms.reserve.periods") + ":";
	for (GenericEvent genericEvent : roomsReserveRequest.getGenericEvents()) {
	    body += "\n\t" + genericEvent.getGanttDiagramEventPeriod() + genericEvent.getGanttDiagramEventObservations();
	}
	if (roomsReserveRequest.getGenericEvents().isEmpty()) {
	    body += "\n" + messages.getMessage("label.rooms.reserve.periods.none");
	}
	body += "\n\n" + messages.getMessage("message.room.reservation.description") + "\n";
	if (roomsReserveRequest.getDescription() != null) {
	    body += roomsReserveRequest.getDescription();
	} else {
	    body += "-";
	}
	PunctualRoomsOccupationComment punctualRoomsOccupationComment = getLastComment(roomsReserveRequest);
	
	body += "\n\n" + messages.getMessage("message.room.reservation.last.comment") + "\n";

	if (punctualRoomsOccupationComment != null){
	    body += punctualRoomsOccupationComment.getDescription();
	}else{
	    body += "-";
	}
	sendMessage(Collections.EMPTY_LIST, roomsReserveRequest.getRequestor().getDefaultEmailAddressValue(),
		messages.getMessage("message.room.reservation"), body);
    }

    private static PunctualRoomsOccupationComment getLastComment(PunctualRoomsOccupationRequest roomsReserveRequest) {
	PunctualRoomsOccupationComment last = null;
	for (Iterator<PunctualRoomsOccupationComment> iterator = roomsReserveRequest.getCommentsWithoutFirstCommentOrderByDate()
		.iterator(); iterator.hasNext();) {
	    last = iterator.next();
	}
	return last;
    }

    @Service
    private static void sendMessage(Collection<Recipient> spaceManagers, String email, String subject, String body) {
	SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
	if (email != null || !spaceManagers.isEmpty() ) {
	    new Message(systemSender, systemSender.getConcreteReplyTos(), spaceManagers, subject, body, email);
	}
    }
    
    public static String getSeparator(int len) {
	String separator = "";
	while(len-- > 0) {
	    separator += '-';
	}
	return separator;
    }

}