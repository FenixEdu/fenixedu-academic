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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GuideEntry;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonAccount;
import org.fenixedu.academic.domain.gratuity.ReimbursementGuideState;
import org.fenixedu.academic.domain.reimbursementGuide.ReimbursementGuideEntry;
import org.fenixedu.academic.domain.student.Registration;
import org.joda.time.DateTime;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceTransaction extends InsuranceTransaction_Base {

    public InsuranceTransaction() {
        super();
    }

    public InsuranceTransaction(Double value, Timestamp transactionDate, String remarks, PaymentType paymentType,
            TransactionType transactionType, Boolean wasInternalBalance, Person responsiblePerson, PersonAccount personAccount,
            GuideEntry guideEntry, ExecutionYear executionYear, Registration registration) {
        this();
        setValue(value);
        setTransactionDate(transactionDate);
        setRemarks(remarks);
        setPaymentType(paymentType);
        setTransactionType(transactionType);
        setWasInternalBalance(wasInternalBalance);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
        setExecutionYear(executionYear);
        setGuideEntry(guideEntry);
        setStudent(registration);
    }

    public InsuranceTransaction(final BigDecimal value, final DateTime whenRegistered, final PaymentType paymentType,
            final Person responsiblePerson, final PersonAccount personAccount, final ExecutionYear executionYear,
            final Registration registration) {
        this();
        setValueBigDecimal(value);
        setTransactionDateDateTime(whenRegistered);
        setPaymentType(paymentType);
        setTransactionType(TransactionType.INSURANCE_PAYMENT);
        setWasInternalBalance(false);
        setResponsiblePerson(responsiblePerson);
        setPersonAccount(personAccount);
        setExecutionYear(executionYear);
        setStudent(registration);
    }

    public boolean isReimbursed() {
        if (getGuideEntry() == null || getGuideEntry().getReimbursementGuideEntriesSet().size() == 0) {
            return false;
        } else {
            for (final ReimbursementGuideEntry reimbursementGuideEntry : getGuideEntry().getReimbursementGuideEntriesSet()) {
                if (reimbursementGuideEntry.getReimbursementGuide().getActiveReimbursementGuideSituation()
                        .getReimbursementGuideState().equals(ReimbursementGuideState.PAYED)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public void delete() {
        setExecutionYear(null);
        setStudent(null);
        super.delete();
    }

}
