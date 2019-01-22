/**
 * Copyright © 2002 Instituto Superior Técnico
 * <p>
 * This file is part of FenixEdu Academic.
 * <p>
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.events.administrativeOfficeFee.IAdministrativeOfficeFeeEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.IInsuranceEvent;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import org.fenixedu.academic.domain.accounting.postingRules.AdministrativeOfficeFeePR;
import org.fenixedu.academic.domain.accounting.postingRules.IAdministrativeOfficeFeeAndInsurancePR;
import org.fenixedu.academic.domain.accounting.postingRules.PastAdministrativeOfficeFeeAndInsurancePR;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/***
 * Use {@link org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent} and {@link AdministrativeOfficeFeeEvent}
 */
@Deprecated
public class AdministrativeOfficeFeeAndInsuranceEvent extends AdministrativeOfficeFeeAndInsuranceEvent_Base
        implements IAdministrativeOfficeFeeEvent, IInsuranceEvent {

    static {
        getRelationPersonAccountingEvent().addListener(new RelationAdapter<Party, Event>() {
            @Override public void beforeAdd(Party party, Event event) {
                if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent && party instanceof Person) {
                    Person person = (Person) party;
                    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                            (AdministrativeOfficeFeeAndInsuranceEvent) event;
                    if (person.hasAdministrativeOfficeFeeInsuranceEventFor(
                            administrativeOfficeFeeAndInsuranceEvent.getExecutionYear())) {
                        throw new DomainException(
                                "error.org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent.event.is.already.defined.for.execution.year");

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
        throw new DomainException("Can't be created anymore");
    }

    @Override protected LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = super.getDescriptionForEntryType(entryType);
        labelFormatter.appendLabel(" - ").appendLabel(getExecutionYear().getYear());
        return labelFormatter;
    }

    @Override protected AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
        return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override protected Account getFromAccount() {
        return getPerson().getExternalAccount();
    }

    @Override public Account getToAccount() {
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

    private IAdministrativeOfficeFeeAndInsurancePR getAdministrativeOfficeFeeAndInsurancePR() {
        return (IAdministrativeOfficeFeeAndInsurancePR) getPostingRule();
    }

    public Money getAdministrativeOfficeFeeAmount() {
        return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeeAmount(this, getStartDate(), getEndDate());
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate() {
        return getPaymentEndDate() != null ? getPaymentEndDate() : ((IAdministrativeOfficeFeeAndInsurancePR) getPostingRule())
                .getAdministrativeOfficeFeePaymentLimitDate(getStartDate(), getEndDate());
    }

    public Money getAdministrativeOfficeFeePenaltyAmount() {
        return getAdministrativeOfficeFeeAndInsurancePR()
                .getAdministrativeOfficeFeePenaltyAmount(this, getStartDate(), getEndDate());
    }

    public Money getInsuranceAmount() {
        PostingRule postingRule = getPostingRule();
        if (postingRule instanceof AdministrativeOfficeFeeAndInsurancePR) {
            return ((AdministrativeOfficeFeeAndInsurancePR) postingRule).getInsuranceAmount(getStartDate(), getEndDate());
        } else if (postingRule instanceof PastAdministrativeOfficeFeeAndInsurancePR) {
            return Money.ZERO;
        } else {
            throw new UnsupportedOperationException(String.format("no value for %s%n", postingRule.getClass()));
        }
    }

    @Override
    public Map<LocalDate, Money> calculateDueDateAmountMap() {
        final Map<LocalDate, Money> dueDateAmountMap = new HashMap<>();
        final LocalDate possibleDueDate = getPossibleDueDate();
        final IAdministrativeOfficeFeeAndInsurancePR officeFeeAndInsurancePR = (IAdministrativeOfficeFeeAndInsurancePR) getPostingRule();
        final DateTime startDate = this.getStartDate();
        final DateTime endDate = this.getEndDate();

        final Money insuranceAmount = officeFeeAndInsurancePR.getInsuranceAmount(startDate, endDate);
        if (insuranceAmount.isPositive()) {
            dueDateAmountMap.put(possibleDueDate.plusDays(1), insuranceAmount);
        }

        dueDateAmountMap.put(possibleDueDate, officeFeeAndInsurancePR.getAdministrativeOfficeFeeAmount(this, startDate, endDate));

        return dueDateAmountMap;
    }

    private AccountingEventPaymentCode getNonProcessedPaymentCode() {
        return (getNonProcessedPaymentCodes().isEmpty() ? null : (AccountingEventPaymentCode) getNonProcessedPaymentCodes().iterator().next());
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, PaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser, buildEntryDTOsFrom(amountToPay), transactionDetail);
    }

    @Override public boolean isInDebt() {
        return isOpen() && ((getPaymentEndDate() != null && getPaymentEndDate().isBefore(new YearMonthDay()))
                || getSpecificPostingRule().getWhenToApplyFixedAmountPenalty().isBefore(new YearMonthDay()));
    }

    private AdministrativeOfficeFeePR getSpecificPostingRule() {
        return (AdministrativeOfficeFeePR) getServiceAgreementTemplate()
                .findPostingRuleBy(EventType.ADMINISTRATIVE_OFFICE_FEE, getStartDate(), getEndDate());
    }

    private List<EntryDTO> buildEntryDTOsFrom(final Money amountToPay) {
        final List<EntryDTO> result = new ArrayList<>(2);
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

    public void changePaymentCodeState(DateTime whenRegistered, PaymentMethod paymentMethod) {
        if (canCloseEvent(whenRegistered) && hasNonProcessedPaymentCode()) {
            getNonProcessedPaymentCode().setState(getPaymentCodeStateFor(paymentMethod));
        }
    }

    private boolean hasNonProcessedPaymentCode() {
        return (getNonProcessedPaymentCode() != null);
    }

    public boolean hasAdministrativeOfficeFeeAndInsurancePenaltyExemption() {
        return getAdministrativeOfficeFeeAndInsurancePenaltyExemption() != null;
    }

    public AdministrativeOfficeFeeAndInsurancePenaltyExemption getAdministrativeOfficeFeeAndInsurancePenaltyExemption() {
        return (AdministrativeOfficeFeeAndInsurancePenaltyExemption) getExemptionsSet().stream()
                .filter(exemption -> exemption instanceof AdministrativeOfficeFeeAndInsurancePenaltyExemption)
                .findFirst().orElse(null);

    }

    public boolean hasAdministrativeOfficeFeeAndInsuranceExemption() {
        return getAdministrativeOfficeFeeAndInsuranceExemption() != null;
    }

    public Exemption getAdministrativeOfficeFeeAndInsuranceExemption() {
        return getExemptionsSet().stream().filter(Exemption::isForAdministrativeOfficeFee).findFirst().orElse(null);
    }

    @Override public void setPaymentEndDate(YearMonthDay paymentEndDate) {
        if (!isOpen()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent.payment.end.date.can.only.be.modified.on.open.events");
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
            if (transaction.getToAccountEntry().getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE && transaction
                    .isPayed(civilYear)) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    @Override public EntryType getEntryType() {
        return EntryType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE;
    }

    @Override public boolean isOpen() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).greaterThan(Money.ZERO);
    }

    @Override public boolean isClosed() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).lessOrEqualThan(Money.ZERO);
    }

    @Override public boolean isInState(final EventState eventState) {
        switch (eventState) {
            case OPEN:
                return isOpen();
            case CLOSED:
                return isClosed();
            case CANCELLED:
                return isCancelled();
            default:
                throw new DomainException(
                        "error.org.fenixedu.academic.domain.accounting.events.gratuity.DfaGratuityEvent.unexpected.state.to.test");
        }
    }

    @Override public boolean isAdministrativeOfficeAndInsuranceEvent() {
        return true;
    }

    public Exemption getInsuranceExemption() {
        return getExemptionsSet().stream().filter(Exemption::isForInsurance).findFirst().orElse(null);
    }

    public boolean hasInsuranceExemption() {
        return getInsuranceExemption() != null;
    }

    private LocalDate getPossibleDueDate() {
        final YearMonthDay ymd = getAdministrativeOfficeFeePaymentLimitDate();
        return ymd != null ? ymd.plusDays(1).toDateTimeAtMidnight().toLocalDate() : getDueDateByPaymentCodes().toLocalDate();
    }

}
