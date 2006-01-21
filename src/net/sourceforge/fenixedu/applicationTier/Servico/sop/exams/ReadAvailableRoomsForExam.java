package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam extends Service {

    public List run(Calendar periodStart, Calendar periodEnd, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer roomOccupationToRemoveId, Integer normalCapacity,
            Integer frequency, Integer weekOfStart, Boolean withLabs) throws ExcepcaoPersistencia {
        final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
        final List<Room> rooms;
        if (normalCapacity != null) {
            rooms = persistentRoom.readByNormalCapacity(normalCapacity);
        } else if (withLabs.booleanValue()) {
            rooms = persistentRoom.readAll();
        } else {
            rooms = persistentRoom.readForRoomReservation();
        }

        final List<InfoRoom> availableInfoRooms = new ArrayList<InfoRoom>();
        for (final Room room : rooms) {
            if (room.getNome().equals("QA")) {
                System.out.println();
            }
            if (!isOccupied(room, periodStart, periodEnd, startTime, endTime, dayOfWeek, frequency,
                    weekOfStart, roomOccupationToRemoveId)) {
                availableInfoRooms.add(InfoRoom.newInfoFromDomain(room));
            }
        }

        return availableInfoRooms;
    }

    private boolean isOccupied(final Room room, final Calendar periodStart, final Calendar periodEnd,
            final Calendar startTime, final Calendar endTime, final DiaSemana dayOfWeek,
            Integer frequency, Integer weekOfStart, Integer roomOccupationToRemoveId) {
        final List<RoomOccupation> roomOccupations = room.getRoomOccupations();
        for (final RoomOccupation roomOccupation : roomOccupations) {
            if (!roomOccupation.getIdInternal().equals(roomOccupationToRemoveId)) {
                boolean isOccupied = roomOccupation.roomOccupationForDateAndTime(periodStart,
                        periodEnd, startTime, endTime, dayOfWeek, frequency, weekOfStart);
                if (isOccupied) {
                    return true;
                }
            }
        }
        return false;
    }

}