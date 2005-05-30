/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class SupportLessonOJB extends PersistentObjectOJB implements IPersistentSupportLesson {

    public List readByProfessorship(Integer professorshipID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", professorshipID);
        return queryList(SupportLesson.class, criteria);
    }

    public ISupportLesson readByUnique(Integer professorshipID, DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", professorshipID);
        criteria.addEqualTo("weekDay", weekDay);
        criteria.addEqualTo("startTime", startTime);
        criteria.addEqualTo("endTime", endTime);
        return (ISupportLesson) queryObject(SupportLesson.class, criteria);
    }

    public List readOverlappingPeriod(Integer teacherID, Integer executionPeriodID,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.executionCourse.keyExecutionPeriod", executionPeriodID);
        criteria.addEqualTo("professorship.keyTeacher", teacherID);
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

        return queryList(SupportLesson.class, criteria);
    }
}