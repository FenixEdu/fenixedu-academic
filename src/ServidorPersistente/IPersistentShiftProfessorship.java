/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.Date;
import java.util.List;

import Util.DiaSemana;
import Util.TipoCurso;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IShiftProfessorship;
import Dominio.ITeacher;
import Dominio.ITurno;

/**
 * @author jpvl
 */
public interface IPersistentShiftProfessorship extends IPersistentObject {
	IShiftProfessorship readByUnique(IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia;
	void delete (IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia;
    IShiftProfessorship readByProfessorshipAndShift(IProfessorship professorship, ITurno shift) throws ExcepcaoPersistencia;
    /**
     * @param teacher
     * @param executionPeriod
     * @param weekDay
     * @param startTime
     * @param endTime
     * @return
     */
    List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod, DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia;
    /**
     * @param teacher
     * @param executionPeriod
     * @return
     */
    List readByTeacherAndExecutionPeriodAndDegreeType(ITeacher teacher, IExecutionPeriod executionPeriod, TipoCurso curso) throws ExcepcaoPersistencia;
}
