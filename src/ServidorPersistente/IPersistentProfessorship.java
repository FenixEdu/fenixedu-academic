/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

/**
 * @author João Mota
 * 
 *  
 */
public interface IPersistentProfessorship extends IPersistentObject
{

    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
    public IProfessorship readByTeacherAndExecutionCourse(
        ITeacher teacher,
        IDisciplinaExecucao executionCourse)
        throws ExcepcaoPersistencia;
    public IProfessorship readByTeacherIDAndExecutionCourseID(ITeacher teacher, IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
    
    public List readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
    public void delete(IProfessorship professorship) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
    public void lockWrite(IProfessorship professorship)
        throws ExcepcaoPersistencia, ExistingPersistentException;
    public List readAll() throws ExcepcaoPersistencia;
    public IProfessorship readByTeacherAndExecutionCoursePB(
        ITeacher teacher,
        IDisciplinaExecucao executionCourse)
        throws ExcepcaoPersistencia;
    /**
     * Read ProfessorShips from a teacher, given a type of degree.
     * @param teacher
     * @param curso
     * @return List of  Dominio.IProfessorship
     */
    public List readByTeacherAndTypeOfDegree(ITeacher teacher, TipoCurso degreeType) throws ExcepcaoPersistencia;

}
