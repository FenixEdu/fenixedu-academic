/*
 * ITurnoPersistente.java Created on 17 de Outubro de 2002, 19:32
 */

package ServidorPersistente;

/**
 * @author tfc130
 */
import java.util.List;

import Dominio.IAula;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;

public interface ITurnoPersistente extends IPersistentObject {

    public ITurno readByNameAndExecutionCourse(String nome, IExecutionCourse IDE)
            throws ExcepcaoPersistencia;

    public void delete(ITurno turno) throws ExcepcaoPersistencia;

    public Integer countAllShiftsOfAllClassesAssociatedWithShift(ITurno shift)
            throws ExcepcaoPersistencia;

    // FIXME : O metodo nao seleciona bem as turmas ... mas nao da erro na query
    // e usa o associatedCurricularCourses
    public List readByDisciplinaExecucao(String sigla, String anoLectivo, String siglaLicenciatura)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourseAndType(IExecutionCourse executionCourse, Integer type)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    /**
     * @param executionDegree
     * @param curricularYear
     * @return
     */
    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(
            IExecutionPeriod executionPeriod, ICursoExecucao executionDegree,
            ICurricularYear curricularYear) throws ExcepcaoPersistencia;

    /**
     * @param shcoolClass
     * @return
     */
    public List readAvailableShiftsForClass(ITurma schoolClass) throws ExcepcaoPersistencia;

    public List readByExecutionCourseID(Integer id) throws ExcepcaoPersistencia;

    /**
     * @return
     */
    public List readByLesson(IAula lesson) throws ExcepcaoPersistencia;

    public ITurno readShiftByLesson(IAula lesson) throws ExcepcaoPersistencia;

    public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(IStudent student,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

}