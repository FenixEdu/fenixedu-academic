package Dominio;

import Util.PeriodToApplyRestriction;

/**
 * @author David Santos in Jan 27, 2004
 */

public interface IRestrictionPeriodToApply extends IRestriction
{
	public abstract PeriodToApplyRestriction getPeriodToApplyRestriction();
	public abstract void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction);
}