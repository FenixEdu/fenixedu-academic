package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.DiaSemana;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam implements IService {

    public List run(Calendar periodStart, Calendar periodEnd, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer roomOccupationToRemoveId, Integer normalCapacity,
            Integer frequency, Integer weekOfStart, Boolean withLabs) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
        final List<IRoom> rooms;
        if (normalCapacity != null) {
            rooms = persistentRoom.readByNormalCapacity(normalCapacity);
        } else if (withLabs.booleanValue()) {
            rooms = persistentRoom.readAll();
        } else {
            rooms = persistentRoom.readForRoomReservation();
        }

        final List<InfoRoom> availableInfoRooms = new ArrayList<InfoRoom>();
        for (final IRoom room : rooms) {
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

    private boolean isOccupied(final IRoom room, final Calendar periodStart, final Calendar periodEnd,
            final Calendar startTime, final Calendar endTime, final DiaSemana dayOfWeek,
            Integer frequency, Integer weekOfStart, Integer roomOccupationToRemoveId) {
        final List<IRoomOccupation> roomOccupations = room.getRoomOccupations();
        for (final IRoomOccupation roomOccupation : roomOccupations) {
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