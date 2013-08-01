package net.sourceforge.fenixedu.domain.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.DateTime;

public class AccountingTransactionDetail extends AccountingTransactionDetail_Base {

    protected AccountingTransactionDetail() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenProcessed(new DateTime());
    }

    public AccountingTransactionDetail(final DateTime whenRegistered, final PaymentMode paymentMode) {
        this(whenRegistered, paymentMode, null);
    }

    public AccountingTransactionDetail(final DateTime whenRegistered, final PaymentMode paymentMode, final String comments) {
        this();
        init(whenRegistered, paymentMode, comments);
    }

    private void checkParameters(DateTime whenRegistered, PaymentMode paymentMode) {

        if (whenRegistered == null) {
            throw new DomainException("error.accounting.AccountingTransactionDetail.whenRegistered.cannot.be.null");
        }

        if (paymentMode == null) {
            throw new DomainException("error.accounting.AccountingTransactionDetail.paymentMode.cannot.be.null");
        }
    }

    protected void init(DateTime whenRegistered, PaymentMode paymentMode, final String comments) {

        checkParameters(whenRegistered, paymentMode);

        super.setWhenRegistered(whenRegistered);
        super.setPaymentMode(paymentMode);
        super.setComments(comments);
    }

    @Override
    public void setWhenRegistered(DateTime whenRegistered) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.whenRegistered");
    }

    @Override
    public void setPaymentMode(PaymentMode paymentMode) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.paymentMode");
    }

    @Override
    public void setTransaction(AccountingTransaction transaction) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.transaction");
    }

    void delete() {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setTransaction(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Event getEvent() {
        return getTransaction().getEvent();
    }

    @Deprecated
    public boolean hasPaymentMode() {
        return getPaymentMode() != null;
    }

    @Deprecated
    public boolean hasWhenProcessed() {
        return getWhenProcessed() != null;
    }

    @Deprecated
    public boolean hasComments() {
        return getComments() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTransaction() {
        return getTransaction() != null;
    }

    @Deprecated
    public boolean hasWhenRegistered() {
        return getWhenRegistered() != null;
    }

}
