/*
 * Created on 26/Mar/2003
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Util.TipoCurso;

/**
 * @author João Mota
 */
public interface IPersistentProfessorship extends IPersistentObject {

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

    public IProfessorship readByTeacherAndExecutionCourse(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public IProfessorship readByTeacherIDAndExecutionCourseID(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public void delete(IProfessorship professorship) throws ExcepcaoPersistencia;

    public void deleteAll() throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public IProfessorship readByTeacherAndExecutionCoursePB(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    /**
     * Read ProfessorShips from a teacher, given a type of degree.
     * 
     * @param teacher
     * @param curso
     * @return List of Dominio.IProfessorship
     */
    public List readByTeacherAndTypeOfDegree(ITeacher teacher, TipoCurso degreeType)
            throws ExcepcaoPersistencia;

    public IProfessorship readByTeacherIDandExecutionCourseID(Integer teacherID,
            Integer executionCourseID) throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @param executionPeriod
     * @return
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @param executionYear
     * @return
     */
    public List readByTeacherAndExecutionYear(ITeacher teacher, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    /**
     * @param executionDegrees
     * @return
     */
    public List readByExecutionDegreesAndBasic(List executionDegrees, Boolean basic)
            throws ExcepcaoPersistencia;

    /**
     * @param executionDegree
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia;

    /**
     * @param executionDegree
     * @param basic
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByExecutionDegreeAndBasic(ICursoExecucao executionDegree, Boolean basic)
            throws ExcepcaoPersistencia;

    /**
     * @param executionsDegrees
     * @return
     */
    public List readByExecutionDegrees(List executionDegrees) throws ExcepcaoPersistencia;
}