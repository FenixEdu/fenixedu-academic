/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorPersistente.teacher.workingTime;

import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentTeacherInstitutionWorkingTime extends IPersistentObject
{

    ITeacherInstitutionWorkTime readByUnique(ITeacherInstitutionWorkTime teacherInstitutionWorkTime)
        throws ExcepcaoPersistencia;

	List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia;
    
}