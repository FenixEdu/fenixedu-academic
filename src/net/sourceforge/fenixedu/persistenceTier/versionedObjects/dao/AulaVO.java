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

import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.space.IRoom;
import net.sourceforge.fenixedu.domain.space.IRoomOccupation;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class AulaVO extends VersionedObjectsBase implements IAulaPersistente {

    public List readByRoomAndExecutionPeriod(final Integer roomOID, final Integer executionPeriodOID)
            throws ExcepcaoPersistencia {
        final IRoom room = (IRoom) readByOID(Room.class, roomOID);
        final List<ILesson> lessons = new ArrayList<ILesson>();
        for (final IRoomOccupation roomOccupation : room.getRoomOccupations()) {
            final ILesson lesson = roomOccupation.getLesson();
            if (lesson != null && lesson.getExecutionPeriod().getIdInternal().equals(executionPeriodOID)) {
                lessons.add(lesson);
            }
        }
        return lessons;
    }

    public List readLessonsByStudent(final String username) throws ExcepcaoPersistencia {
        final List lessons = new ArrayList();

        List<IStudent> students = (List<IStudent>) readAll(Student.class);
        for(IStudent student : students){
            if(student.getPerson().getUsername().equalsIgnoreCase(username)){
                List<IShift> shifts = student.getShifts();
                for(IShift shift : shifts){
                    List auxLessons = shift.getAssociatedLessons();
                    if (auxLessons != null) {
                        lessons.addAll(auxLessons);
                    }
                }
                break;
            }
        }

        return lessons;
    }

}