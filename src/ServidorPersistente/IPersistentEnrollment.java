package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDegree;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Util.CurricularCourseType;
import Util.EnrollmentState;
import Util.StudentCurricularPlanState;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentEnrollment extends IPersistentObject {
    public void delete(IEnrollment enrolment) throws ExcepcaoPersistencia;

    public List readEnrollmentsByStudentAndCurricularCourseNameAndDegree(IStudent student,
            ICurricularCourse curricularCourse, IDegree degree) throws ExcepcaoPersistencia;

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(Integer studentCurricularPlanID,
            EnrollmentState enrollmentState) throws ExcepcaoPersistencia;

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
            IStudentCurricularPlan studentCurricularPlan, EnrollmentState enrollmentState)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllByStudentCurricularPlan(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia;

    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public IEnrollment readEnrolmentByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, String year)
            throws ExcepcaoPersistencia;

    public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, String year)
            throws ExcepcaoPersistencia;

    public List readByCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(
            StudentCurricularPlanState state, EnrollmentState state2,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public IEnrollment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    public List readAllByStudentCurricularPlanAndCurricularCourse(
            IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    public List readAllByStudentCurricularPlanAndEnrolmentStateAndExecutionPeriod(
            IStudentCurricularPlan studentCurricularPlan, EnrollmentState enrollmentState,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @param executionPeriod
     * @param state
     * @return
     */
    public List readByCurricularCourseAndExecutionPeriodAndEnrolmentState(
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod, EnrollmentState state)
            throws ExcepcaoPersistencia;

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentStateAndCurricularCourseType(
            IStudentCurricularPlan studentCurricularPlan, EnrollmentState enrollmentState,
            CurricularCourseType curricularCourseType) throws ExcepcaoPersistencia;

    public int countEnrolmentsByCurricularCourseAndExecutionPeriod(Integer curricularCourseID,
            Integer executionPeriodID) throws ExcepcaoPersistencia;

    public int countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia;
}