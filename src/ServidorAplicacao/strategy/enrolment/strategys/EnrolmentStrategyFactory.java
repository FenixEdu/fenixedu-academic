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

		//		if (enrolmentContext.getStudent() == null)
		//		{
		//			throw new IllegalArgumentException("Must initialize student in context!");
		//		}
		//
		//		if (enrolmentContext.getSemester() == null)
		//		{
		//			throw new IllegalArgumentException("Must initialize semester in context!");
		//		}
		//
		//		if (enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled() == null)
		//		{
		//			throw new IllegalArgumentException("Must initialize
		// FinalCurricularCoursesScopesSpanToBeEnrolled in context!");
		//		}

		if (studentCurricularPlan == null)
		{
			throw new IllegalArgumentException("Must initialize StudentCurricularPlan!");
		}

		String enrollmentStrategyClassName =
			studentCurricularPlan.getDegreeCurricularPlan().getEnrollmentStrategyClassName();

		if (enrollmentStrategyClassName != null
			&& enrollmentStrategyClassName.equals(EnrolmentStrategyLEEC.class.toString()))
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