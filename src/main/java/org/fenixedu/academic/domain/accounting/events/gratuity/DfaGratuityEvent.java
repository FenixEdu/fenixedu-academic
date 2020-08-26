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
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.YearMonthDay;

public class DfaGratuityEvent extends DfaGratuityEvent_Base {

    protected DfaGratuityEvent() {
        super();
    }

    public DfaGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
            StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {

        this();

        checkRulesToCreate(studentCurricularPlan);
        init(administrativeOffice, person, studentCurricularPlan, executionYear);
        persistDueDateAmountMap();
    }

    private void checkRulesToCreate(StudentCurricularPlan studentCurricularPlan) {
        if (!studentCurricularPlan.getDegreeType().isAdvancedFormationDiploma()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.gratuity.DfaGratuityEvent.invalid.degreeType");
        }
    }

    @Override
    public boolean canApplyExemption(final EventExemptionJustificationType justificationType) {
        if (isCustomEnrolmentModel()) {
            return justificationType == EventExemptionJustificationType.OTHER_INSTITUTION
                    || justificationType == EventExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION;

        }

        return true;
    }

    private Student getStudent() {
        return getStudentCurricularPlan().getRegistration().getStudent();
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        return calculateNextEndDate(new YearMonthDay());
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, PaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser,
                Collections.singletonList(new EntryDTO(EntryType.GRATUITY_FEE, this, amountToPay)), transactionDetail);
    }

    @Override
    public boolean isOtherPartiesPaymentsSupported() {
        return true;
    }

    static public Set<AccountingTransaction> readPaymentsFor(final YearMonthDay startDate, final YearMonthDay endDate) {
        return readPaymentsFor(DfaGratuityEvent.class, startDate, endDate);

    }

    @Override
    public boolean isDfaGratuityEvent() {
        return true;
    }
}
