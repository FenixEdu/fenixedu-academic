/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;

/**
 * @author João Mota
 * 
 *  
 */
public interface IPersistentResponsibleFor extends IPersistentObject {
    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

    public IResponsibleFor readByTeacherAndExecutionCourse(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public IResponsibleFor readByTeacherAndExecutionCoursePB(ITeacher teacher,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public List readByExecutionCourseID(Integer executionCourseID) throws ExcepcaoPersistencia;

    public void delete(IResponsibleFor responsibleFor) throws ExcepcaoPersistencia;

    public void deleteAll() throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

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
     * @param teacher
     * @param responsabilitiesToRemove
     * @return
     */
    public List readByTeacherAndExecutionCourseIds(ITeacher teacher, List executionCourseIds)
            throws ExcepcaoPersistencia;

    public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia;
}