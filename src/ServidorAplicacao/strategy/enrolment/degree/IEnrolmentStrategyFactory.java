package ServidorAplicacao.strategy.enrolment.degree;

import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;

/**
 * @author dcs-rjao
 *
 * 15/Abr/2003
 */
public interface IEnrolmentStrategyFactory {
	public IEnrolmentStrategy getEnrolmentStrategyInstance(EnrolmentContext enrolmentContext);
}