package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentEnrollment extends IPersistentObject {
    
    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
            Integer studentCurricularPlanId, Integer executionPeriodId)
            throws ExcepcaoPersistencia;
    
    public List readEnrollmentsByStudentAndCurricularCourseNameAndDegree(Integer studentId,
            String curricularCourseName, Integer degreeId) throws ExcepcaoPersistencia;
    
    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(Integer studentCurricularPlanID,
            EnrollmentState enrollmentState) throws ExcepcaoPersistencia;

    public IEnrolment readEnrolmentByStudentNumberAndCurricularCourse(
            Integer studentNumber, Integer curricularCourseId, String year)
            throws ExcepcaoPersistencia;
    
    public List readByCurricularCourseAndYear(Integer curricularCourseId, String year)
            throws ExcepcaoPersistencia;
    
    public List readByCurricularCourseAndExecutionPeriod(Integer curricularCourseId,
            Integer executionPeriodId) throws ExcepcaoPersistencia;

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
            Integer studentCurricularPlanId, Integer curricularCourseId,
            Integer executionPeriodId) throws ExcepcaoPersistencia;

    public IEnrolment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
            Integer studentCurricularPlanId, Integer curricularCourseId,
            Integer executionPeriodId) throws ExcepcaoPersistencia;

    public List readByStudentCurricularPlanAndCurricularCourse(
            Integer studentCurricularPlanId, Integer curricularCourseId)
            throws ExcepcaoPersistencia;

    public int countEnrolmentsByCurricularCourseAndExecutionPeriod(Integer curricularCourseID,
            Integer executionPeriodID) throws ExcepcaoPersistencia;

    public int countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(Integer studentId)
            throws ExcepcaoPersistencia;
}