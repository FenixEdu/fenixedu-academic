package Dominio.degree.enrollment.rules;

import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Dominio.precedences.PrecedenceContext;
import Util.PrecedenceScopeToApply;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedencesApplyToSpanEnrollmentRule extends PrecedencesEnrollmentRule implements IEnrollmentRule
{
	public PrecedencesApplyToSpanEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
												IExecutionPeriod executionPeriod)
	{
		super.precedenceContext = new PrecedenceContext();
		super.precedenceContext.setStudentCurricularPlan(studentCurricularPlan);
		super.precedenceContext.setExecutionPeriod(executionPeriod);
	}

	protected PrecedenceScopeToApply getScopeToApply()
	{
		return PrecedenceScopeToApply.TO_APPLY_TO_SPAN;
	}
}