/*
 * ExamOJB.java
 *
 * Created on 2003/03/19
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ExamVO extends VersionedObjectsBase implements IPersistentExam {

    public Collection readAll(final Class clazz) {
        final Collection evaluations = super.readAll(clazz);
        CollectionUtils.filter(evaluations, new Predicate() {
            public boolean evaluate(Object arg0) {
                final Evaluation evaluation = (Evaluation) arg0;
                return evaluation.getOjbConcreteClass().equals(Exam.class.getName());
            }});
        return evaluations;
    }

    public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {

        List<Exam> exams = (List<Exam>) readAll(Exam.class);
        List<Exam> result = new ArrayList();
        for (Exam exam : exams) {
            if (exam.getBeginning().equals(beginning) && exam.getDay().equals(day)) {
                result.add(exam);
            }

        }
        return result;
    }

    public List readByRoomAndExecutionPeriod(String roomName, String executionPeriodName, String year)
            throws ExcepcaoPersistencia {
        List<ExecutionYear> executionYears = (List<ExecutionYear>) readAll(ExecutionYear.class);
        ExecutionYear executionYearResult = null;
        ExecutionPeriod executionPeriodResult = null;

        for (ExecutionYear executionYear : executionYears) {
            if (executionYear.getYear().equals(year)) {
                executionYearResult = executionYear;
                break;
            }
        }

        List<ExecutionPeriod> executionPeriods = executionYearResult.getExecutionPeriods();
        for (ExecutionPeriod executionPeriod : executionPeriods) {
            if (executionPeriod.getName().equalsIgnoreCase(executionPeriodName)) {
                executionPeriodResult = executionPeriod;
                break;
            }
        }

        List<ExecutionCourse> executionCourses = executionPeriodResult.getAssociatedExecutionCourses();
        List<Exam> result = new ArrayList();

        HashSet hashSet = new HashSet(75);

        for (ExecutionCourse executionCourse : executionCourses) {
            List<Exam> exams = new ArrayList();
            List<Evaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
            for(Evaluation evaluation : associatedEvaluations){
                if (evaluation instanceof Exam){
                    exams.add((Exam) evaluation);
                }
            }
            for (Exam exam : exams) {
                List<Room> rooms = exam.getAssociatedRooms();
                for (Room room : rooms) {
                    if (room.getNome().equalsIgnoreCase(roomName)) {

                        if (!hashSet.contains(exam.getIdInternal())) {
                            result.add(exam);
                            hashSet.add(exam.getIdInternal());
                        }

                    }
                }
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExam#readBy(java.util.Calendar,
     *      java.util.Calendar, java.util.Calendar)
     */
    public List readBy(Calendar day, Calendar beginning, Calendar end) throws ExcepcaoPersistencia {
        List<Exam> exams = (List<Exam>) readAll(Exam.class);
        List<Exam> result = new ArrayList();
        if (beginning != null) {
            if (end != null) {
                for (Exam exam : exams) {
                    if (exam.getBeginning().equals(beginning) && exam.getDay().equals(day)
                            && exam.getEnd().equals(end)) {
                        result.add(exam);
                    }
                }
            } else {
                for (Exam exam : exams) {
                    if (exam.getBeginning().equals(beginning) && exam.getDay().equals(day)) {
                        result.add(exam);
                    }
                }
            }
        } else {
            if (end != null) {
                for (Exam exam : exams) {
                    if (exam.getDay().equals(day) && exam.getEnd().equals(end)) {
                        result.add(exam);
                    }
                }
            } else {
                for (Exam exam : exams) {
                    if (exam.getDay().equals(day)) {
                        result.add(exam);
                    }
                }
            }
        }
        return result;

    }

    public List readByRoomAndWeek(String roomName, Calendar day) throws ExcepcaoPersistencia {
        List<Exam> result = new ArrayList();
        List<Exam> exams = (List<Exam>) readAll(Exam.class);

        // day.add(Calendar.DATE, Calendar.MONDAY -
        // day.get(Calendar.DAY_OF_WEEK));
        Calendar endDay = Calendar.getInstance();
        endDay.setTime(day.getTime());
        endDay.add(Calendar.DATE, 6);

        for (Exam exam : exams) {
            if (!(exam.getDay().before(day) || exam.getDay().after(endDay))) {
                List<RoomOccupation> roomOccupations = exam.getAssociatedRoomOccupation();
                for (RoomOccupation roomOccupation : roomOccupations) {
                    if (roomOccupation.getRoom().getNome().equals(roomName)) {
                        result.add(exam);

                    }
                }
            }
        }
        return result;
    }

    public boolean isExamOfExecutionCourseTheStudentAttends(Integer examOID, String studentsUsername)
            throws ExcepcaoPersistencia {
        Exam exam = (Exam) readByOID(Exam.class, examOID);

        for (ExecutionCourse executionCourse : exam.getAssociatedExecutionCourses()) {
            for (final Attends attends : executionCourse.getAttends()) {
                final Student student = attends.getAluno();
                if (student.getPerson().getUsername().equals(studentsUsername)) {
                    return true;
                }
            }

        }
        return false;
    }

}