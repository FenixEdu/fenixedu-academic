package Dominio.transactions;

import java.sql.Timestamp;

import Dominio.IGratuitySituation;
import Dominio.IGuideEntry;
import Dominio.IPersonAccount;
import Dominio.IPessoa;
import Util.PaymentType;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class GratuityTransaction extends PaymentTransaction implements IGratuityTransaction {

    private Integer keyGratuitySituation;

    private IGratuitySituation gratuitySituation;

    public GratuityTransaction() {
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
    public GratuityTransaction(Double value, Timestamp transactionDate, String remarks,
            PaymentType paymentType, TransactionType transactionType, Boolean wasInternalBalance,
            IPessoa responsiblePerson, IPersonAccount personAccount, IGuideEntry guideEntry,
            IGratuitySituation gratuitySituation) {
        super(value, transactionDate, remarks, paymentType, transactionType, wasInternalBalance,
                responsiblePerson, personAccount, guideEntry);
        this.gratuitySituation = gratuitySituation;
    }

    /**
     * @return Returns the gratuitySituation.
     */
    public IGratuitySituation getGratuitySituation() {
        return gratuitySituation;
    }

    /**
     * @param gratuitySituation
     *            The gratuitySituation to set.
     */
    public void setGratuitySituation(IGratuitySituation gratuitySituation) {
        this.gratuitySituation = gratuitySituation;
    }

    /**
     * @return Returns the keyGratuitySituation.
     */
    public Integer getKeyGratuitySituation() {
        return keyGratuitySituation;
    }

    /**
     * @param keyGratuitySituation
     *            The keyGratuitySituation to set.
     */
    public void setKeyGratuitySituation(Integer keyGratuitySituation) {
        this.keyGratuitySituation = keyGratuitySituation;
    }

}