/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.accounting;

import java.io.Serializable;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class AnnulAccountingTransactionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1104560585375265754L;

    private AccountingTransaction transaction;

    private String reason;

    public AnnulAccountingTransactionBean() {
    }

    public AnnulAccountingTransactionBean(final AccountingTransaction transaction) {
        setTransaction(transaction);
    }

    public AccountingTransaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(AccountingTransaction transaction) {
        this.transaction = transaction;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DateTime getWhenRegistered() {
        return getTransaction().getTransactionDetail().getWhenRegistered();
    }

    public DateTime getWhenProcessed() {
        return getTransaction().getTransactionDetail().getWhenProcessed();
    }

    public PaymentMethod getPaymentMethod() {
        return getTransaction().getTransactionDetail().getPaymentMethod();
    }

    public String getPaymentReference() {
        return getTransaction().getTransactionDetail().getPaymentReference();
    }

    public Money getAmountWithAdjustment() {
        return getTransaction().getAmountWithAdjustment();
    }

    public Person getPerson() {
        return getTransaction().getEvent().getPerson();
    }

}
