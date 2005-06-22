package net.sourceforge.fenixedu.persistenceTier;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourseScope;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentCurricularCourseScope extends IPersistentObject {

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
            Integer curricularCourseId, Integer curricularSemesterId, Integer branchId)
            throws ExcepcaoPersistencia;

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
            Integer curricularCourseId, Integer curricularSemesterId, Integer branchId,
            Calendar endDate) throws ExcepcaoPersistencia;

    public List readActiveCurricularCourseScopesByCurricularCourse(Integer curricularCourseId)
            throws ExcepcaoPersistencia;

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
    public List readActiveCurricularCourseScopesByDegreeCurricularPlanId(Integer degreeCurricularPlanId)
            throws ExcepcaoPersistencia;

}