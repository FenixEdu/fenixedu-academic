package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

import org.apache.struts.util.MessageResources;

import pt.ist.fenixWebFramework.services.Service;

public class GOPSendMessageService {
    
    public static final MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");
    
    private static Map<Building, Set<GenericEvent>> getEventsByBuilding(Collection<GenericEvent> events) {
	final Map<Building, Set<GenericEvent>> eventsByBuldings = new HashMap<Building, Set<GenericEvent>>();
	for(GenericEvent event : events) {
	    for (GenericEventSpaceOccupation space : event.getGenericEventSpaceOccupations()) {
		final Building building = space.getRoom().getSpaceBuilding();
		Set<GenericEvent> eventsForThisBuilding = eventsByBuldings.get(building);
		if (eventsForThisBuilding == null) {
		    eventsForThisBuilding = new HashSet<GenericEvent>();
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
	final Map<Building, Set<GenericEvent>> eventsbyBuilding = getEventsByBuilding(events);
	for (Building building : eventsbyBuilding.keySet()) {
	    final Group spaceManagementAccessGroup = building.getSpaceManagementAccessGroup();
	    final String emails = building.getSpaceInformation().getEmails();
	    if (spaceManagementAccessGroup != null || (emails != null && !emails.isEmpty())) {
		String body = messages.getMessage("message.room.reservation.spacemanager.body");
		Set<String> rooms = new HashSet<String>();
		for (GenericEvent event : eventsbyBuilding.get(building)) {
		    body += "\t";
		    for (GenericEventSpaceOccupation space : event.getGenericEventSpaceOccupations()) {
			final AllocatableSpace eventRoom = space.getRoom();
			final Building eventBuilding = eventRoom.getSpaceBuilding();
			if (eventBuilding.equals(building)) {
			    final String roomID = eventRoom.getIdentification();
			    rooms.add(roomID);
			    body += roomID + ",";
			}
		    }
		    body = body.substring(0, body.length()-1);
		    body += messages.getMessage("message.room.reservation.spacemanager.body.sep") + event.getGanttDiagramEventPeriod() + "\n";
		}
		body += separator + "\n";
		body += description;
		body += separator + "\n";
		body += getLegend();
		body += separator + "\n";
		final Collection<Recipient> recipients = spaceManagementAccessGroup == null ? Collections.EMPTY_LIST : Recipient.newInstance(spaceManagementAccessGroup).asCollection();
		String subject = messages.getMessage("message.room.reservation.spacemanager.subject") + ":";
		for(String roomID : rooms) {
		    subject += " " + roomID + ",";
		}
		subject = subject.substring(0, subject.length() - 1);
		sendMessage(recipients,emails,subject, body);
	    }
	}
    }
    
    @Service
    public static void sendMessage(Collection<Recipient> spaceManagers, String email, String subject, String body) {
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
    
    public static String getLegend() {
	StringBuilder builder = new StringBuilder();
	builder.append(messages.getMessage("label.legend") + "\n");
	builder.append("[C]" + messages.getMessage("label.continuous") + "\n");
	builder.append("[D]" + messages.getMessage("label.daily") + "\n");
	builder.append("[D-S]" + messages.getMessage("label.daily.1") + "\n");
	builder.append("[D-D]" + messages.getMessage("label.daily.2") + "\n");
	builder.append("[D-SD]" + messages.getMessage("label.daily.3") + "\n");
	builder.append("[S]" + messages.getMessage("label.weekly") + "\n");
	builder.append("[Q]" + messages.getMessage("label.biweekly") + "\n");
	return builder.toString();
    }
    
}

