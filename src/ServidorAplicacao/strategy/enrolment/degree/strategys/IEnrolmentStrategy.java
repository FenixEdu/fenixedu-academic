package ServidorAplicacao.strategy.enrolment.degree.strategys;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public interface IEnrolmentStrategy {

	public EnrolmentContext getAvailableCurricularCourses();
	public EnrolmentContext validateEnrolment();
	public EnrolmentContext getOptionalCurricularCourses();
	public EnrolmentContext getDegreesForOptionalCurricularCourses();
		
	public EnrolmentContext getEnrolmentContext();
	public void setEnrolmentContext(EnrolmentContext enrolmentContext);
}