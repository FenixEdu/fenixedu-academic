package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public abstract class Transaction extends Transaction_Base {

	private Timestamp transactionDate;

	private PaymentType paymentType;

	private TransactionType transactionType;

	private IPerson responsiblePerson;

	private IPersonAccount personAccount;

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
		setValue(value);
		this.transactionDate = transactionDate;
		setRemarks(remarks);
		this.paymentType = paymentType;
		this.transactionType = transactionType;
		setWasInternalBalance(wasInternalBalance);
		this.responsiblePerson = responsiblePerson;
		this.personAccount = personAccount;
	}

	/**
	 * @return Returns the personAccount.
	 */
	public IPersonAccount getPersonAccount() {
		return personAccount;
	}

	/**
	 * @param personAccount
	 *            The personAccount to set.
	 */
	public void setPersonAccount(IPersonAccount personAccount) {
		this.personAccount = personAccount;
	}

	/**
	 * @return Returns the responsiblePerson.
	 */
	public IPerson getResponsiblePerson() {
		return responsiblePerson;
	}

	/**
	 * @param responsiblePerson
	 *            The responsiblePerson to set.
	 */
	public void setResponsiblePerson(IPerson responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
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

	/**
	 * @return Returns the paymentType.
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType
	 *            The paymentType to set.
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return Returns the transactionType.
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            The transactionType to set.
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
}