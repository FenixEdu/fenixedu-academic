package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.util.PaymentType;
import net.sourceforge.fenixedu.util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public abstract class PaymentTransaction extends Transaction implements IPaymentTransaction {

    private Integer keyGuideEntry;

    private IGuideEntry guideEntry;

    public PaymentTransaction() {

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
     */
    public PaymentTransaction(Double value, Timestamp transactionDate, String remarks,
            PaymentType paymentType, TransactionType transactionType, Boolean wasInternalBalance,
            IPerson responsiblePerson, IPersonAccount personAccount, IGuideEntry guideEntry) {
        super(value, transactionDate, remarks, paymentType, transactionType, wasInternalBalance,
                responsiblePerson, personAccount);
        this.guideEntry = guideEntry;
    }

    /**
     * @return Returns the guideEntry.
     */
    public IGuideEntry getGuideEntry() {
        return guideEntry;
    }

    /**
     * @param guideEntry
     *            The guideEntry to set.
     */
    public void setGuideEntry(IGuideEntry guideEntry) {
        this.guideEntry = guideEntry;
    }

    /**
     * @return Returns the keyGuideEntry.
     */
    public Integer getKeyGuideEntry() {
        return keyGuideEntry;
    }

    /**
     * @param keyGuideEntry
     *            The keyGuideEntry to set.
     */
    public void setKeyGuideEntry(Integer keyGuideEntry) {
        this.keyGuideEntry = keyGuideEntry;
    }
}