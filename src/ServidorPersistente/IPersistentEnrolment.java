package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public interface IPersistentEnrolment extends IPersistentObject
{

	
	  public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia;
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
    public IEnrolment readByStudentCurricularPlanAndCurricularCourse(
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
}