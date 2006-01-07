/*
 * AulaOJB.java
 *
 * Created on 18 de Outubro de 2002, 00:34
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class AulaVO extends VersionedObjectsBase implements IAulaPersistente {

    public List readByRoomAndExecutionPeriod(final Integer roomOID, final Integer executionPeriodOID)
            throws ExcepcaoPersistencia {
        final Room room = (Room) readByOID(Room.class, roomOID);
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (final RoomOccupation roomOccupation : room.getRoomOccupations()) {
            final Lesson lesson = roomOccupation.getLesson();
            if (lesson != null && lesson.getExecutionPeriod().getIdInternal().equals(executionPeriodOID)) {
                lessons.add(lesson);
            }
        }
        return lessons;
    }

}