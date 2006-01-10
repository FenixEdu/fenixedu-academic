package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;

import org.apache.ojb.broker.query.Criteria;

public class ExamOJB extends ObjectFenixOJB implements IPersistentExam {

    public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("dayDate", day.getTime());
        criteria.addEqualTo("beginningDate", beginning.getTime());
        return queryList(Exam.class, criteria);
    }


    public List readByRoomAndExecutionPeriod(String roomName, String executionPeriodName, String year)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedRoomOccupation.room.nome", roomName);
        criteria
                .addEqualTo("associatedExecutionCourses.executionPeriod.name", executionPeriodName);
        criteria.addEqualTo("associatedExecutionCourses.executionPeriod.executionYear.year",year);
        List examsWithRepetition = queryList(Exam.class, criteria);
        List examsWithoutRepetition = new ArrayList();
        for (int i = 0; i < examsWithRepetition.size(); i++) {
            Exam exam = (Exam) examsWithRepetition.get(i);
            if (!examsWithoutRepetition.contains(exam)) {
                examsWithoutRepetition.add(exam);
            }
        }
        return examsWithoutRepetition;
    }

    public List readBy(Calendar day, Calendar beginning, Calendar end) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("dayDate", day.getTime());
        if (beginning != null) {
            criteria.addEqualTo("beginningDate", beginning.getTime());
        }
        if (end != null) {
            criteria.addEqualTo("endDate", end.getTime());
        }
        return queryList(Exam.class, criteria);
    }

    public List readByRoomAndWeek(String roomName, Calendar day) throws ExcepcaoPersistencia {
        List list = new ArrayList();
        day.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
        for (int i = 0; i < 6; i++) {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("dayDate", day.getTime());
            criteria.addEqualTo("associatedRoomOccupation.room.nome", roomName);
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