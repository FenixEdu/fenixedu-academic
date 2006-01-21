/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

public class DeleteLessons extends Service {

    public Boolean run(final List lessonOIDs) throws ExcepcaoPersistencia {
        for (int j = 0; j < lessonOIDs.size(); j++) {
            final Integer lessonOID = (Integer) lessonOIDs.get(j);
            deleteLesson(persistentSupport, lessonOID);
        }

        return new Boolean(true);
    }

    public static void deleteLesson(final ISuportePersistente persistentSupport, final Integer lessonOID)
            throws ExcepcaoPersistencia {
        final Lesson lesson = (Lesson) persistentObject.readByOID(Lesson.class, lessonOID);
        deleteLesson(persistentSupport, lesson);
    }

    public static void deleteLesson(final ISuportePersistente persistentSupport, final Lesson lesson)
            throws ExcepcaoPersistencia {
        final IPersistentRoomOccupation persistentRoomOccupation = persistentSupport
                .getIPersistentRoomOccupation();

        final RoomOccupation roomOccupation = lesson.getRoomOccupation();

        roomOccupation.setPeriod(null);
        roomOccupation.setRoom(null);

        lesson.setShift(null);
        lesson.setRoomOccupation(null);
        lesson.setSala(null);
        lesson.setExecutionPeriod(null);

        persistentRoomOccupation.deleteByOID(RoomOccupation.class, roomOccupation.getIdInternal());
        persistentObject.deleteByOID(Lesson.class, lesson.getIdInternal());
    }

}