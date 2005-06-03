package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public abstract class Transaction extends Transaction_Base {

	private Timestamp transactionDate;

	public Transaction() {
	}

	/**
	 * @param value
	 * @param transactionDate
	 * @param remarks
	 * @param paymentType
	 * @param transactionType
	 * @param wasInternalBalance
	 * @param responsiblePerson
	 * @param personAccount
	 */
	protected Transaction(Double value, Timestamp transactionDate,
			String remarks, PaymentType paymentType,
			TransactionType transactionType, Boolean wasInternalBalance,
			IPerson responsiblePerson, IPersonAccount personAccount) {
        this.setValue(value);
		this.setTransactionDate(transactionDate);
		this.setRemarks(remarks);
		this.setPaymentType(paymentType);
		this.setTransactionType(transactionType);
        this.setWasInternalBalance(wasInternalBalance);
        this.setResponsiblePerson(responsiblePerson);
        this.setPersonAccount(personAccount);
	}

	/**
	 * @return Returns the transactionDate.
	 */
	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            The transactionDate to set.
	 */
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

}
