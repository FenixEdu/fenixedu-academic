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
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class StandaloneEnrolmentGratuityEvent extends StandaloneEnrolmentGratuityEvent_Base {

    protected StandaloneEnrolmentGratuityEvent() {
        super();
    }

    public StandaloneEnrolmentGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
            StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {

        this();

        init(administrativeOffice, EventType.STANDALONE_ENROLMENT_GRATUITY, person, studentCurricularPlan, executionYear);
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    @Override
    public boolean isOpen() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).greaterThan(Money.ZERO);
    }

    @Override
    public boolean isClosed() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).lessOrEqualThan(Money.ZERO);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear());

        return labelFormatter;
    }

    @Override
    public boolean isDepositSupported() {
        return true;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.STANDALONE_ENROLMENT_GRATUITY_FEE);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter result = new LabelFormatter();
        result.appendLabel(getEventType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear());

        return result;
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();
        return Collections.singletonList(createPaymentCode(entryDTO));
    }

    private AccountingEventPaymentCode createPaymentCode(final EntryDTO entryDTO) {
        return AccountingEventPaymentCode.create(PaymentCodeType.TOTAL_GRATUITY, new YearMonthDay(),
                calculatePaymentCodeEndDate(), this, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), getPerson());
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
        } else {
            createPaymentCode(entryDTO);
        }

        return getNonProcessedPaymentCodes();
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        final LocalDate nextMonth = new LocalDate().plusMonths(1);
        return new YearMonthDay(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1).minusDays(1);
    }

    /**
     * This method deposits amount to pay directly in event
     */
    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        final AccountingTransaction transaction =
                depositAmount(responsibleUser, amountToPay, EntryType.STANDALONE_ENROLMENT_GRATUITY_FEE, transactionDetail);
        return Collections.singleton(transaction.getToAccountEntry());
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

}
