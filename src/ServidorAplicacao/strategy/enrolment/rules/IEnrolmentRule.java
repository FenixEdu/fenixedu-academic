package ServidorAplicacao.strategy.enrolment.rules;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public interface IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext);
}