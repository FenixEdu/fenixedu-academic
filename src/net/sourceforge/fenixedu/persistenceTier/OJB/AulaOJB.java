/*
 * AulaOJB.java
 *
 * Created on 18 de Outubro de 2002, 00:34
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;

import org.apache.ojb.broker.query.Criteria;

public class AulaOJB extends ObjectFenixOJB implements IAulaPersistente {

    public List readByRoomAndExecutionPeriod(final Integer roomOID, final Integer executionPeriodOID)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyRoom", roomOID);
        List roomsOccupation = queryList(RoomOccupation.class, crit);
        ArrayList lessonList = new ArrayList();
        for (int i = 0; i < roomsOccupation.size(); i++) {
            crit = new Criteria();
            RoomOccupation roomOccupation = (RoomOccupation) roomsOccupation.get(i);
            crit.addEqualTo("keyRoomOccupation", roomOccupation.getIdInternal());
            crit.addEqualTo("keyExecutionPeriod", executionPeriodOID);
            Lesson lesson = (Lesson) queryObject(Lesson.class, crit);
            if (lesson != null) {
                lessonList.add(lesson);
            }
        }
        return lessonList;
    }

    public List readLessonsByStudent(String username) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", username);
        Student student = (Student) queryList(Student.class, crit);
        List<Shift> shifts = student.getShifts();
        List lessons = new ArrayList();
        for(Shift shift : shifts){
            List auxLessons = shift.getAssociatedLessons();
            if (auxLessons != null) {
                lessons.addAll(auxLessons);
            }
        }
        
        return lessons;
    }

}