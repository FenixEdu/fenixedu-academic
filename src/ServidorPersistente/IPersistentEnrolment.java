package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IPersistentEnrolment extends IPersistentObject {

	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(IEnrolment enrolmentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia;
	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
	public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(IStudentCurricularPlan studentCurricularPlan, EnrolmentState enrolmentState) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	/**
	 * @param studentCurricularPlan
	 * @return
	 */
	public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
}