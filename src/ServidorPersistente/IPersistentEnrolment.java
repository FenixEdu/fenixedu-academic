package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IPersistentEnrolment extends IPersistentObject {

	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(IEnrolment enrolmentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia;
//	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
//	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
	public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(IStudentCurricularPlan studentCurricularPlan, EnrolmentState enrolmentState) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
	public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
	

	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope) throws ExcepcaoPersistencia;
	public List readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeList(IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope) throws ExcepcaoPersistencia;

	/**
	 * @deprecated
	 */
	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse , String year) throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param curricularCourseScope
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByCurricularCourseScope(ICurricularCourseScope curricularCourseScope) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param curricularCourse
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, String year) throws ExcepcaoPersistencia ;
	public List readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(StudentCurricularPlanState state, EnrolmentState state2, IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;  


	public IEnrolment readByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

	public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

	public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
}