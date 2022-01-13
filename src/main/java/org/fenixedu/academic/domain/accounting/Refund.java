package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

public class Refund extends Refund_Base {

    public Refund(Event event, Money amount, User creator, boolean excessOnly, final String bankAccountNumber) {
        this(event, amount, creator, excessOnly, new DateTime(), bankAccountNumber);
    }
    
    public Refund(Event event, Money amount, User creator, boolean excessOnly, DateTime when, final String bankAccountNumber) {
        setEvent(event);
        setWhenOccured(when);
        setAmount(amount);
        setCreator(creator);
        setExcessOnly(excessOnly);
        setState(RefundState.PENDING);
        setStateDate(when.toLocalDate());
        setBankAccountNumber(bankAccountNumber);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    protected void delete(boolean annulAccountingTransaction) {
        DomainException.throwWhenDeleteBlocked(this.getDeletionBlockers());
        final AccountingTransaction accountingTransaction = getAccountingTransaction();

        if (accountingTransaction != null && annulAccountingTransaction) {
            setAccountingTransaction(null);
            accountingTransaction.annul(Authenticate.getUser(), String.format("Refund %s was deleted", getExternalId()));
        }

        setEvent(null);
        setCreator(null);
        super.deleteDomainObject();
    }

    public void delete() {
        delete(true);
    }

    public LocalizedString getDescription() {
        return BundleUtil.getLocalizedString(Bundle.ACCOUNTING, getExcessOnly() ? "label.refund.excess.only" : "label.refund");
    }
}
