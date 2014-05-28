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
package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventState;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.events.administrativeOfficeFee.IAdministrativeOfficeFeeEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.IInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.AdministrativeOfficeFeePR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AdministrativeOfficeFeeAndInsuranceEvent extends AdministrativeOfficeFeeAndInsuranceEvent_Base implements
        IAdministrativeOfficeFeeEvent, IInsuranceEvent {

    static {
        getRelationPersonAccountingEvent().addListener(new RelationAdapter<Party, Event>() {
            @Override
            public void beforeAdd(Party party, Event event) {
                if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent && party != null && party instanceof Person) {
                    Person person = (Person) party;
                    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                            (AdministrativeOfficeFeeAndInsuranceEvent) event;
                    if (person.hasAdministrativeOfficeFeeInsuranceEventFor(administrativeOfficeFeeAndInsuranceEvent
                            .getExecutionYear())) {
                        throw new DomainException(
                                "error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent.event.is.already.defined.for.execution.year");

                    }

                }
            }
        });
    }

    protected AdministrativeOfficeFeeAndInsuranceEvent() {
        super();
    }

    public AdministrativeOfficeFeeAndInsuranceEvent(AdministrativeOffice administrativeOffice, Person person,
            ExecutionYear executionYear) {
        this();
        init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, person, executionYear);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(getExecutionYear().getYear());

        return labelFormatter;
    }

    @Override
    protected AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
        return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getExternalAccount();
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    public boolean isInsuranceAmountIncludedInDebt() {
        return !getPerson().hasInsuranceEventFor(getExecutionYear());
    }

    public boolean hasToPayInsurance() {
        if (!isInsuranceAmountIncludedInDebt()) {
            return false;
        }

        if (hasInsuranceExemption()) {
            return false;
        }

        return getInsurancePayedAmount().lessThan(getInsuranceAmount());
    }

    public boolean hasToPayAdministrativeOfficeFee() {
        return getAdministrativeOfficeFeePayedAmount().lessThan(getAdministrativeOfficeFeeAmount());
    }

    private AdministrativeOfficeFeeAndInsurancePR getAdministrativeOfficeFeeAndInsurancePR() {
        return (AdministrativeOfficeFeeAndInsurancePR) getPostingRule();
    }

    public Money getAdministrativeOfficeFeeAmount() {
        return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeeAmount(getStartDate(), getEndDate());
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate() {
        return getPaymentEndDate() != null ? getPaymentEndDate() : getAdministrativeOfficeFeeAndInsurancePR()
                .getAdministrativeOfficeFeePaymentLimitDate(getStartDate(), getEndDate());
    }

    public Money getAdministrativeOfficeFeePenaltyAmount() {
        return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeePenaltyAmount(getStartDate(), getEndDate());
    }

    public Money getInsuranceAmount() {
        return getAdministrativeOfficeFeeAndInsurancePR().getInsuranceAmount(getStartDate(), getEndDate());
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
        AccountingEventPaymentCode paymentCode = findPaymentCodeInStudentCandidacy();

        if (paymentCode != null) {
            paymentCode.setAccountingEvent(this);
            return Collections.singletonList(paymentCode);

        }

        final Money totalAmount = calculateTotalAmount();
        return Collections.singletonList(AccountingEventPaymentCode.create(
                PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE, new YearMonthDay(), calculatePaymentCodeEndDate(), this,
                totalAmount, totalAmount, getPerson()));
    }

    private AccountingEventPaymentCode findPaymentCodeInStudentCandidacy() {
        if (!getPerson().hasStudent()) {
            return null;
        }

        if (getPerson().getStudent().getActiveRegistrationsIn(getExecutionYear().getFirstExecutionPeriod()).size() != 1) {
            return null;
        }

        StudentCandidacy studentCandidacy = getActiveDgesCandidacy(getPerson());

        if (studentCandidacy == null) {
            return null;
        }

        for (PaymentCode paymentCode : studentCandidacy.getAvailablePaymentCodesSet()) {
            if (!paymentCode.isNew()) {
                continue;
            }

            if (!paymentCode.getClass().equals(AccountingEventPaymentCode.class)) {
                continue;
            }

            AccountingEventPaymentCode accountingEventPaymentCode = (AccountingEventPaymentCode) paymentCode;
            if (accountingEventPaymentCode.hasAccountingEvent()) {
                continue;
            }

            if (!PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE.equals(accountingEventPaymentCode.getType())) {
                continue;
            }

            if (!getExecutionYear().containsDate(accountingEventPaymentCode.getStartDate().toDateTimeAtMidnight())) {
                continue;
            }

            return accountingEventPaymentCode;
        }

        return null;
    }

    private StudentCandidacy getActiveDgesCandidacy(Person person) {
        for (Candidacy candidacy : person.getCandidacies()) {
            if (!candidacy.isActive()) {
                continue;
            }

            if (!(candidacy instanceof StudentCandidacy)) {
                continue;
            }

            StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
            if (!studentCandidacy.hasDgesStudentImportationProcess()) {
                continue;
            }

            return studentCandidacy;
        }

        return null;
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
        final Money totalAmount = calculateTotalAmount();
        final AccountingEventPaymentCode nonProcessedPaymentCode = getNonProcessedPaymentCode();

        if (nonProcessedPaymentCode != null) {
            nonProcessedPaymentCode.update(new YearMonthDay(), calculatePaymentCodeEndDate(), totalAmount, totalAmount);
        } else {
            final AccountingEventPaymentCode paymentCode = getCancelledPaymentCode();
            if (paymentCode != null) {
                paymentCode.update(new YearMonthDay(), calculatePaymentCodeEndDate(), totalAmount, totalAmount);
                paymentCode.setState(PaymentCodeState.NEW);
            }
        }

        return getNonProcessedPaymentCodes();
    }

    private AccountingEventPaymentCode getCancelledPaymentCode() {
        return (getCancelledPaymentCodes().isEmpty() ? null : getCancelledPaymentCodes().iterator().next());
    }

    private AccountingEventPaymentCode getNonProcessedPaymentCode() {
        return (getNonProcessedPaymentCodes().isEmpty() ? null : getNonProcessedPaymentCodes().iterator().next());
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        final YearMonthDay today = new YearMonthDay();
        final YearMonthDay administrativeOfficeFeePaymentLimitDate = getAdministrativeOfficeFeePaymentLimitDate();
        return today.isBefore(administrativeOfficeFeePaymentLimitDate) ? administrativeOfficeFeePaymentLimitDate : calculateNextEndDate(today);
    }

    private Money calculateTotalAmount() {
        Money totalAmount = Money.ZERO;
        for (final EntryDTO entryDTO : calculateEntries()) {
            totalAmount = totalAmount.add(entryDTO.getAmountToPay());
        }
        return totalAmount;
    }

    public AccountingEventPaymentCode calculatePaymentCode() {
        return calculatePaymentCodes().iterator().next();
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser, buildEntryDTOsFrom(amountToPay), transactionDetail);
    }

    @Override
    public boolean isInDebt() {
        return isOpen()
                && ((getPaymentEndDate() != null && getPaymentEndDate().isBefore(new YearMonthDay())) || getSpecificPostingRule()
                        .getWhenToApplyFixedAmountPenalty().isBefore(new YearMonthDay()));
    }

    private AdministrativeOfficeFeePR getSpecificPostingRule() {
        return (AdministrativeOfficeFeePR) getServiceAgreementTemplate().findPostingRuleBy(EventType.ADMINISTRATIVE_OFFICE_FEE,
                getStartDate(), getEndDate());
    }

    private List<EntryDTO> buildEntryDTOsFrom(final Money amountToPay) {
        final List<EntryDTO> result = new ArrayList<EntryDTO>(2);
        Money insuranceAmountToDiscount = Money.ZERO;
        if (hasToPayInsurance()) {
            insuranceAmountToDiscount = getInsuranceAmount();
            result.add(buildInsuranceEntryDTO(insuranceAmountToDiscount));
        }

        final Money remainingAmount = amountToPay.subtract(insuranceAmountToDiscount);
        if (remainingAmount.isPositive()) {
            result.add(buildAdministrativeOfficeFeeEntryDTO(remainingAmount));
        }

        return result;
    }

    private EntryDTO buildAdministrativeOfficeFeeEntryDTO(Money administrativeOfficeFeeAmountToDiscount) {
        return new EntryDTO(EntryType.ADMINISTRATIVE_OFFICE_FEE, this, administrativeOfficeFeeAmountToDiscount);
    }

    private EntryDTO buildInsuranceEntryDTO(Money insuranceAmountToDiscount) {
        return new EntryDTO(EntryType.INSURANCE_FEE, this, insuranceAmountToDiscount);
    }

    public void changePaymentCodeState(DateTime whenRegistered, PaymentMode paymentMode) {
        if (canCloseEvent(whenRegistered) && hasNonProcessedPaymentCode()) {
            getNonProcessedPaymentCode().setState(getPaymentCodeStateFor(paymentMode));
        }
    }

    private boolean hasNonProcessedPaymentCode() {
        return (getNonProcessedPaymentCode() != null);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        labelFormatter.appendLabel(" ").appendLabel(getExecutionYear().getYear());
        return labelFormatter;
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    public boolean hasAdministrativeOfficeFeeAndInsurancePenaltyExemption() {
        return getAdministrativeOfficeFeeAndInsurancePenaltyExemption() != null;
    }

    public AdministrativeOfficeFeeAndInsurancePenaltyExemption getAdministrativeOfficeFeeAndInsurancePenaltyExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof AdministrativeOfficeFeeAndInsurancePenaltyExemption) {
                return (AdministrativeOfficeFeeAndInsurancePenaltyExemption) exemption;
            }
        }

        return null;
    }

    public boolean hasAdministrativeOfficeFeeAndInsuranceExemption() {
        return getAdministrativeOfficeFeeAndInsuranceExemption() != null;
    }

    public Exemption getAdministrativeOfficeFeeAndInsuranceExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption.isForAdministrativeOfficeFee()) {
                return exemption;
            }
        }

        return null;
    }

    @Override
    public void setPaymentEndDate(YearMonthDay paymentEndDate) {
        if (!isOpen()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent.payment.end.date.can.only.be.modified.on.open.events");
        }

        super.setPaymentEndDate(paymentEndDate);
    }

    public Money getInsurancePayedAmount() {
        Money result = Money.ZERO;

        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.getToAccountEntry().getEntryType() == EntryType.INSURANCE_FEE) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;

    }

    public Money getInsurancePayedAmountFor(int civilYear) {
        Money result = Money.ZERO;

        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.getToAccountEntry().getEntryType() == EntryType.INSURANCE_FEE && transaction.isPayed(civilYear)) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    public Money getAdministrativeOfficeFeePayedAmount() {
        Money result = Money.ZERO;

        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.getToAccountEntry().getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    public Money getAdministrativeOfficeFeePayedAmountFor(int civilYear) {
        Money result = Money.ZERO;

        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.getToAccountEntry().getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE
                    && transaction.isPayed(civilYear)) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    @Override
    public Money calculateAmountToPay(DateTime whenRegistered) {
        Money result = super.calculateAmountToPay(whenRegistered);
        if (result.isZero()) {
            return result;
        }

        result = result.subtract(getPerson().hasInsuranceEventFor(getExecutionYear()) ? getInsuranceAmount() : Money.ZERO);

        return result.isPositive() ? result : Money.ZERO;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        final Set<EntryType> result = new HashSet<EntryType>();
        result.add(EntryType.ADMINISTRATIVE_OFFICE_FEE);
        result.add(EntryType.INSURANCE_FEE);

        return result;
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
    public boolean isInState(final EventState eventState) {
        if (eventState == EventState.OPEN) {
            return isOpen();
        } else if (eventState == EventState.CLOSED) {
            return isClosed();
        } else if (eventState == EventState.CANCELLED) {
            return isCancelled();
        } else {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent.unexpected.state.to.test");
        }
    }

    @Override
    public boolean isAdministrativeOfficeAndInsuranceEvent() {
        return true;
    }

    public Exemption getInsuranceExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption.isForInsurance()) {
                return exemption;
            }
        }

        return null;

    }

    public boolean hasInsuranceExemption() {
        return getInsuranceExemption() != null;
    }

    @Deprecated
    public boolean hasPaymentEndDate() {
        return getPaymentEndDate() != null;
    }

}
