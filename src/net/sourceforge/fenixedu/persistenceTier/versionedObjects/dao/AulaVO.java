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
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

public class AulaVO extends VersionedObjectsBase implements IAulaPersistente {

    public List readByRoomAndExecutionPeriod(final Integer roomOID, final Integer executionPeriodOID)
            throws ExcepcaoPersistencia {
        final IRoom room = (IRoom) readByOID(Room.class, roomOID);
        final List<ILesson> lessons = new ArrayList<ILesson>();
        for (final IRoomOccupation roomOccupation : ((List<IRoomOccupation>) room.getRoomOccupations())) {
            final ILesson lesson = roomOccupation.getLesson();
            if (lesson != null && lesson.getExecutionPeriod().getIdInternal().equals(executionPeriodOID)) {
                lessons.add(lesson);
            }
        }
        return lessons;
    }

    public List readLessonsByStudent(final String username) throws ExcepcaoPersistencia {
        final List lessons = new ArrayList();

        final List<IShiftStudent> shiftStudents = (List<IShiftStudent>) readAll(ShiftStudent.class);
        for (final IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getStudent().getPerson().getUsername().equalsIgnoreCase(username)) {
                lessons.add(shiftStudent.getShift().getAssociatedLessons());
            }
        }

        return lessons;
    }

}