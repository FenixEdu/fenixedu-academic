package ServidorAplicacao.strategy.enrolment.degree.strategys;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public interface IEnrolmentStrategy {

	public EnrolmentContext getAvailableCurricularCourses() throws ExcepcaoPersistencia;
	
	public EnrolmentContext validateEnrolment() throws ExcepcaoPersistencia;

	public EnrolmentContext getEnrolmentContext();
	public void setEnrolmentContext(EnrolmentContext enrolmentContext);
}