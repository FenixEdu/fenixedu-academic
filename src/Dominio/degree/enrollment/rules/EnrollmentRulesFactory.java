package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Util.TipoCurso;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jun 14, 2004
 */

public class EnrollmentRulesFactory
{
	private static EnrollmentRulesFactory instance = null;

	private EnrollmentRulesFactory()
	{
	}

	public static synchronized EnrollmentRulesFactory getInstance()
	{
		if (instance == null)
		{
			instance = new EnrollmentRulesFactory();
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

	public List getListOfEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType)
	{
		if (enrollmentRuleType.equals(EnrollmentRuleType.PARTIAL))
		{
			return getListOfPartialEnrollmentRules(degreeCurricularPlan, studentCurricularPlan, executionPeriod);
		} else if (enrollmentRuleType.equals(EnrollmentRuleType.TOTAL))
		{
			return getListOfTotalEnrollmentRules(degreeCurricularPlan, studentCurricularPlan, executionPeriod);
		} else if (enrollmentRuleType.equals(EnrollmentRuleType.EMPTY))
		{
			return getListOfEmptyEnrollmentRules(degreeCurricularPlan, studentCurricularPlan, executionPeriod);
		} else
		{
			return null;
		}
	}



	
	
	
	
	
	
	private List getListOfPartialEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
	{
		List result = new ArrayList();
		
//		result.add( new MaximumCurricularCourseEnrollmentRule(studentCurricularPlan, executionPeriod));
//		result.add( new MaxOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
		
		return result;
	}

	private List getListOfTotalEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
	{
		List result = new ArrayList();
		
//		result.add( new MaximumCurricularCourseEnrollmentRule(studentCurricularPlan, executionPeriod));
//		result.add( new MaxOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
		result.add(new PrecedencesApplyToSpanEnrollmentRule(studentCurricularPlan, executionPeriod));

		if (degreeCurricularPlan.getDegree().getTipoCurso().equals(TipoCurso.LICENCIATURA_OBJ)
			&& degreeCurricularPlan.getName().equals("LEEC2003/2004"))
		{
			result.add(new SpecificLEECEnrollmentRule(studentCurricularPlan));
		}
		
		return result;
	}

	private List getListOfEmptyEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
	{
		return new ArrayList();
	}
	
}