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
import org.fenixedu.academic.domain.reimbursementGuide.ReimbursementGuideEntry;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public abstract class PaymentTransaction extends PaymentTransaction_Base {

    public PaymentTransaction() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
     * @param guideEntry
     */
    public PaymentTransaction(BigDecimal value, Timestamp transactionDate, String remarks, PaymentType paymentType,
            TransactionType transactionType, Boolean wasInternalBalance, Person responsiblePerson, PersonAccount personAccount,
            GuideEntry guideEntry) {
        this();
        setGuideEntry(guideEntry);
        setValueBigDecimal(value);
        setTransactionDate(transactionDate);
        setRemarks(remarks);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(wasInternalBalance);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
    }

    @Override
    public void delete() {
        if (this instanceof GratuityTransaction) {
            GratuityTransaction gratuityTransaction = (GratuityTransaction) this;
            GratuitySituation gratuitySituation = gratuityTransaction.getGratuitySituation();
            gratuityTransaction.setGratuitySituation(null);

            gratuitySituation.updateValues();
        }

        setGuideEntry(null);
        super.delete();
    }

    public BigDecimal getValueWithAdjustment() {

        BigDecimal reimbursedValue = BigDecimal.ZERO;
        if (getGuideEntry() != null) {
            for (final ReimbursementGuideEntry reimbursementGuideEntry : getGuideEntry().getReimbursementGuideEntriesSet()) {
                if (reimbursementGuideEntry.getReimbursementGuide().isPayed()) {
                    reimbursedValue = reimbursedValue.add(reimbursementGuideEntry.getValueBigDecimal());
                }
            }
        }

        return getValueBigDecimal().subtract(reimbursedValue);
    }

}
