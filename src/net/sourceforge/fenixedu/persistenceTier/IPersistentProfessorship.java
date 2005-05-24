/*
 * Created on 26/Mar/2003
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IProfessorship;

/**
 * @author João Mota
 */
public interface IPersistentProfessorship extends IPersistentObject {

    public List readByTeacherNumber(Integer teacherNumber) throws ExcepcaoPersistencia;
    
    public List readByTeacher(Integer teacherID) throws ExcepcaoPersistencia;

    public IProfessorship readByTeacherAndExecutionCourse(Integer teacherID, Integer executionCourseID)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia;

    public List readByTeacherAndExecutionPeriod(Integer teacherID, Integer executionPeriodID)
            throws ExcepcaoPersistencia;

    public List readByTeacherAndExecutionYear(Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlansAndExecutionYearAndBasic(List degreeCurricularPlanIDs,
            Integer executionYearID, Boolean basic) throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlanAndExecutionYear(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlanAndBasic(Integer degreeCurricularPlanID,
            Integer executionYearID, Boolean basic) throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlansAndExecutionYear(List degreeCurricularPlanIDs,
            Integer executionYearID) throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlanAndExecutionPeriod(Integer degreeCurricularPlanID,
            Integer executionPeriodID) throws ExcepcaoPersistencia;

}