/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorPersistente.teacher.professorship;

import java.util.Date;
import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public interface IPersistentSupportLesson extends IPersistentObject
{

    public List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia;

    /**
	 * @param lesson
	 * @return
	 */
    public ISupportLesson readByUnique(ISupportLesson lesson) throws ExcepcaoPersistencia;

    /**
	 * @param teacher
	 * @param executionPeriod
	 * @param weekDay
	 * @param startTime
	 * @param endTime
	 * @return
	 */
    public List readOverlappingPeriod(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        DiaSemana weekDay,
        Date startTime,
        Date endTime)
        throws ExcepcaoPersistencia;

    /**
	 * @param teacher
	 * @param executionPeriod
	 * @return
	 */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia;

    public void delete(ISupportLesson supportLesson) throws ExcepcaoPersistencia;
}
