/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 * @author Susana Fernades
 * 
 * 21/Jul/2003 fenix-head ServidorPersistente.OJB
 *  
 */
public class SummaryOJB extends PersistentObjectOJB implements IPersistentSummary {

    /**
     *  
     */
    public SummaryOJB() {
    }

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseID);
        return queryList(Summary.class, criteria);

    }

    public List readByExecutionCourseAndType(Integer executionCourseID, ShiftType summaryType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseID);
        criteria.addEqualTo("summaryType", summaryType.name());
        return queryList(Summary.class, criteria);

    }

    public List readByExecutionCourseShifts(Integer executionCourseID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourseID);

        return queryList(Summary.class, criteria);
    }

    public List readByExecutionCourseShiftsAndTypeLesson(Integer executionCourseID,
            ShiftType summaryType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourseID);
        criteria.addEqualTo("shift.tipo", summaryType.name());

        return queryList(Summary.class, criteria);
    }

    public List readByShift(Integer executionCourseID, Integer shiftID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourseID);
        criteria.addEqualTo("shift.idInternal", shiftID);

        return queryList(Summary.class, criteria);
    }

    public List readByTeacher(Integer executionCourseID, Integer teacherNumber)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourseID);
        criteria.addEqualTo("professorship.teacher.teacherNumber", teacherNumber);

        return queryList(Summary.class, criteria);
    }

    public List readByOtherTeachers(Integer executionCourseID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourseID);
        criteria.addIsNull("professorship");

        Criteria criteria2 = new Criteria();
        criteria2.addNotNull("teacher");

        Criteria criteria3 = new Criteria();
        criteria3.addNotNull("teacherName");

        criteria2.addOrCriteria(criteria3);

        criteria.addAndCriteria(criteria2);

        return queryList(Summary.class, criteria);
    }

   
    public ISummary readSummaryByUnique(Integer shiftID, Date summaryDate, Date summaryHour)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.idInternal", shiftID);
        criteria.addEqualTo("summaryDate", summaryDate);
        criteria.addEqualTo("summaryHour", summaryHour);

        return (ISummary) queryObject(Summary.class, criteria);
    }
}