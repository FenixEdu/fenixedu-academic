package Dominio.precedences;

import Util.PeriodToApplyRestriction;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestrictionPeriodToApply extends IRestriction
{
	public PeriodToApplyRestriction getPeriodToApplyRestriction();
	public void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction);
}