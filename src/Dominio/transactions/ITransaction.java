package Dominio.transactions;

import java.sql.Timestamp;

import Dominio.IDomainObject;
import Dominio.IPersonAccount;
import Dominio.IPerson;
import Util.PaymentType;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface ITransaction extends IDomainObject {

    /**
     * @return Returns the responsiblePerson.
     */
    public abstract IPerson getResponsiblePerson();

    /**
     * @param responsiblePerson
     *            The responsiblePerson to set.
     */
    public abstract void setResponsiblePerson(IPerson responsiblePerson);

    /**
     * @return Returns the wasInternalBalance.
     */
    public abstract Boolean getWasInternalBalance();

    /**
     * @param wasInternalBalance
     *            The wasInternalBalance to set.
     */
    public abstract void setWasInternalBalance(Boolean wasInternalBalance);

    /**
     * @return Returns the remarks.
     */
    public abstract String getRemarks();

    /**
     * @param remarks
     *            The remarks to set.
     */
    public abstract void setRemarks(String remarks);

    /**
     * @return Returns the transactionDate.
     */
    public abstract Timestamp getTransactionDate();

    /**
     * @param transactionDate
     *            The transactionDate to set.
     */
    public abstract void setTransactionDate(Timestamp transactionDate);

    /**
     * @return Returns the value.
     */
    public abstract Double getValue();

    /**
     * @param value
     *            The value to set.
     */
    public abstract void setValue(Double value);

    /**
     * @return Returns the paymentType.
     */
    public abstract PaymentType getPaymentType();

    /**
     * @param paymentType
     *            The paymentType to set.
     */
    public abstract void setPaymentType(PaymentType paymentType);

    /**
     * @return Returns the transactionType.
     */
    public abstract TransactionType getTransactionType();

    /**
     * @param transactionType
     *            The transactionType to set.
     */
    public abstract void setTransactionType(TransactionType transactionType);

    /**
     * @return Returns the personAccount.
     */
    public IPersonAccount getPersonAccount();

    /**
     * @param personAccount
     *            The personAccount to set.
     */
    public void setPersonAccount(IPersonAccount personAccount);
}