package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.IDegreeCurricularPlan;
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

	public List getListOfEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan, EnrollmentRuleType enrollmentRuleType)
	{
		if (enrollmentRuleType.equals(EnrollmentRuleType.PARTIAL))
		{
			return getListOfPartialEnrollmentRules(degreeCurricularPlan);
		} else if (enrollmentRuleType.equals(EnrollmentRuleType.TOTAL))
		{
			return getListOfTotalEnrollmentRules(degreeCurricularPlan);
		} else if (enrollmentRuleType.equals(EnrollmentRuleType.EMPTY))
		{
			return getListOfEmptyEnrollmentRules(degreeCurricularPlan);
		} else
		{
			return null;
		}
	}

	private List getListOfPartialEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan)
	{
		// TODO [DAVID]: Add code here.
		return null;
	}

	private List getListOfTotalEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan)
	{
		// TODO [DAVID]: Add code here.
		return null;
	}

	private List getListOfEmptyEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan)
	{
		return new ArrayList();
	}
}