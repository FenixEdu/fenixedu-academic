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

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.util.TipoAula;

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
            IRoomOccupation roomOccupation = (IRoomOccupation) roomsOccupation.get(i);
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
        crit.addEqualTo("aluno.person.username", username);
        List studentShifts = queryList(ShiftStudent.class, crit);
        if (studentShifts == null) {
            return null;
        }
        List lessons = new ArrayList();
        for (int i = 0; i < studentShifts.size(); i++) {
            IShift shift = ((ShiftStudent) studentShifts.get(i)).getShift();
            List auxLessons = shift.getAssociatedLessons();
            if (auxLessons != null) {
                lessons.addAll(auxLessons);
            }
        }
        return lessons;
    }

    public List readLessonsByShift(IShift shift) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyShift", shift.getIdInternal());
        List lessons = queryList(Lesson.class, crit);
        return lessons;
    }

    public List readLessonsByShiftAndLessonType(IShift shift, TipoAula lessonType)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("keyShift", shift.getIdInternal());
        crit.addEqualTo("tipo", lessonType);
        List lessons = queryList(Lesson.class, crit);
        return lessons;
    }

	public List readLessonsByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyShift", executionCourse.getIdInternal());
        List lessons = queryList(Lesson.class, crit);
        return lessons;
    }

}