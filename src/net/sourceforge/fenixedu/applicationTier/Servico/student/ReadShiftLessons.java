/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * Service ReadShiftSignup
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadShiftLessons implements IService {

    public Object run(final InfoShift infoShift) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

        final Shift shift = (Shift) persistentShift.readByOID(Shift.class, infoShift.getIdInternal());
        return CollectionUtils.collect(shift.getAssociatedLessons(), new Transformer() {

            public Object transform(Object arg0) {
                final Lesson lesson = (Lesson) arg0;
                final RoomOccupation roomOccupation = lesson.getRoomOccupation();
                final Room room = roomOccupation.getRoom();

                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoLesson.setInfoShift(infoShift);
                final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
                infoLesson.setInfoRoomOccupation(infoRoomOccupation);
                final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
                infoRoomOccupation.setInfoRoom(infoRoom);

                return infoLesson;
            }
            
        });
    }

}