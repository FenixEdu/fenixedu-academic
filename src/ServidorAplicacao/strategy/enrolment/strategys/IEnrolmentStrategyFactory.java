package ServidorAplicacao.strategy.enrolment.strategys;

import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos in Jan 16, 2004
 */

public interface IEnrolmentStrategyFactory
{
	public IEnrolmentStrategy getEnrolmentStrategyInstance(IStudentCurricularPlan studentCurricularPlan);
}