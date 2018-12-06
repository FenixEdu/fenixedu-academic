package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class Refund extends Refund_Base {

    public Refund(Event event, Money amount, User creator, boolean excessOnly) {
        this(event, amount, creator, excessOnly, new DateTime());
    }
    
    public Refund(Event event, Money amount, User creator, boolean excessOnly, DateTime when) {
        setEvent(event);
        setWhenOccured(when);
        setAmount(amount);
        setCreator(creator);
        setExcessOnly(excessOnly);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void delete() {
        setEvent(null);
        setCreator(null);
        super.deleteDomainObject();
    }

    public LocalizedString getDescription() {
        return BundleUtil.getLocalizedString(Bundle.ACCOUNTING, getExcessOnly() ? "label.refund.excess.only" : "label.refund");
    }
}
