package Dominio.transactions;

import java.sql.Timestamp;

import Dominio.IGuideEntry;
import Dominio.IPersonAccount;
import Dominio.IPerson;
import Util.PaymentType;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SmsTransaction extends PaymentTransaction implements ISmsTransaction {

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
    public SmsTransaction(Double value, Timestamp transactionDate, String remarks,
            PaymentType paymentType, TransactionType transactionType, Boolean wasInternalBalance,
            IPerson responsiblePerson, IPersonAccount personAccount, IGuideEntry guideEntry) {
        super(value, transactionDate, remarks, paymentType, transactionType, wasInternalBalance,
                responsiblePerson, personAccount, guideEntry);
    }
}