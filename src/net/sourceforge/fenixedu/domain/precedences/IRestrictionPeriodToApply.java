package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestrictionPeriodToApply extends IRestriction {
    public PeriodToApplyRestriction getPeriodToApplyRestriction();

    public void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction);
}