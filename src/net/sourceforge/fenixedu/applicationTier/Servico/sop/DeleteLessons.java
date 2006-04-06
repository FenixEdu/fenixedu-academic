/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
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
        final Lesson lesson = rootDomainObject.readLessonByOID(lessonOID);
        deleteLesson(persistentSupport, lesson);
    }

    public static void deleteLesson(final ISuportePersistente persistentSupport, final Lesson lesson)
            throws ExcepcaoPersistencia {
        final RoomOccupation roomOccupation = lesson.getRoomOccupation();

        roomOccupation.setPeriod(null);
        roomOccupation.setRoom(null);

        lesson.setShift(null);
        lesson.setRoomOccupation(null);
        lesson.setSala(null);
        lesson.setExecutionPeriod(null);

        roomOccupation.delete();
        persistentObject.deleteByOID(Lesson.class, lesson.getIdInternal());
    }

}