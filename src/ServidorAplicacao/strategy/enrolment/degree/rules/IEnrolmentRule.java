package ServidorAplicacao.strategy.enrolment.degree.rules;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public interface IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext);
}