package ServidorAplicacao.strategy.enrolment.strategys;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos in Jan 16, 2004
 */

public interface IEnrolmentStrategy
{
	public StudentEnrolmentContext getStudentEnrolmentContext();
	public void setStudentEnrolmentContext(StudentEnrolmentContext studentEnrolmentContext);

	public StudentEnrolmentContext getAvailableCurricularCourses() throws ExcepcaoPersistencia;
//	public StudentEnrolmentContext validateEnrolment();
//	public StudentEnrolmentContext getOptionalCurricularCourses();
//	public StudentEnrolmentContext getDegreesForOptionalCurricularCourses();
}