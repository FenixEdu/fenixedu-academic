package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.util.Money;

public class PhdGratuityPriceQuirk extends PhdGratuityPriceQuirk_Base {

    public PhdGratuityPriceQuirk() {
        super();
    }

    public PhdGratuityPriceQuirk(int year, Money gratuity) {
        setYear(year);
        setGratuity(gratuity);
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPostingRule() {
        return getPostingRule() != null;
    }

    @Deprecated
    public boolean hasGratuity() {
        return getGratuity() != null;
    }

}
