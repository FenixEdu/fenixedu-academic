/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorPersistente.OJB.teacher.workingTime;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import Dominio.teacher.workTime.TeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class TeacherInstitutionWorkingTimeOJB extends ObjectFenixOJB
        implements
            IPersistentTeacherInstitutionWorkingTime
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readByUnique(Dominio.teacher.workTime.ITeacherInstitutionWorkTime)
	 */
    public ITeacherInstitutionWorkTime readByUnique(
            ITeacherInstitutionWorkTime teacherInstitutionWorkTime) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("startTime", teacherInstitutionWorkTime.getStartTime());
        criteria.addEqualTo("keyTeacher", teacherInstitutionWorkTime.getTeacher().getIdInternal());
        criteria.addEqualTo("keyExecutionPeriod", teacherInstitutionWorkTime.getExecutionPeriod()
                .getIdInternal());
        criteria.addEqualTo("weekDay", teacherInstitutionWorkTime.getWeekDay());
        return (ITeacherInstitutionWorkTime) queryObject(TeacherInstitutionWorkTime.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
	 *          Dominio.IExecutionPeriod)
	 */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        return queryList(TeacherInstitutionWorkTime.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readOverlappingPeriod(Dominio.ITeacher,
	 *          Dominio.IExecutionPeriod, Util.DiaSemana, java.util.Date, java.util.Date)
	 */
    public List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
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

}