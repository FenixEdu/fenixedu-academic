package Dominio.degree.enrollment.rules;

import Util.PrecedenceScopeToApply;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedencesApplyDuringEnrollmentRule extends PrecedencesEnrollmentRule implements IEnrollmentRule
{
	public PrecedencesApplyDuringEnrollmentRule()
	{
	}

	protected PrecedenceScopeToApply getScopeToApply()
	{
		return PrecedenceScopeToApply.TO_APPLY_DURING_ENROLMENT;
	}
}