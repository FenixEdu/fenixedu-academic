package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Util.CurricularCourseType;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentEnrolment extends IPersistentObject
{
    public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia;

    public List readEnrollmentsByStudentAndCurricularCourseNameAndCurricularCourseDegree(
        IStudent student,
        ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia;

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
        IStudentCurricularPlan studentCurricularPlan,
        EnrolmentState enrolmentState)
        throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
        throws ExcepcaoPersistencia;

    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia;

    public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse,
        String year)
        throws ExcepcaoPersistencia;

    public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, String year)
        throws ExcepcaoPersistencia;

    public List readByCurricularCourseAndExecutionPeriod(
        ICurricularCourse curricularCourse,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia;

    public List readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(
        StudentCurricularPlanState state,
        EnrolmentState state2,
        IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia;

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia;

    public IEnrolment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia;

    public List readByStudentCurricularPlanAndCurricularCourse(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia;

    public List readAllByStudentCurricularPlanAndCurricularCourse(
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia;

    public List readAllByStudentCurricularPlanAndEnrolmentStateAndExecutionPeriod(
        IStudentCurricularPlan studentCurricularPlan,
        EnrolmentState enrolmentState,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @param executionPeriod
     * @param state
     * @return
     */
    public List readByCurricularCourseAndExecutionPeriodAndEnrolmentState(
        ICurricularCourse curricularCourse,
        IExecutionPeriod executionPeriod,
        EnrolmentState state)
        throws ExcepcaoPersistencia;

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentStateAndCurricularCourseType(
        IStudentCurricularPlan studentCurricularPlan,
        EnrolmentState enrolmentState,
        CurricularCourseType curricularCourseType)
        throws ExcepcaoPersistencia;

    public int countEnrolmentsByCurricularCourseAndExecutionPeriod(
        Integer curricularCourseID,
        Integer executionPeriodID)
        throws ExcepcaoPersistencia;

    public int countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(
            IStudent student) throws ExcepcaoPersistencia;
}