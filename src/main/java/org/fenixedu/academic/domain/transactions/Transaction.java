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
package org.fenixedu.academic.domain.transactions;

import java.math.BigDecimal;
import java.util.Comparator;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public abstract class Transaction extends Transaction_Base {

    public static Comparator<Transaction> COMPARATOR_BY_TRANSACTION_DATE_TIME = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction leftTransaction, Transaction rightTransaction) {
            int comparationResult =
                    leftTransaction.getTransactionDateDateTime().compareTo(rightTransaction.getTransactionDateDateTime());
            return (comparationResult == 0) ? leftTransaction.getExternalId().compareTo(rightTransaction.getExternalId()) : comparationResult;
        }
    };

    public Transaction() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Deprecated
    public Double getValue() {
        return getValueBigDecimal().doubleValue();
    }

    @Deprecated
    public void setValue(Double value) {
        setValueBigDecimal(BigDecimal.valueOf(value));
    }

    public void delete() {
        setPersonAccount(null);
        setResponsiblePerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getTransactionDate() {
        org.joda.time.DateTime dt = getTransactionDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setTransactionDate(java.util.Date date) {
        if (date == null) {
            setTransactionDateDateTime(null);
        } else {
            setTransactionDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
