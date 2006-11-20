package net.sourceforge.fenixedu.domain.accounting.accountingTransactions.detail;

import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SibsTransactionDetail extends SibsTransactionDetail_Base {

    protected SibsTransactionDetail() {
	super();
    }

    public SibsTransactionDetail(DateTime whenRegistered, String sibsTransactionId, String sibsCode) {
	this();
	init(whenRegistered, sibsTransactionId, sibsCode);
    }

    protected void init(DateTime whenRegistered, String sibsTransactionId, String sibsCode) {

	super.init(whenRegistered, PaymentMode.ATM);

	checkParameters(sibsTransactionId, sibsCode);

	super.setSibsTransactionId(sibsTransactionId);
	super.setSibsCode(sibsCode);
    }

    private void checkParameters(String sibsTransactionId, String sibsCode) {
	if (sibsTransactionId == null) {
	    throw new DomainException(
		    "error.accounting.accountingTransactions.detail.SibsTransactionDetail.sibsTransactionId.cannot.be.null");
	}

	if (sibsCode == null) {
	    throw new DomainException(
		    "error.accounting.accountingTransactions.detail.SibsTransactionDetail.sibsCode.cannot.be.null");
	}

    }

    @Override
    public void setSibsTransactionId(String sibsTransactionId) {
	throw new DomainException(
		"error.accounting.accountingTransactions.detail.SibsTransactionDetail.cannot.modify.sibsTransactionId");

    }

    @Override
    public void setSibsCode(String sibsCode) {
	throw new DomainException(
		"error.accounting.accountingTransactions.detail.SibsTransactionDetail.cannot.modify.sibsCode");
    }

}
