package ServidorAplicacao.strategy.enrolment.rules;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * @author David Santos in Jan 16, 2004
 */

public interface IEnrolmentRule
{
	public StudentEnrolmentContext apply(StudentEnrolmentContext studentEnrolmentContext);
}