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
import java.sql.Timestamp;

import org.fenixedu.academic.domain.GratuitySituation;
import org.fenixedu.academic.domain.GuideEntry;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonAccount;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
@Deprecated
public class GratuityTransaction extends GratuityTransaction_Base {

    public GratuityTransaction() {
        super();
    }

    public GratuityTransaction(Double value, Timestamp transactionDate, String remarks, PaymentType paymentType,
            TransactionType transactionType, Boolean wasInternalBalance, Person responsiblePerson, PersonAccount personAccount,
            GuideEntry guideEntry, GratuitySituation gratuitySituation) {
        this();
        setValue(value);
        setTransactionDate(transactionDate);
        setRemarks(remarks);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(wasInternalBalance);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
        setGuideEntry(guideEntry);
        setGratuitySituation(gratuitySituation);
    }

    public GratuityTransaction(final BigDecimal value, final DateTime transactionDateTime, final PaymentType paymentType,
            final TransactionType transactionType, final Person responsiblePerson, final PersonAccount personAccount,
            final GratuitySituation gratuitySituation) {
        this();
        setValueBigDecimal(value);
        setTransactionDateDateTime(transactionDateTime);
        setRemarks(null);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(false);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
        setGuideEntry(null);
        setGratuitySituation(gratuitySituation);
    }

    public boolean isInsidePeriod(final YearMonthDay start, final YearMonthDay end) {
        final YearMonthDay date = getTransactionDateDateTime().toYearMonthDay();
        return !date.isBefore(start) && !date.isAfter(end);
    }

}
