package DataBeans.transactions;

import java.sql.Timestamp;

import DataBeans.InfoObject;
import DataBeans.InfoPerson;
import DataBeans.InfoPersonAccount;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.IReimbursementTransaction;
import Dominio.transactions.ITransaction;
import Util.PaymentType;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public abstract class InfoTransaction extends InfoObject {

    private Double value;

    private Timestamp transactionDate;

    private String remarks;

    private PaymentType paymentType;

    private TransactionType transactionType;

    private Boolean wasInternalBalance;

    private InfoPerson infoResponsiblePerson;

    private InfoPersonAccount infoPersonAccount;

    public InfoTransaction() {
    }

    public static InfoTransaction newInfoFromDomain(ITransaction transaction) {

        InfoTransaction infoTransaction = null;

        if (transaction instanceof IPaymentTransaction) {
            infoTransaction = InfoPaymentTransaction
                    .newInfoFromDomain((IPaymentTransaction) transaction);
        } else if (transaction instanceof IReimbursementTransaction) {
            infoTransaction = InfoReimbursementTransaction
                    .newInfoFromDomain((IReimbursementTransaction) transaction);
        }

        return infoTransaction;

    }

    /**
     * @param infoTransaction
     * @param transaction
     */
    protected void copyFromDomain(ITransaction transaction) {
        super.copyFromDomain(transaction);
        this.paymentType = transaction.getPaymentType();
        this.remarks = transaction.getRemarks();
        this.transactionDate = transaction.getTransactionDate();
        this.transactionType = transaction.getTransactionType();
        this.value = transaction.getValue();
        this.wasInternalBalance = transaction.getWasInternalBalance();

    }

    /**
     * @return Returns the infoPersonAccount.
     */
    public InfoPersonAccount getInfoPersonAccount() {
        return infoPersonAccount;
    }

    /**
     * @param infoPersonAccount
     *            The infoPersonAccount to set.
     */
    public void setInfoPersonAccount(InfoPersonAccount infoPersonAccount) {
        this.infoPersonAccount = infoPersonAccount;
    }

    /**
     * @return Returns the infoResponsiblePerson.
     */
    public InfoPerson getInfoResponsiblePerson() {
        return infoResponsiblePerson;
    }

    /**
     * @param infoResponsiblePerson
     *            The infoResponsiblePerson to set.
     */
    public void setInfoResponsiblePerson(InfoPerson infoResponsiblePerson) {
        this.infoResponsiblePerson = infoResponsiblePerson;
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
}