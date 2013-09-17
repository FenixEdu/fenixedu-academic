package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Collections;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

import org.apache.struts.util.MessageResources;
import org.joda.time.DateTime;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ClosePunctualRoomsOccupationRequest {

    public static final MessageResources messages = MessageResources
            .getMessageResources("resources/ResourceAllocationManagerResources");

    @Atomic
    public static void run(PunctualRoomsOccupationRequest request, Person person) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (request != null) {
            request.closeRequestAndAssociateOwnerOnlyForEmployees(new DateTime(), person);
            sendCloseRequestMessage(request);
            final Person requestor = request.getRequestor();
            final String personName = requestor.getName();
            final String contactString = requestor.getDefaultPhoneNumber() + "/" + requestor.getDefaultEmailAddressValue();
        }
    }

    private static void sendCloseRequestMessage(PunctualRoomsOccupationRequest roomsReserveRequest) {
        MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");
        String body =
                messages.getMessage("message.room.reservation.solved") + "\n\n"
                        + messages.getMessage("message.room.reservation.request.number") + "\n"
                        + roomsReserveRequest.getIdentification() + "\n\n";
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

        if (punctualRoomsOccupationComment != null) {
            body += punctualRoomsOccupationComment.getDescription();
        } else {
            body += "-";
        }
        GOPSendMessageService.sendMessage(Collections.EMPTY_LIST, roomsReserveRequest.getRequestor()
                .getDefaultEmailAddressValue(), messages.getMessage("message.room.reservation"), body);
    }

    private static PunctualRoomsOccupationComment getLastComment(PunctualRoomsOccupationRequest roomsReserveRequest) {
        PunctualRoomsOccupationComment last = null;
        for (Iterator<PunctualRoomsOccupationComment> iterator =
                roomsReserveRequest.getCommentsWithoutFirstCommentOrderByDate().iterator(); iterator.hasNext();) {
            last = iterator.next();
        }
        return last;
    }

}