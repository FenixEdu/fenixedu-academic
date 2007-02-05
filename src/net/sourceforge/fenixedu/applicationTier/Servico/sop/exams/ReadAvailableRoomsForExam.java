package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoSala;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam extends Service {

    public List run(Calendar periodStart, Calendar periodEnd, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer roomOccupationToRemoveId, Integer normalCapacity,            
            Integer frequency, Integer weekOfStart, Boolean withLabs) throws ExcepcaoPersistencia {
	
        final Collection<OldRoom> rooms = new HashSet<OldRoom>();
        if (normalCapacity != null) {
            rooms.addAll(OldRoom.findOldRoomsWithNormalCapacity(normalCapacity));
        } else if (withLabs.booleanValue()) {
        	rooms.addAll(OldRoom.getOldRooms());
        } else {
        	rooms.addAll(OldRoom.findOldRoomsOfAnyOtherType(new TipoSala(TipoSala.LABORATORIO)));
        }

        // This is a code optomization to avoid materializing all room occupations. Here we just materialize all periods,
        // and the room occupations in them contained. Most of the periods were already being materialized anyways.
        // This also reduced the comparisons that are performed.
        for (final OccupationPeriod period : rootDomainObject.getOccupationPeriodsSet()) {
        	if (period.intersectPeriods(periodStart, periodEnd)) {
        		for (final RoomOccupation roomOccupation : period.getRoomOccupationsSet()) {
        			if (!roomOccupation.getIdInternal().equals(roomOccupationToRemoveId)) {
        				final Room room = roomOccupation.getRoom();
        				if (rooms.contains(room) && roomOccupation.roomOccupationForDateAndTime(periodStart, periodEnd, startTime, endTime, dayOfWeek, frequency, weekOfStart)) {
        					rooms.remove(roomOccupation.getRoom());
        				}
        			}
        		}
        	}
        }

        final List<InfoRoom> availableInfoRooms = new ArrayList<InfoRoom>();
        for (final OldRoom room : rooms) {
//            if (!isOccupied(room, periodStart, periodEnd, startTime, endTime, dayOfWeek, frequency,
//                    weekOfStart, roomOccupationToRemoveId)) {
                availableInfoRooms.add(InfoRoom.newInfoFromDomain(room));
//            }
        }

        return availableInfoRooms;
    }

//    private boolean isOccupied(final OldRoom room, final Calendar periodStart, final Calendar periodEnd,
//            final Calendar startTime, final Calendar endTime, final DiaSemana dayOfWeek,
//            Integer frequency, Integer weekOfStart, Integer roomOccupationToRemoveId) {
//        final List<RoomOccupation> roomOccupations = room.getRoomOccupations();
//        for (final RoomOccupation roomOccupation : roomOccupations) {
//            if (!roomOccupation.getIdInternal().equals(roomOccupationToRemoveId)) {
//                boolean isOccupied = roomOccupation.roomOccupationForDateAndTime(periodStart,
//                        periodEnd, startTime, endTime, dayOfWeek, frequency, weekOfStart);
//                if (isOccupied) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

}