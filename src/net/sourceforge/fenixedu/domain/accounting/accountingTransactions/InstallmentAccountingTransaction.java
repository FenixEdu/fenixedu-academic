package net.sourceforge.fenixedu.domain.accounting.accountingTransactions;

import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransactionDetail;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InstallmentAccountingTransaction extends InstallmentAccountingTransaction_Base {

    protected InstallmentAccountingTransaction() {
	super();
    }

    public InstallmentAccountingTransaction(User responsibleUser, Event event, Entry debit, Entry credit,
	    Installment installment, AccountingTransactionDetail transactionDetail) {
	this();
	init(responsibleUser, event, debit, credit, installment, transactionDetail);
    }

    private void init(User responsibleUser, Event event, Entry debit, Entry credit, Installment installment,
	    AccountingTransactionDetail transactionDetail) {
	super.init(responsibleUser, event, debit, credit, transactionDetail);
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

    @Override
    public void delete() {
	super.setInstallment(null);
	super.delete();
    }
}
