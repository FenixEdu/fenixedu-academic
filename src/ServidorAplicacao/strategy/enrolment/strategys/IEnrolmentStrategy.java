package ServidorAplicacao.strategy.enrolment.strategys;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public interface IEnrolmentStrategy {

	public EnrolmentContext getEnrolmentContext();
	public void setEnrolmentContext(EnrolmentContext enrolmentContext);

	public EnrolmentContext getAvailableCurricularCourses();
	public EnrolmentContext validateEnrolment();
	public EnrolmentContext getOptionalCurricularCourses();
	public EnrolmentContext getDegreesForOptionalCurricularCourses();
}