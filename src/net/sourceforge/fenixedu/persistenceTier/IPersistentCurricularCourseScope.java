package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentCurricularCourseScope extends IPersistentObject {

    /*public CurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
            Integer curricularCourseId, Integer curricularSemesterId, Integer branchId)
            throws ExcepcaoPersistencia;*/

    /*public CurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
            Integer curricularCourseId, Integer curricularSemesterId, Integer branchId,
            Calendar endDate) throws ExcepcaoPersistencia;*/

    /*public List readActiveCurricularCourseScopesByCurricularCourse(Integer curricularCourseId)
            throws ExcepcaoPersistencia;*/

    public List readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
            Integer curricularCourseId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia;

    public List readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(
			Integer degreeCurricularPlanId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia;
	
    public List readCurricularCourseScopesByCurricularCourseInExecutionYear(
            Integer curricularCourseId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia;
	
    public List readActiveCurricularCourseScopesByDegreeCurricularPlanAndCurricularYear(
            Integer degreeCurricularPlanId, Integer curricularYear, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia;

    /**
     * @param degreeCurricularPlanId
     * @return
     */
    /*public List readActiveCurricularCourseScopesByDegreeCurricularPlanId(Integer degreeCurricularPlanId)
            throws ExcepcaoPersistencia;*/

}