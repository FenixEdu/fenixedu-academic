package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class AnnulAccountingTransactionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1104560585375265754L;

    private DomainReference<AccountingTransaction> transaction;

    private String reason;

    public AnnulAccountingTransactionBean(final AccountingTransaction transaction) {
	setTransaction(transaction);
    }

    public AccountingTransaction getTransaction() {
	return (this.transaction != null) ? this.transaction.getObject() : null;
    }

    public void setTransaction(AccountingTransaction transaction) {
	this.transaction = (transaction != null) ? new DomainReference<AccountingTransaction>(transaction) : null;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public DateTime getWhenRegistered() {
	return getTransaction().getTransactionDetail().getWhenRegistered();
    }

    public DateTime getWhenProcessed() {
	return getTransaction().getTransactionDetail().getWhenProcessed();
    }

    public PaymentMode getPaymentMode() {
	return getTransaction().getTransactionDetail().getPaymentMode();
    }

    public Money getAmountWithAdjustment() {
	return getTransaction().getAmountWithAdjustment();
    }

    public Person getPerson() {
	return getTransaction().getEvent().getPerson();
    }

}
