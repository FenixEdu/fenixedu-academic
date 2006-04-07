package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class GratuityTransaction extends GratuityTransaction_Base {

	public GratuityTransaction() {
        super();
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
	 * @param gratuitySituation
	 */
	public GratuityTransaction(Double value, Timestamp transactionDate,
			String remarks, PaymentType paymentType,
			TransactionType transactionType, Boolean wasInternalBalance,
			Person responsiblePerson, PersonAccount personAccount,
			GuideEntry guideEntry, GratuitySituation gratuitySituation) {
        this();
        setOjbConcreteClass(getClass().getName());
		setValue(value);
		setTransactionDate(transactionDate);
		setRemarks(remarks);
		setPaymentType(paymentType);
		setTransactionType(transactionType);
		setWasInternalBalance(wasInternalBalance);
		setResponsiblePerson(responsiblePerson);
		setPersonAccount(personAccount);
        setGuideEntry(guideEntry);
		setGratuitySituation(gratuitySituation);
	}

}
