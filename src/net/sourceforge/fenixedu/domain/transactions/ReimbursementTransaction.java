package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReimbursementTransaction extends ReimbursementTransaction_Base {

    private IReimbursementGuideEntry reimbursementGuideEntry;

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
            IPerson responsiblePerson, IPersonAccount personAccount,
            IReimbursementGuideEntry reimbursementGuideEntry) {
		setValue(value);
		setTransactionDate(transactionDate);
		setRemarks(remarks);
		setPaymentType(paymentType);
		setTransactionType(transactionType);
		setWasInternalBalance(wasInternalBalance);
		setResponsiblePerson(responsiblePerson);
		setPersonAccount(personAccount);
        this.reimbursementGuideEntry = reimbursementGuideEntry;
    }

    /**
     * @return Returns the reimbursementGuideEntry.
     */
    public IReimbursementGuideEntry getReimbursementGuideEntry() {
        return reimbursementGuideEntry;
    }

    /**
     * @param reimbursementGuideEntry
     *            The reimbursementGuideEntry to set.
     */
    public void setReimbursementGuideEntry(IReimbursementGuideEntry reimbursementGuideEntry) {
        this.reimbursementGuideEntry = reimbursementGuideEntry;
    }
}