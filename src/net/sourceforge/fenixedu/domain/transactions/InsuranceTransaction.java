package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.util.PaymentType;
import net.sourceforge.fenixedu.util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceTransaction extends InsuranceTransaction_Base {

	private IStudent student;

	public InsuranceTransaction() {

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
	 * @param guideEntry
	 * @param executionYear
	 * @param student
	 */
	public InsuranceTransaction(Double value, Timestamp transactionDate,
			String remarks, PaymentType paymentType,
			TransactionType transactionType, Boolean wasInternalBalance,
			IPerson responsiblePerson, IPersonAccount personAccount,
			IGuideEntry guideEntry, IExecutionYear executionYear,
			IStudent student) {
		setValue(value);
		setTransactionDate(transactionDate);
		setRemarks(remarks);
		setPaymentType(paymentType);
		setTransactionType(transactionType);
		setWasInternalBalance(wasInternalBalance);
		setResponsiblePerson(responsiblePerson);
		setPersonAccount(personAccount);
		setExecutionYear(executionYear);
		this.student = student;
	}

	/**
	 * @return Returns the student.
	 */
	public IStudent getStudent() {
		return student;
	}

	/**
	 * @param student
	 *            The student to set.
	 */
	public void setStudent(IStudent student) {
		this.student = student;
	}
}