/*
 * ExamOJB.java
 *
 * Created on 2003/03/19
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;

import org.apache.ojb.broker.query.Criteria;

public class ExamOJB extends ObjectFenixOJB implements IPersistentExam {

    public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("day", day);
        criteria.addEqualTo("beginning", beginning);
        return queryList(Exam.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Exam.class, null, "season", true);

    }

    public void delete(IExam exam) throws ExcepcaoPersistencia {

        /*
         * Criteria criteria = new Criteria(); criteria.addEqualTo("keyExam",
         * exam.getIdInternal()); List examEnrollments =
         * queryList(ExamStudentRoom.class, criteria); if (examEnrollments !=
         * null && !examEnrollments.isEmpty()) { throw new
         * notAuthorizedPersistentDeleteException(); } else {
         * 
         * List associatedExecutionCourses =
         * exam.getAssociatedExecutionCourses();
         * 
         * if (associatedExecutionCourses != null) { for (int i = 0; i <
         * associatedExecutionCourses.size(); i++) { IExecutionCourse
         * executionCourse = (IExecutionCourse)
         * associatedExecutionCourses.get(i);
         * executionCourse.getAssociatedExams().remove(exam);
         * 
         * IExamExecutionCourse examExecutionCourseToDelete =
         * PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentExamExecutionCourse().readBy(
         * exam, executionCourse);
         * 
         * PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentExamExecutionCourse().delete(
         * examExecutionCourseToDelete); } }
         * 
         * exam.setAssociatedExecutionCourses(null);
         * 
         * super.delete(exam); }
         */
        super.delete(exam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExam#readBy(Dominio.IRoom,
     *      Dominio.IExecutionPeriod)
     */
    /**
     * 
     * @deprecated
     */
    public List readBy(IRoom room, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedRooms.nome", room.getNome());
        criteria
                .addEqualTo("associatedExecutionCourses.executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("associatedExecutionCourses.executionPeriod.executionYear.year",
                executionPeriod.getExecutionYear().getYear());
        List examsWithRepetition = queryList(Exam.class, criteria);
        List examsWithoutRepetition = new ArrayList();
        for (int i = 0; i < examsWithRepetition.size(); i++) {
            IExam exam = (IExam) examsWithRepetition.get(i);
            if (!examsWithoutRepetition.contains(exam)) {
                examsWithoutRepetition.add(exam);
            }
        }
        return examsWithoutRepetition;
    }

    public List readByRoomAndExecutionPeriod(IRoom room, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedRoomOccupation.room.nome", room.getNome());
        criteria
                .addEqualTo("associatedExecutionCourses.executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("associatedExecutionCourses.executionPeriod.executionYear.year",
                executionPeriod.getExecutionYear().getYear());
        List examsWithRepetition = queryList(Exam.class, criteria);
        List examsWithoutRepetition = new ArrayList();
        for (int i = 0; i < examsWithRepetition.size(); i++) {
            IExam exam = (IExam) examsWithRepetition.get(i);
            if (!examsWithoutRepetition.contains(exam)) {
                examsWithoutRepetition.add(exam);
            }
        }
        return examsWithoutRepetition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExam#readBy(java.util.Calendar,
     *      java.util.Calendar, java.util.Calendar)
     */
    public List readBy(Calendar day, Calendar beginning, Calendar end) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("day", day);
        if (beginning != null) {
            criteria.addEqualTo("beginning", beginning);
        }
        if (end != null) {
            criteria.addEqualTo("end", end);
        }
        return queryList(Exam.class, criteria);
    }

    public List readByRoomAndWeek(IRoom room, Calendar day) throws ExcepcaoPersistencia {
        List list = new ArrayList();
        day.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
        for (int i = 0; i < 6; i++) {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("day", day);
            criteria.addEqualTo("associatedRoomOccupation.room.nome", room.getNome());
            list.addAll(queryList(Exam.class, criteria));
            day.add(Calendar.DATE, 1);
        }
        return list;
    }

    public boolean isExamOfExecutionCourseTheStudentAttends(Integer examOID, String studentsUsername) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", examOID);
        criteria.addEqualTo("associatedExecutionCourses.attendingStudents.person.username",
                studentsUsername);
        return count(Exam.class, criteria) != 0;
    }

}