package Dominio.degree.enrollment.rules;

import Util.PrecedenceScopeToApply;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedencesApplyToOptionEnrollmentRule extends PrecedencesEnrollmentRule implements IEnrollmentRule
{
	public PrecedencesApplyToOptionEnrollmentRule()
	{
	}

	protected PrecedenceScopeToApply getScopeToApply()
	{
		return PrecedenceScopeToApply.TO_APLLY_TO_OPTION_LIST;
	}
}