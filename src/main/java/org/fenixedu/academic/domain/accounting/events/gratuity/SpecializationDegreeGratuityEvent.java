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
package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class SpecializationDegreeGratuityEvent extends SpecializationDegreeGratuityEvent_Base {

    protected SpecializationDegreeGratuityEvent() {
        super();
    }

    public SpecializationDegreeGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
            StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
        this();

        checkRulesToCreate(studentCurricularPlan);

        init(administrativeOffice, person, studentCurricularPlan, executionYear);
    }

    private void checkRulesToCreate(StudentCurricularPlan studentCurricularPlan) {
        if (studentCurricularPlan.getDegreeType() != DegreeType.BOLONHA_SPECIALIZATION_DEGREE) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.SpecializationDegreeGratuityEvent.invalid.degreeType");
        }
    }

    @Override
    public boolean canApplyExemption(final GratuityExemptionJustificationType justificationType) {
        if (isCustomEnrolmentModel()) {
            return justificationType == GratuityExemptionJustificationType.OTHER_INSTITUTION
                    || justificationType == GratuityExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION;

        }

        return true;
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();

        if (!getNonProcessedPaymentCodes().isEmpty()) {
            getNonProcessedPaymentCodes()
                    .iterator()
                    .next()
                    .update(new YearMonthDay(), calculatePaymentCodeEndDate(), entryDTO.getAmountToPay(),
                            entryDTO.getAmountToPay());
        }

        return getNonProcessedPaymentCodes();
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();

        return Collections.singletonList(AccountingEventPaymentCode.create(PaymentCodeType.TOTAL_GRATUITY, new YearMonthDay(),
                calculatePaymentCodeEndDate(), this, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), getStudent()
                        .getPerson()));
    }

    private Student getStudent() {
        return getStudentCurricularPlan().getRegistration().getStudent();
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        return calculateNextEndDate(new YearMonthDay());
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser,
                Collections.singletonList(new EntryDTO(EntryType.GRATUITY_FEE, this, amountToPay)), transactionDetail);
    }

    @Override
    public boolean isOtherPartiesPaymentsSupported() {
        return true;
    }

    static public Set<AccountingTransaction> readPaymentsFor(final YearMonthDay startDate, final YearMonthDay endDate) {
        return readPaymentsFor(SpecializationDegreeGratuityEvent.class, startDate, endDate);

    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.GRATUITY_FEE);
    }

}
