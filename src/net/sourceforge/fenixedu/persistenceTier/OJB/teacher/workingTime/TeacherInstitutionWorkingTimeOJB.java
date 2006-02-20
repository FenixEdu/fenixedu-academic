/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher.workingTime;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class TeacherInstitutionWorkingTimeOJB extends PersistentObjectOJB implements
        IPersistentTeacherInstitutionWorkingTime {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readByUnique(Dominio.teacher.workTime.TeacherInstitutionWorkTime)
     */
    public TeacherInstitutionWorkTime readByUnique(
            TeacherInstitutionWorkTime teacherInstitutionWorkTime) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("startTime", teacherInstitutionWorkTime.getStartTime());
        criteria.addEqualTo("keyTeacher", teacherInstitutionWorkTime.getTeacher().getIdInternal());
        criteria.addEqualTo("keyExecutionPeriod", teacherInstitutionWorkTime.getExecutionPeriod()
                .getIdInternal());
        criteria.addEqualTo("weekDay", teacherInstitutionWorkTime.getWeekDay());
        return (TeacherInstitutionWorkTime) queryObject(TeacherInstitutionWorkTime.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readByTeacherAndExecutionPeriod(Dominio.Teacher,
     *      Dominio.ExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(Teacher teacher, ExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyOrientationTeacher", teacher.getIdInternal());
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        return queryList(TeacherInstitutionWorkTime.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readOverlappingPeriod(Dominio.Teacher,
     *      Dominio.ExecutionPeriod, Util.DiaSemana, java.util.Date,
     *      java.util.Date)
     */
    public List readOverlappingPeriod(Integer teacherId, Integer executionPeriodId,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionPeriod", executionPeriodId);
        criteria.addEqualTo("keyOrientationTeacher", teacherId);
        criteria.addEqualTo("weekDay", weekDay);

        Criteria startCriteria = new Criteria();
        startCriteria.addGreaterThan("startTime", startTime);
        startCriteria.addLessThan("startTime", endTime);

        Criteria endCriteria = new Criteria();
        endCriteria.addGreaterThan("endTime", startTime);
        endCriteria.addLessThan("startTime", endTime);

        Criteria equalCriteria = new Criteria();
        equalCriteria.addEqualTo("startTime", startTime);
        equalCriteria.addEqualTo("endTime", endTime);

        Criteria timeCriteria = new Criteria();
        timeCriteria.addOrCriteria(startCriteria);
        timeCriteria.addOrCriteria(endCriteria);
        timeCriteria.addOrCriteria(equalCriteria);

        criteria.addAndCriteria(timeCriteria);

        return queryList(TeacherInstitutionWorkTime.class, criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readByExecutionPeriod(Dominio.ExecutionPeriod)
     */
    public List readByExecutionPeriod(ExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(TeacherInstitutionWorkTime.class, criteria);
    }

}