package net.sourceforge.fenixedu.domain.accounting.accountingTransactions;

import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class InstallmentAccountingTransaction extends InstallmentAccountingTransaction_Base {

    static {
	AccountingTransactionEvent.addListener(new RelationAdapter<AccountingTransaction, Event>() {

	    @Override
	    public void beforeAdd(AccountingTransaction accountingTransaction, Event event) {
		if (accountingTransaction instanceof InstallmentAccountingTransaction) {
		    InstallmentAccountingTransaction installmentAccountingTransaction = (InstallmentAccountingTransaction) accountingTransaction;
		    if (event.hasAccountingTransactionFor(installmentAccountingTransaction
			    .getInstallment())) {
			throw new DomainException(
				"error.accounting.accountingTransactions.InstallmentAccountingTransaction.event.already.has.accounting.transaction.for.same.installment");
		    }
		}
	    }

	});
    }

    protected InstallmentAccountingTransaction() {
	super();
    }

    public InstallmentAccountingTransaction(User responsibleUser, Event event, Entry debit,
	    Entry credit, PaymentMode paymentMode, DateTime whenRegistered, Installment installment) {
	this();
	init(responsibleUser, event, debit, credit, paymentMode, whenRegistered, installment);
    }

    private void init(User responsibleUser, Event event, Entry debit, Entry credit,
	    PaymentMode paymentMode, DateTime whenRegistered, Installment installment) {
	super.init(responsibleUser, event, debit, credit, paymentMode, whenRegistered);
	checkParameters(installment);
	super.setInstallment(installment);
    }

    private void checkParameters(Installment installment) {
	if (installment == null) {
	    throw new DomainException(
		    "error.accounting.accountingTransactions.InstallmentAccountingTransaction.installment.cannot.be.null");
	}
    }

    @Override
    public void setInstallment(Installment installment) {
	throw new DomainException(
		"error.accounting.accountingTransactions.InstallmentAccountingTransaction.cannot.modify.installment");
    }
}
