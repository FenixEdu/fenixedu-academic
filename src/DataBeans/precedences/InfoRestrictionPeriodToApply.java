package DataBeans.precedences;

import Dominio.precedences.IRestrictionPeriodToApply;
import Util.PeriodToApplyRestriction;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionPeriodToApply extends InfoRestriction {

    protected PeriodToApplyRestriction periodToApplyRestriction;

    public InfoRestrictionPeriodToApply() {
    }

    public PeriodToApplyRestriction getPeriodToApplyRestriction() {
        return periodToApplyRestriction;
    }

    public void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction) {
        this.periodToApplyRestriction = periodToApplyRestriction;
    }

    public void copyFromDomain(IRestrictionPeriodToApply restriction) {
        super.copyFromDomain(restriction);
        this.setPeriodToApplyRestriction(restriction.getPeriodToApplyRestriction());
        this.setRestrictionKindResourceKey("label.manager.restrictionPeriodToApply");
    }

    public static InfoRestrictionPeriodToApply newInfoFromDomain(IRestrictionPeriodToApply restriction) {

        InfoRestrictionPeriodToApply infoRestriction = null;
        
        if (restriction != null) {
            infoRestriction = new InfoRestrictionPeriodToApply();
            infoRestriction.copyFromDomain(restriction);
        }
        
        return infoRestriction;
    }

    public String getArg() {
        return periodToApplyRestriction.toString();
    }
}