package Dominio.transactions;

import java.sql.Timestamp;

import Dominio.IPersonAccount;
import Dominio.IPerson;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Util.PaymentType;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReimbursementTransaction extends Transaction implements IReimbursementTransaction {

    private Integer keyReimbursementGuideEntry;

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
        super(value, transactionDate, remarks, paymentType, transactionType, wasInternalBalance,
                responsiblePerson, personAccount);
        this.reimbursementGuideEntry = reimbursementGuideEntry;
    }

    /**
     * @return Returns the keyReimbursementGuideEntry.
     */
    public Integer getKeyReimbursementGuideEntry() {
        return keyReimbursementGuideEntry;
    }

    /**
     * @param keyReimbursementGuideEntry
     *            The keyReimbursementGuideEntry to set.
     */
    public void setKeyReimbursementGuideEntry(Integer keyReimbursementGuideEntry) {
        this.keyReimbursementGuideEntry = keyReimbursementGuideEntry;
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