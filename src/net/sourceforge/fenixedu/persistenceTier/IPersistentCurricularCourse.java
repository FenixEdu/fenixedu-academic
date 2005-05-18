package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;

/**
 * @author dcs-rjao 25/Mar/2003
 */

public interface IPersistentCurricularCourse extends IPersistentObject {

    public List readCurricularCoursesByDegreeCurricularPlan(String name, String degreeName, String degreeSigla)
            throws ExcepcaoPersistencia;
	
    public ICurricularCourse readCurricularCourseByDegreeCurricularPlanAndNameAndCode(
            Integer degreeCurricularPlanId, String name, String code) throws ExcepcaoPersistencia;

    public List readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(
			Integer degreeCurricularPlanKey, Boolean basic) throws ExcepcaoPersistencia;

    public List readbyCourseCodeAndDegreeCurricularPlan(String curricularCourseCode,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	
    public List readbyCourseNameAndDegreeCurricularPlan(String curricularCourseName,
			Integer degreeCurricularPlanKey) throws ExcepcaoPersistencia;

    public List readExecutedCurricularCoursesByDegreeAndExecutionYear(Integer degreeKey, 
			Integer executionYearKey) throws ExcepcaoPersistencia;

    public List readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(Integer degreeKey, 
			Integer year, Integer executionYearKey) throws ExcepcaoPersistencia;
}