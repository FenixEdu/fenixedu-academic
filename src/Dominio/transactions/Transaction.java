package Dominio.transactions;

import java.sql.Timestamp;

import Util.PaymentType;
import Dominio.DomainObject;
import Dominio.IPessoa;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public abstract class Transaction extends DomainObject {
    private IPessoa responsible; /* can be a student,employee or teacher */

    private Double value;

    private Timestamp transactionDate;

    private String remarks;

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

    private PaymentType paymentType;

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
    public IPessoa getResponsible() {
        return responsible;
    }
    public void setResponsible(IPessoa responsible) {
        this.responsible = responsible;
    }
}