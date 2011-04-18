package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
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
    
    public static final MessageResources MESSAGES = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");
    
    private static Map<Building, Set<GenericEvent>> getEventsByBuilding(Collection<GenericEvent> events) {
	final Map<Building, Set<GenericEvent>> eventsByBuldings = new HashMap<Building, Set<GenericEvent>>();
	for(GenericEvent event : events) {
	    for (GenericEventSpaceOccupation space : event.getGenericEventSpaceOccupations()) {
		final AllocatableSpace room = space.getRoom();
		final Building building = room.getSpaceBuilding();
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
    
    private static Map<Building,Set<AllocatableSpace>> getRoomsByBuilding(Collection<AllocatableSpace> spaces) {
	final Map<Building,Set<AllocatableSpace>> roomsByBuilding = new HashMap<Building,Set<AllocatableSpace>>();
	for(AllocatableSpace space : spaces) {
	    final Building building = space.getSpaceBuilding();
	    Set<AllocatableSpace> rooms = roomsByBuilding.get(building);
	    if (rooms == null) {
		rooms = new HashSet<AllocatableSpace>();
		roomsByBuilding.put(building,rooms);
	    }
	    rooms.add(space);
	}
	return roomsByBuilding;
    }
    public static void sendMessageToSpaceManagers(WrittenEvaluation eval) {
	sendMessageToSpaceManagers(eval, eval.getAssociatedRooms());
    }
    
    public static void sendMessageToSpaceManagers(WrittenEvaluation eval, List<AllocatableSpace> rooms) {
	final String body_header = getMessageBody();
	final Map<Building,Set<AllocatableSpace>> roomsByBuilding = getRoomsByBuilding(rooms);
	for(Map.Entry<Building, Set<AllocatableSpace>> entry : roomsByBuilding.entrySet()) {
	    final Building building = entry.getKey();
	    
	    final Group managersGroup = building.getSpaceManagementAccessGroup();
	    String emails = building.getSpaceInformation().getEmails();
	    if (managersGroup == null || managersGroup.getElements().isEmpty() || emails == null || emails.isEmpty()) {
		continue;
	    }
	    
	    final Set<AllocatableSpace> roomsBuilding = entry.getValue();
	    Set<String> roomsID = new HashSet<String>();
	    for(AllocatableSpace room : roomsBuilding ) {
		roomsID.add(room.getIdentification());
	    }
	    
	    
	    String subject = getMessageSubject(roomsID);
	    String body = body_header;
	    body += getRoomsString(roomsID);
	    final String date = new SimpleDateFormat("dd/MM/yyyy").format(eval.getDay().getTime());
	    final String startTime = new SimpleDateFormat("HH:mm").format(eval.getBeginning().getTime());
	    final String endTime = new SimpleDateFormat("HH:mm").format(eval.getEnd().getTime());
	    String evalName = null;
	    if (eval instanceof WrittenTest) {
		evalName = ((WrittenTest)eval).getDescription(); 
	    }
	    if (eval instanceof Exam) {
		evalName = ((Exam)eval).getSeason().toString(); 
	    }
	    
	    String courses = new String();
	    
	    final List<ExecutionCourse> associatedExecutionCourses = eval.getAssociatedExecutionCourses();
	    for(ExecutionCourse course : associatedExecutionCourses) {
		courses += course.getSigla() + " - " + course.getNome() + ",";
	    }
	    courses = courses.substring(0, courses.length() -1);
	    body += " " + MESSAGES.getMessage("message.room.reservation.spacemanager.writenevaluation.body", new Object[] { date, startTime, endTime, evalName,courses} );
	    emails += ", natachamoniz@ist.utl.pt";
	    sendMessage(Recipient.newInstance(managersGroup).asCollection(), emails, subject, body);
	}
    }
    
    
    @SuppressWarnings("unchecked")
    public static void sendMessageToSpaceManagers(Collection<GenericEvent> events, String description) {
	final String separator = getSeparator(100);
	final Map<Building, Set<GenericEvent>> eventsbyBuilding = getEventsByBuilding(events);
	for (Building building : eventsbyBuilding.keySet()) {
	    final Group spaceManagementAccessGroup = building.getSpaceManagementAccessGroup();
	    final String emails = building.getSpaceInformation().getEmails();
	    if (spaceManagementAccessGroup != null || (emails != null && !emails.isEmpty())) {
		String body = getMessageBody();
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
		    body += MESSAGES.getMessage("message.room.reservation.spacemanager.body.sep") + event.getGanttDiagramEventPeriod() + "\n";
		}
		body += separator + "\n";
		body += description;
		body += separator + "\n";
		body += getLegend();
		body += separator + "\n";
		final Collection<Recipient> recipients = spaceManagementAccessGroup == null ? Collections.EMPTY_LIST : Recipient.newInstance(spaceManagementAccessGroup).asCollection();
		String subject = getMessageSubject(rooms);
		sendMessage(recipients,emails,subject, body);
	    }
	}
    }
    
    private static String getMessageBody() {
	String message = MESSAGES.getMessage("message.room.reservation.spacemanager.body") + "\n";
	return message;
    }
    
    private static String getMessageBody(Collection<String> rooms) {
	return getMessageBody() + getRoomsString(rooms);
    }

    private static String getMessageSubject(Collection<String> rooms) {
	String subject = MESSAGES.getMessage("message.room.reservation.spacemanager.subject") + ":";
	subject += getRoomsString(rooms);
	return subject;
    }

    private static String getRoomsString(Collection<String> rooms) {
	String header = new String();
	for(String roomID : rooms) {
	    header += " " + roomID + ",";
	}
	return header.substring(0, header.length() - 1);
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
	builder.append(MESSAGES.getMessage("label.legend") + "\n");
	builder.append("[C]" + MESSAGES.getMessage("label.continuous") + "\n");
	builder.append("[D]" + MESSAGES.getMessage("label.daily") + "\n");
	builder.append("[D-S]" + MESSAGES.getMessage("label.daily.1") + "\n");
	builder.append("[D-D]" + MESSAGES.getMessage("label.daily.2") + "\n");
	builder.append("[D-SD]" + MESSAGES.getMessage("label.daily.3") + "\n");
	builder.append("[S]" + MESSAGES.getMessage("label.weekly") + "\n");
	builder.append("[Q]" + MESSAGES.getMessage("label.biweekly") + "\n");
	return builder.toString();
    }
    
}

