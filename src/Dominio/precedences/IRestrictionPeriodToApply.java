package Dominio.precedences;

import Util.PeriodToApplyRestriction;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestrictionPeriodToApply extends IRestriction
{
	public abstract PeriodToApplyRestriction getPeriodToApplyRestriction();
	public abstract void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction);
}