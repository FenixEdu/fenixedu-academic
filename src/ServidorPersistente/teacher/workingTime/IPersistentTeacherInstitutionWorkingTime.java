/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorPersistente.teacher.workingTime;

import java.util.Date;
import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public interface IPersistentTeacherInstitutionWorkingTime extends IPersistentObject
{

    ITeacherInstitutionWorkTime readByUnique(ITeacherInstitutionWorkTime teacherInstitutionWorkTime)
        throws ExcepcaoPersistencia;

	List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @param executionPeriod
     * @param weekDay
     * @param startTime
     * @param endTime
     */
    List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod, DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @return
     */
    List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia; 
    
}