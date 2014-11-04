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
package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class SmsTransaction extends SmsTransaction_Base {

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
    public SmsTransaction(Double value, Timestamp transactionDate, String remarks, PaymentType paymentType,
            TransactionType transactionType, Boolean wasInternalBalance, Person responsiblePerson, PersonAccount personAccount,
            GuideEntry guideEntry) {
        setValue(value);
        setTransactionDate(transactionDate);
        setRemarks(remarks);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(wasInternalBalance);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
        setGuideEntry(guideEntry);
    }
}