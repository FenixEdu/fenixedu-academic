package Dominio.transactions;

import java.sql.Timestamp;

import Dominio.DomainObject;
import Dominio.IPersonAccount;
import Dominio.IPerson;
import Util.PaymentType;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public abstract class Transaction extends DomainObject implements ITransaction {

    private Double value;

    private Timestamp transactionDate;

    private String remarks;

    private PaymentType paymentType;

    private TransactionType transactionType;

    private Boolean wasInternalBalance;

    private Integer keyResponsiblePerson;

    private IPerson responsiblePerson;

    private Integer keyPersonAccount;

    private IPersonAccount personAccount;

    public Transaction() {
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
     */
    protected Transaction(Double value, Timestamp transactionDate, String remarks,
            PaymentType paymentType, TransactionType transactionType, Boolean wasInternalBalance,
            IPerson responsiblePerson, IPersonAccount personAccount) {
        this.value = value;
        this.transactionDate = transactionDate;
        this.remarks = remarks;
        this.paymentType = paymentType;
        this.transactionType = transactionType;
        this.wasInternalBalance = wasInternalBalance;
        this.responsiblePerson = responsiblePerson;
        this.personAccount = personAccount;
    }

    /**
     * @return Returns the keyPersonAccount.
     */
    public Integer getKeyPersonAccount() {
        return keyPersonAccount;
    }

    /**
     * @param keyPersonAccount
     *            The keyPersonAccount to set.
     */
    public void setKeyPersonAccount(Integer keyPersonAccount) {
        this.keyPersonAccount = keyPersonAccount;
    }

    /**
     * @return Returns the personAccount.
     */
    public IPersonAccount getPersonAccount() {
        return personAccount;
    }

    /**
     * @param personAccount
     *            The personAccount to set.
     */
    public void setPersonAccount(IPersonAccount personAccount) {
        this.personAccount = personAccount;
    }

    /**
     * @return Returns the keyResponsiblePerson.
     */
    public Integer getKeyResponsiblePerson() {
        return keyResponsiblePerson;
    }

    /**
     * @param keyResponsiblePerson
     *            The keyResponsiblePerson to set.
     */
    public void setKeyResponsiblePerson(Integer keyResponsiblePerson) {
        this.keyResponsiblePerson = keyResponsiblePerson;
    }

    /**
     * @return Returns the responsiblePerson.
     */
    public IPerson getResponsiblePerson() {
        return responsiblePerson;
    }

    /**
     * @param responsiblePerson
     *            The responsiblePerson to set.
     */
    public void setResponsiblePerson(IPerson responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    /**
     * @return Returns the wasInternalBalance.
     */
    public Boolean getWasInternalBalance() {
        return wasInternalBalance;
    }

    /**
     * @param wasInternalBalance
     *            The wasInternalBalance to set.
     */
    public void setWasInternalBalance(Boolean wasInternalBalance) {
        this.wasInternalBalance = wasInternalBalance;
    }

    /**
     * @return Returns the remarks.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            The remarks to set.
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return Returns the transactionDate.
     */
    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate
     *            The transactionDate to set.
     */
    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return Returns the value.
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * @return Returns the paymentType.
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType
     *            The paymentType to set.
     */
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return Returns the transactionType.
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType
     *            The transactionType to set.
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}