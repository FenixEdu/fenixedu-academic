package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.joda.time.DateTime;

public class AccountingTransactionDetail extends AccountingTransactionDetail_Base {

    protected AccountingTransactionDetail() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
	super.setWhenProcessed(new DateTime());
    }

    public AccountingTransactionDetail(DateTime whenRegistered, PaymentMode paymentMode) {
	this();
	init(whenRegistered, paymentMode);
    }

    private void checkParameters(DateTime whenRegistered, PaymentMode paymentMode) {

	if (whenRegistered == null) {
	    throw new DomainException(
		    "error.accounting.AccountingTransactionDetail.whenRegistered.cannot.be.null");
	}

	if (paymentMode == null) {
	    throw new DomainException(
		    "error.accounting.AccountingTransactionDetail.paymentMode.cannot.be.null");
	}
    }

    protected void init(DateTime whenRegistered, PaymentMode paymentMode) {

	checkParameters(whenRegistered, paymentMode);

	super.setWhenRegistered(whenRegistered);
	super.setPaymentMode(paymentMode);
    }

    @Override
    public void setWhenRegistered(DateTime whenRegistered) {
	throw new DomainException(
		"error.accounting.AccountingTransactionDetail.cannot.modify.whenRegistered");
    }

    @Override
    public void setPaymentMode(PaymentMode paymentMode) {
	throw new DomainException(
		"error.accounting.AccountingTransactionDetail.cannot.modify.paymentMode");
    }

    @Override
    public void setTransaction(AccountingTransaction transaction) {
	throw new DomainException(
		"error.accounting.AccountingTransactionDetail.cannot.modify.transaction");
    }
    
    @Checked("RolePredicates.MANAGER_PREDICATE")
    void delete() {
	super.setTransaction(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
