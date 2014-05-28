/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonAccount;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.ReimbursementTransaction;
import net.sourceforge.fenixedu.domain.transactions.Transaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;

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

    public static InfoTransaction newInfoFromDomain(Transaction transaction) {

        InfoTransaction infoTransaction = null;

        if (transaction instanceof PaymentTransaction) {
            infoTransaction = InfoPaymentTransaction.newInfoFromDomain((PaymentTransaction) transaction);
        } else if (transaction instanceof ReimbursementTransaction) {
            infoTransaction = InfoReimbursementTransaction.newInfoFromDomain((ReimbursementTransaction) transaction);
        }

        return infoTransaction;

    }

    /**
     * @param infoTransaction
     * @param transaction
     */
    protected void copyFromDomain(Transaction transaction) {
        super.copyFromDomain(transaction);
        this.paymentType = transaction.getPaymentType();
        this.remarks = transaction.getRemarks();
        if (transaction.getTransactionDate() != null) {
            this.setTransactionDate(new Timestamp(transaction.getTransactionDate().getTime()));
        }
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