package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam implements IService {

    public List run(IPeriod period, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek,
            IRoomOccupation roomOccupationToRemove, Integer normalCapacity, Integer frequency,
            Integer weekOfStart, Boolean withLabs) throws FenixServiceException {

        Transformer TRANSFORM_TO_INFOROOM = new Transformer() {
            public Object transform(Object input) {
                return Cloner.copyRoom2InfoRoom((IRoom) input);
            }
        };

        List availableRooms = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentRoomOccupation persistentRoomOccupation = sp.getIPersistentRoomOccupation();
            List roomOccupations = persistentRoomOccupation.readAll();

            if (roomOccupationToRemove != null) {
                roomOccupations.remove(roomOccupationToRemove);
            }
            List occupiedInfoRooms = new ArrayList();

            for (int iterRO = 0; iterRO < roomOccupations.size(); iterRO++) {
                IRoomOccupation roomOccupation = (IRoomOccupation) roomOccupations.get(iterRO);
                boolean occupiedRO = roomOccupation.roomOccupationForDateAndTime(period, startTime,
                        endTime, dayOfWeek, frequency, weekOfStart);

                if (occupiedRO) {
                    InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(roomOccupation.getRoom());
                    occupiedInfoRooms.add(infoRoom);
                }

            }

            ISalaPersistente persistentRoom = sp.getISalaPersistente();
            List rooms;
            if (normalCapacity != null) {
                rooms = persistentRoom.readByNormalCapacity(normalCapacity);
            } else if (withLabs.booleanValue()) {
                rooms = persistentRoom.readAll();
            } else {
                rooms = persistentRoom.readForRoomReservation();
            }

            List allInfoRooms = (List) CollectionUtils.collect(rooms, TRANSFORM_TO_INFOROOM);
            availableRooms = (List) CollectionUtils.subtract(allInfoRooms, occupiedInfoRooms);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return availableRooms;
    }

}