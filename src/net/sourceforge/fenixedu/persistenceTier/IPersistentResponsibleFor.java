/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;

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

    public List readByExecutionDegree(IExecutionDegree executionDegree) throws ExcepcaoPersistencia;
}