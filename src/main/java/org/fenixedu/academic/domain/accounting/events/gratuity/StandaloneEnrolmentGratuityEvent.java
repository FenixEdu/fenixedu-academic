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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;

public class StandaloneEnrolmentGratuityEvent extends StandaloneEnrolmentGratuityEvent_Base {

    protected StandaloneEnrolmentGratuityEvent() {
        super();
    }

    public StandaloneEnrolmentGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
            StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {

        this();

        init(administrativeOffice, EventType.STANDALONE_ENROLMENT_GRATUITY, person, studentCurricularPlan, executionYear);
        persistDueDateAmountMap();
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    @Override public EntryType getEntryType() {
        return EntryType.STANDALONE_ENROLMENT_GRATUITY_FEE;
    }

    /**
     * This method deposits amount to pay directly in event
     */
    @Override
    protected Set<Entry> internalProcess(User responsibleUser, PaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        final AccountingTransaction transaction =
                depositAmount(responsibleUser, amountToPay, EntryType.STANDALONE_ENROLMENT_GRATUITY_FEE, transactionDetail);
        return Collections.singleton(transaction.getToAccountEntry());
    }

}
