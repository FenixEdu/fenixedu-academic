package ServidorAplicacao.strategy.enrolment.strategys;

import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.strategys.student.EnrolmentStrategyLEEC;

/**
 * @author David Santos in Jan 16, 2004
 */

public class EnrolmentStrategyFactory implements IEnrolmentStrategyFactory
{
	private static EnrolmentStrategyFactory instance = null;

	private EnrolmentStrategyFactory()
	{
	}

	public static synchronized EnrolmentStrategyFactory getInstance()
	{
		if (instance == null)
		{
			instance = new EnrolmentStrategyFactory();
		}
		return instance;
	}

	public static synchronized void resetInstance()
	{
		if (instance != null)
		{
			instance = null;
		}
	}

	public IEnrolmentStrategy getEnrolmentStrategyInstance(IStudentCurricularPlan studentCurricularPlan)
	{
		IEnrolmentStrategy strategyInstance = null;

		if (studentCurricularPlan == null)
		{
			throw new IllegalArgumentException("Must initialize StudentCurricularPlan!");
		}

		String degreeCurricularPlanName =
			studentCurricularPlan.getDegreeCurricularPlan().getName();

		if (degreeCurricularPlanName != null
			&& degreeCurricularPlanName.equals("LEEC2003/2004"))
		{
			strategyInstance = new EnrolmentStrategyLEEC(studentCurricularPlan);
		}
		else
		{
			throw new IllegalArgumentException("Degree or DegreeCurricularPlan invalid!");
		}

		return strategyInstance;
	}
}