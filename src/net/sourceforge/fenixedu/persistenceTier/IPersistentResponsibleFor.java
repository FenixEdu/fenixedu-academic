/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IResponsibleFor;

/**
 * @author João Mota
 *  
 */
public interface IPersistentResponsibleFor extends IPersistentObject {

    public List readByTeacher(Integer teacherNumber) throws ExcepcaoPersistencia;

    public IResponsibleFor readByTeacherAndExecutionCourse(Integer teacherID, Integer executionCourseID)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia;

    public List readByTeacherAndExecutionPeriod(Integer teacherID, Integer executionPeriodID)
            throws ExcepcaoPersistencia;

    public List readByTeacherAndExecutionYear(Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia;

    public List readByTeacherAndExecutionCourseIds(Integer teacherID, List executionCourseIds)
            throws ExcepcaoPersistencia;

    public List readByExecutionDegree(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia;
}