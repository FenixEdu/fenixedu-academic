/*
 * ITurnoPersistente.java Created on 17 de Outubro de 2002, 19:32
 */

package ServidorPersistente;

/**
 * @author tfc130
 */
import java.util.List;

import Dominio.ILesson;
import Dominio.ICurricularYear;
import Dominio.IExecutionDegree;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ISchoolClass;
import Dominio.IShift;

public interface ITurnoPersistente extends IPersistentObject {

    public IShift readByNameAndExecutionCourse(String nome, IExecutionCourse IDE)
            throws ExcepcaoPersistencia;

    public void delete(IShift turno) throws ExcepcaoPersistencia;

    public Integer countAllShiftsOfAllClassesAssociatedWithShift(IShift shift)
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
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree,
            ICurricularYear curricularYear) throws ExcepcaoPersistencia;

    /**
     * @param shcoolClass
     * @return
     */
    public List readAvailableShiftsForClass(ISchoolClass schoolClass) throws ExcepcaoPersistencia;

    public List readByExecutionCourseID(Integer id) throws ExcepcaoPersistencia;

    /**
     * @return
     */
    public List readByLesson(ILesson lesson) throws ExcepcaoPersistencia;

    public IShift readShiftByLesson(ILesson lesson) throws ExcepcaoPersistencia;

    public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(IStudent student,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

}