package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceTransaction extends InsuranceTransaction_Base {

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
			Person responsiblePerson, PersonAccount personAccount,
			GuideEntry guideEntry, ExecutionYear executionYear,
			Registration student) {
        setOjbConcreteClass(getClass().getName());
		setValue(value);
		setTransactionDate(transactionDate);
		setRemarks(remarks);
		setPaymentType(paymentType);
		setTransactionType(transactionType);
		setWasInternalBalance(wasInternalBalance);
		setResponsiblePerson(responsiblePerson);
		setPersonAccount(personAccount);
		setExecutionYear(executionYear);
        setGuideEntry(guideEntry);
		setStudent(student);
	}
}