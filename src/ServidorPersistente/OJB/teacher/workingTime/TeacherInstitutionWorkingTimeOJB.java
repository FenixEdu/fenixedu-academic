/*
 * Created on Nov 25, 2003 by jpvl
 *
 */
package ServidorPersistente.OJB.teacher.workingTime;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import Dominio.teacher.workTime.TeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

/**
 * @author jpvl
 */
public class TeacherInstitutionWorkingTimeOJB extends ObjectFenixOJB implements IPersistentTeacherInstitutionWorkingTime
{

    /* (non-Javadoc)
     * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readByUnique(Dominio.teacher.workTime.ITeacherInstitutionWorkTime)
     */
    public ITeacherInstitutionWorkTime readByUnique(ITeacherInstitutionWorkTime teacherInstitutionWorkTime) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria ();
        criteria.addEqualTo("startTime", teacherInstitutionWorkTime.getStartTime());
        criteria.addEqualTo("keyTeacher", teacherInstitutionWorkTime.getTeacher().getIdInternal());
		criteria.addEqualTo("keyExecutionPeriod", teacherInstitutionWorkTime.getExecutionPeriod().getIdInternal());
		criteria.addEqualTo("weekDay", teacherInstitutionWorkTime.getWeekDay());
        return (ITeacherInstitutionWorkTime) queryObject(TeacherInstitutionWorkTime.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime#readByTeacherAndExecutionPeriod(Dominio.ITeacher, Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
		Criteria criteria = new Criteria ();
		criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
		criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
		return queryList(TeacherInstitutionWorkTime.class, criteria);
    }

}
