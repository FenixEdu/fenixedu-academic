package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReimbursementTransaction extends ReimbursementTransaction_Base {


    public ReimbursementTransaction() {

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
     * @param reimbursementGuideEntry
     */
    public ReimbursementTransaction(Double value, Timestamp transactionDate, String remarks,
            PaymentType paymentType, TransactionType transactionType, Boolean wasInternalBalance,
            Person responsiblePerson, PersonAccount personAccount,
            ReimbursementGuideEntry reimbursementGuideEntry) {
		setValue(value);
		setTransactionDate(transactionDate);
		setRemarks(remarks);
		setPaymentType(paymentType);
		setTransactionType(transactionType);
		setWasInternalBalance(wasInternalBalance);
		setResponsiblePerson(responsiblePerson);
		setPersonAccount(personAccount);
        setReimbursementGuideEntry(reimbursementGuideEntry);
    }
}