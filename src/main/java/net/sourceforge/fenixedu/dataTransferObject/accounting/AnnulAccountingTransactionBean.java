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
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class AnnulAccountingTransactionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1104560585375265754L;

    private AccountingTransaction transaction;

    private String reason;

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

    public PaymentMode getPaymentMode() {
        return getTransaction().getTransactionDetail().getPaymentMode();
    }

    public Money getAmountWithAdjustment() {
        return getTransaction().getAmountWithAdjustment();
    }

    public Person getPerson() {
        return getTransaction().getEvent().getPerson();
    }

}
