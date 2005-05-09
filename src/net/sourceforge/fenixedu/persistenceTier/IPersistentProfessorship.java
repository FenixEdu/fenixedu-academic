/*
 * Created on 26/Mar/2003
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

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
    public List readByTeacherAndTypeOfDegree(ITeacher teacher, DegreeType degreeType)
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
    public List readByExecutionDegree(IExecutionDegree executionDegree) throws ExcepcaoPersistencia;

    /**
     * @param executionDegree
     * @param basic
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByExecutionDegreeAndBasic(IExecutionDegree executionDegree, Boolean basic)
            throws ExcepcaoPersistencia;

    /**
     * @param executionsDegrees
     * @return
     */
    public List readByExecutionDegrees(List executionDegrees) throws ExcepcaoPersistencia;
    
    /**
     * @author João e Rita
     * @param executionDegree
     * @param executionPeriod
     * @return List
     * @throws ExcepcaoPersistencia
     */
    public List readByExecutionDegreeAndExecutionPeriod(IExecutionDegree executionDegree, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
    
}