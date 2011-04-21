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
    private static final String GOP_ADMIN_EMAIL = "natachamoniz@ist.utl.pt";

    public static final MessageResources MESSAGES = MessageResources
	    .getMessageResources("resources/ResourceAllocationManagerResources");

    private static Map<Building, Set<GenericEvent>> getEventsByBuilding(Collection<GenericEvent> events) {
	final Map<Building, Set<GenericEvent>> eventsByBuldings = new HashMap<Building, Set<GenericEvent>>();
	for (GenericEvent event : events) {
	    for (GenericEventSpaceOccupation space : event.getGenericEventSpaceOccupations()) {
		final AllocatableSpace room = space.getRoom();
		final Building building = room.getSpaceBuilding();
		Set<GenericEvent> eventsForThisBuilding = eventsByBuldings.get(building);
		if (eventsForThisBuilding == null) {
		    eventsForThisBuilding = new HashSet<GenericEvent>();
		    eventsByBuldings.put(building, eventsForThisBuilding);
		}
		eventsForThisBuilding.add(event);
	    }
	}
	return eventsByBuldings;
    }

    private static Map<Building, Set<AllocatableSpace>> getRoomsByBuilding(Collection<AllocatableSpace> spaces) {
	final Map<Building, Set<AllocatableSpace>> roomsByBuilding = new HashMap<Building, Set<AllocatableSpace>>();
	for (AllocatableSpace space : spaces) {
	    final Building building = space.getSpaceBuilding();
	    Set<AllocatableSpace> rooms = roomsByBuilding.get(building);
	    if (rooms == null) {
		rooms = new HashSet<AllocatableSpace>();
		roomsByBuilding.put(building, rooms);
	    }
	    rooms.add(space);
	}
	return roomsByBuilding;
    }

    private static boolean dontHaveRecipients(Group managersGroup, String emails) {
	return (emails == null || emails.isEmpty() || !emails.contains(",")) && (managersGroup == null || managersGroup.getElements().isEmpty());
    }
    
    public static void sendMessageToSpaceManagers(WrittenEvaluation eval, List<AllocatableSpace> previousRooms) {
	final List<AllocatableSpace> evalRooms = eval.getAssociatedRooms();
	final Map<Building, Set<AllocatableSpace>> associatedRooms = getRoomsByBuilding(evalRooms);
	final Map<Building, Set<AllocatableSpace>> previousAssociatedRooms = getRoomsByBuilding(previousRooms);
	final Set<Building> buildings = new HashSet<Building>();
	buildings.addAll(associatedRooms.keySet());
	buildings.addAll(previousAssociatedRooms.keySet());

	Collection<String> roomsAdded;
	Collection<String> roomsRemoved;

	final String SUBJECT_FORMAT = "Alteração de salas : [%s] para [%s]";
	final String SUBJECT_ONLY1_FORMAT = "Alteração de salas : [%s]";
	final String BODY_FORMAT = "Para a prova de avaliação escrita %s no dia %s das %s às %s, as salas : \n";
	final String ROOMS_REMOVED_FORMAT = "%s foram removidas\n";
	final String ROOMS_ADDED_FORMAT = "%s foram adicionadas\n";

	for (Building building : buildings) {
	    
	    final Group managersGroup = building.getSpaceManagementAccessGroup();
	    String emails = getEmails(building);
	    
	    if (dontHaveRecipients(managersGroup,emails)) {
		continue;
	    }
	    
	    Set<AllocatableSpace> assocRooms = associatedRooms.get(building);
	    Set<AllocatableSpace> prevRooms = previousAssociatedRooms.get(building);
	    if (prevRooms == null) {
		prevRooms = Collections.emptySet();
	    }

	    if (assocRooms == null) {
		assocRooms = Collections.emptySet();
	    }

	    Collection<String> prevRoomsIDs = getRoomsIDs(prevRooms);
	    Collection<String> assocRoomsIDs = getRoomsIDs(assocRooms);

	    roomsRemoved = subtract(prevRoomsIDs, assocRoomsIDs);
	    roomsAdded = subtract(assocRoomsIDs, prevRoomsIDs);

	    if (roomsRemoved.isEmpty() && roomsAdded.isEmpty()) {
		continue;
	    }

	    final String date = new SimpleDateFormat("dd/MM/yyyy").format(eval.getDay().getTime());
	    final String startTime = new SimpleDateFormat("HH:mm").format(eval.getBeginning().getTime());
	    final String endTime = new SimpleDateFormat("HH:mm").format(eval.getEnd().getTime());
	    String evalName = null;
	    if (eval instanceof WrittenTest) {
		evalName = ((WrittenTest) eval).getDescription();
	    }
	    if (eval instanceof Exam) {
		evalName = ((Exam) eval).getSeason().toString();
	    }

	    String body = String.format(BODY_FORMAT, evalName, date, startTime, endTime);
	    String subject;
	    String roomsRemovedString = null;
	    String roomsAddedString = null;
	    if (!roomsRemoved.isEmpty()) {
		roomsRemovedString = getRoomsString(roomsRemoved);
		body += String.format(ROOMS_REMOVED_FORMAT, roomsRemovedString);
	    }
	    if (!roomsAdded.isEmpty()) {
		roomsAddedString = getRoomsString(roomsAdded);
		body += String.format(ROOMS_ADDED_FORMAT, roomsAddedString);
	    }

	    final String prevRoomsIDsString = getRoomsString(prevRoomsIDs);
	    final String assocRoomsIDsString = getRoomsString(assocRoomsIDs);

	    if (prevRoomsIDsString.isEmpty() && !assocRoomsIDsString.isEmpty()) {
		subject = String.format(SUBJECT_ONLY1_FORMAT, assocRoomsIDsString);

	    } else if (!prevRoomsIDsString.isEmpty() && assocRoomsIDsString.isEmpty()) {
		subject = String.format(SUBJECT_ONLY1_FORMAT, prevRoomsIDsString);

	    } else {
		subject = String.format(SUBJECT_FORMAT, prevRoomsIDsString, assocRoomsIDsString);
	    }

	    sendMessage(getRecipients(managersGroup), emails, subject, body);
	}
    }

    public static void sendMessageToSpaceManagers(WrittenEvaluation eval) {
	final Map<Building, Set<AllocatableSpace>> roomsByBuilding = getRoomsByBuilding(eval.getAssociatedRooms());
	for (Map.Entry<Building, Set<AllocatableSpace>> entry : roomsByBuilding.entrySet()) {
	    final Building building = entry.getKey();

	    final Group managersGroup = building.getSpaceManagementAccessGroup();
	    String emails = getEmails(building);
	    
	    if (dontHaveRecipients(managersGroup,emails)) {
		continue;
	    }
	    
	    final Set<AllocatableSpace> roomsBuilding = entry.getValue();
	    Set<String> roomsID = new HashSet<String>();
	    for (AllocatableSpace room : roomsBuilding) {
		roomsID.add(room.getIdentification());
	    }

	    String subject = getMessageSubject(roomsID);
	    String body = getMessageBody();
	    body += getRoomsString(roomsID);
	    final String date = new SimpleDateFormat("dd/MM/yyyy").format(eval.getDay().getTime());
	    final String startTime = new SimpleDateFormat("HH:mm").format(eval.getBeginning().getTime());
	    final String endTime = new SimpleDateFormat("HH:mm").format(eval.getEnd().getTime());
	    String evalName = null;
	    if (eval instanceof WrittenTest) {
		evalName = ((WrittenTest) eval).getDescription();
	    }
	    if (eval instanceof Exam) {
		evalName = ((Exam) eval).getSeason().toString();
	    }
	    final String courses = getCoursesString(eval);
	    body += " "
		    + MESSAGES.getMessage("message.room.reservation.spacemanager.writenevaluation.body", new Object[] { date,
			    startTime, endTime, evalName, courses });

	    sendMessage(getRecipients(managersGroup), emails, subject, body);
	}
    }

    private static Collection<Recipient> getRecipients(Group managersGroup) {
	if (managersGroup == null || managersGroup.getElements().isEmpty()) {
	    return Collections.emptySet();
	}
	return Recipient.newInstance(managersGroup).asCollection();
    }

    private static String getEmails(final Building building) {
	String emails = building.getSpaceInformation().getEmails();
	if (emails == null || emails.isEmpty()) {
	    return GOP_ADMIN_EMAIL;
	} else {
	    emails += ", " + GOP_ADMIN_EMAIL;
	}
	return emails;
    }

    private static String getCoursesString(WrittenEvaluation eval) {
	String courses = new String();

	final List<ExecutionCourse> associatedExecutionCourses = eval.getAssociatedExecutionCourses();
	for (ExecutionCourse course : associatedExecutionCourses) {
	    courses += course.getSigla() + " - " + course.getNome() + ",";
	}
	courses = courses.substring(0, courses.length() - 1);
	return courses;
    }

    @SuppressWarnings("unchecked")
    public static void sendMessageToSpaceManagers(Collection<GenericEvent> events, String description) {
	final String separator = getSeparator(100);
	final Map<Building, Set<GenericEvent>> eventsbyBuilding = getEventsByBuilding(events);
	for (Building building : eventsbyBuilding.keySet()) {
	    final Group managersGroup = building.getSpaceManagementAccessGroup();
	    final String emails = getEmails(building);
	    
	    if (dontHaveRecipients(managersGroup,emails)) {
		continue;
	    }
	    
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
		body = body.substring(0, body.length() - 1);
		body += MESSAGES.getMessage("message.room.reservation.spacemanager.body.sep")
			+ event.getGanttDiagramEventPeriod() + "\n";
	    }
	    body += separator + "\n";
	    body += description;
	    body += separator + "\n";
	    body += getLegend();
	    body += separator + "\n";
	    String subject = getMessageSubject(rooms);
	    sendMessage(getRecipients(managersGroup), emails, subject, body);
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

    private static Collection<String> getRoomsIDs(Set<AllocatableSpace> rooms) {
	Set<String> roomsIDs = new HashSet<String>();
	for (AllocatableSpace room : rooms) {
	    roomsIDs.add(room.getIdentification());
	}
	return roomsIDs;
    }

    private static String getRoomsString(Collection<String> rooms) {
	String header = new String();
	if (rooms.isEmpty()) {
	    return header;
	}
	for (String roomID : rooms) {
	    header += " " + roomID + ",";
	}
	return header.substring(0, header.length() - 1);
    }

    private static String getAllocatableSpaceString(Collection<AllocatableSpace> rooms) {
	String header = new String();
	for (AllocatableSpace room : rooms) {
	    header += " " + room.getIdentification() + ",";
	}
	return header.substring(0, header.length() - 1);
    }

    @Service
    public static void sendMessage(Collection<Recipient> spaceManagers, String email, String subject, String body) {
	SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
	if (email != null || !spaceManagers.isEmpty()) {
	    new Message(systemSender, systemSender.getConcreteReplyTos(), spaceManagers, subject, body, email);
	}
    }

    public static String getSeparator(int len) {
	String separator = "";
	while (len-- > 0) {
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

    private static Collection<String> subtract(Collection<String> a, Collection<String> b) {
	final Collection<String> result = new HashSet<String>();
	for (String s : a) {
	    if (!b.contains(s)) {
		result.add(s);
	    }
	}
	return result;
    }
}
