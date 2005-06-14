package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPeriod;
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

    public List run(IPeriod period, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek,
            IRoomOccupation roomOccupationToRemove, Integer normalCapacity, Integer frequency,
            Integer weekOfStart, Boolean withLabs) throws ExcepcaoPersistencia {

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

        final List availableInfoRooms = new ArrayList();
        for (final IRoom room : rooms) {
            if (!isOccupied(room, period, startTime, endTime, dayOfWeek, frequency, weekOfStart)) {
                availableInfoRooms.add(Cloner.copyRoom2InfoRoom(room));
            }
        }

        if (roomOccupationToRemove != null
                && roomOccupationToRemove.roomOccupationForDateAndTime(period, startTime, endTime,
                        dayOfWeek, frequency, weekOfStart)) {
            availableInfoRooms.add(Cloner.copyRoom2InfoRoom(roomOccupationToRemove.getRoom()));
        }

        return availableInfoRooms;
    }

    private boolean isOccupied(final IRoom room, final IPeriod period, final Calendar startTime,
            final Calendar endTime, final DiaSemana dayOfWeek, Integer frequency, Integer weekOfStart) {
        final List<IRoomOccupation> roomOccupations = room.getRoomOccupations();
        for (final IRoomOccupation roomOccupation : roomOccupations) {
            boolean isOccupied = roomOccupation.roomOccupationForDateAndTime(period, startTime, endTime,
                    dayOfWeek, frequency, weekOfStart);
            if (isOccupied) {
                return true;
            }
        }
        return false;
    }

}