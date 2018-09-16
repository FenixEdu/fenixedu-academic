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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeState;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.accounting.paymentCodes.InstallmentPaymentCode;
import org.fenixedu.academic.domain.accounting.paymentPlans.CustomGratuityPaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlan;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.EntryWithInstallmentDTO;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GratuityEventWithPaymentPlan extends GratuityEventWithPaymentPlan_Base {

    protected GratuityEventWithPaymentPlan() {
        super();
    }

    public GratuityEventWithPaymentPlan(AdministrativeOffice administrativeOffice, Person person,
            StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
        this();
        init(administrativeOffice, person, studentCurricularPlan, executionYear);

    }

    @Override
    protected void init(AdministrativeOffice administrativeOffice, Person person, StudentCurricularPlan studentCurricularPlan,
            ExecutionYear executionYear) {
        super.init(administrativeOffice, person, studentCurricularPlan, executionYear);

        configuratePaymentPlan();
    }

    @Override
    public void setGratuityPaymentPlan(final PaymentPlan gratuityPaymentPlan) {
        throw new DomainException("error.GratuityEventWithPaymentPlan.do.not.use.this.method");
    }

    public void changeGratuityPaymentPlan(final PaymentPlan paymentPlan) {
        if (paymentPlan instanceof GratuityPaymentPlan) {
            setGratuityPaymentPlan((GratuityPaymentPlan) paymentPlan);
        } else if (paymentPlan instanceof CustomGratuityPaymentPlan) {
            setGratuityPaymentPlan((CustomGratuityPaymentPlan) paymentPlan);
        } else {
            throw new DomainException("error.GratuityEventWithPaymentPlan.unexpected.payment.plan.type");
        }
    }

    public void setGratuityPaymentPlan(final GratuityPaymentPlan paymentPlan) {
        if (paymentPlan != null) {
            ensureServiceAgreement();
            super.setGratuityPaymentPlan(paymentPlan);
        }
    }

    public void setGratuityPaymentPlan(final CustomGratuityPaymentPlan paymentPlan) {
        if (paymentPlan != null) {
            ensureServiceAgreement();
            super.setGratuityPaymentPlan(paymentPlan);
        }
    }

    public void configuratePaymentPlan() {
        ensureServiceAgreement();

        if (getGratuityPaymentPlan() == null) {
            super.setGratuityPaymentPlan(getDegreeCurricularPlanServiceAgreement().getGratuityPaymentPlanFor(
                    getStudentCurricularPlan(), getExecutionYear()));
        }

        if (getGratuityPaymentPlan() == null) {
            throw new DomainException("error.accounting.events.gratuity.paymentPlan.has.to.exist");
        }
    }

    private void ensureServiceAgreement() {
        if (getDegreeCurricularPlanServiceAgreement() == null) {
            new DegreeCurricularPlanServiceAgreement(getPerson(),
                    DegreeCurricularPlanServiceAgreementTemplate.readByDegreeCurricularPlan(getStudentCurricularPlan()
                            .getDegreeCurricularPlan()));
        }
    }

    public void configurateCustomPaymentPlan() {
        if (!hasCustomGratuityPaymentPlan()) {
            ensureServiceAgreement();
            super.setGratuityPaymentPlan(new CustomGratuityPaymentPlan(getExecutionYear(),
                    getDegreeCurricularPlanServiceAgreement()));
        }
    }

    public void configurateDefaultPaymentPlan() {
        if (!hasDefaultGratuityPaymentPlan()) {
            ensureServiceAgreement();
            final GratuityPaymentPlan paymentPlan =
                    getDegreeCurricularPlanServiceAgreement().getDefaultGratuityPaymentPlan(getExecutionYear());
            if (paymentPlan == null) {
                throw new DomainException("error.GratuityEventWithPaymentPlan.cannot.set.null.payment.plan");
            }
            super.setGratuityPaymentPlan(paymentPlan);
        }
    }

    public void changeGratuityTotalPaymentCodeState(final PaymentCodeState paymentCodeState) {
        for (final AccountingEventPaymentCode accountingEventPaymentCode : getNonProcessedPaymentCodes()) {
            if (!(accountingEventPaymentCode instanceof InstallmentPaymentCode)) {
                accountingEventPaymentCode.setState(paymentCodeState);
            }
        }
    }

    public void changeInstallmentPaymentCodeState(final Installment installment, final PaymentCodeState paymentCodeState) {
        for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
            if (paymentCode instanceof InstallmentPaymentCode
                    && ((InstallmentPaymentCode) paymentCode).getInstallment() == installment) {
                paymentCode.setState(paymentCodeState);
            }
        }

        // If at least one installment is payed, we assume that the payment
        // will be based on installments
        changeGratuityTotalPaymentCodeState(PaymentCodeState.CANCELLED);
    }

    public DegreeCurricularPlanServiceAgreement getDegreeCurricularPlanServiceAgreement() {
        return (DegreeCurricularPlanServiceAgreement) getPerson().getServiceAgreementFor(getServiceAgreementTemplate());

    }

    @Override
    public boolean hasInstallments() {
        return true;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, PaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser, Collections.singletonList(buildEntryDTOFrom(paymentCode, amountToPay)),
                transactionDetail);
    }

    private EntryDTO buildEntryDTOFrom(final PaymentCode paymentCode, final Money amountToPay) {
        if (paymentCode instanceof InstallmentPaymentCode) {
            return new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, this, amountToPay,
                    ((InstallmentPaymentCode) paymentCode).getInstallment());
        } else {
            return new EntryDTO(EntryType.GRATUITY_FEE, this, amountToPay);
        }
    }

    private boolean installmentIsInDebtToday(Installment installment) {
        return installmentIsInDebt(installment) && new YearMonthDay().isAfter(installment.getEndDate());
    }

    private boolean installmentIsInDebt(Installment installment) {
        return getGratuityPaymentPlan().isInstallmentInDebt(installment, this, new DateTime(),
                calculateDiscountPercentage(getGratuityPaymentPlan().calculateOriginalTotalAmount()));
    }

    private boolean hasAnyInstallmentInDebtToday() {
        return getInstallments().stream().anyMatch(this::installmentIsInDebtToday);
    }

    @Override
    public boolean isInDebt() {
        return isOpen() && hasAnyInstallmentInDebtToday();
    }

    public boolean hasPenaltyExemptionFor(final Installment installment) {
        return getExemptionsSet().stream()
                .filter(exemption -> exemption instanceof InstallmentPenaltyExemption)
                .anyMatch(exemption -> ((InstallmentPenaltyExemption) exemption).getInstallment() == installment);
    }

    public List<Installment> getInstallments() {
        return getGratuityPaymentPlan().getInstallmentsSortedByEndDate();
    }

    public List<InstallmentPenaltyExemption> getInstallmentPenaltyExemptions() {
        return getExemptionsSet().stream()
                .filter(exemption -> exemption instanceof InstallmentPenaltyExemption)
                .map(exemption -> (InstallmentPenaltyExemption) exemption)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isOtherPartiesPaymentsSupported() {
        return true;
    }

    @Override
    protected void disconnect() {
        if (hasCustomGratuityPaymentPlan()) {
            super.getGratuityPaymentPlan().delete();
        }
        super.setGratuityPaymentPlan(null);
        super.disconnect();
    }

    @Override
    public boolean isGratuityEventWithPaymentPlan() {
        return true;
    }

    public boolean hasCustomGratuityPaymentPlan() {
        return getGratuityPaymentPlan() != null && getGratuityPaymentPlan().isCustomGratuityPaymentPlan();
    }

    public boolean hasDefaultGratuityPaymentPlan() {
        return getGratuityPaymentPlan() != null && getGratuityPaymentPlan().isDefault();
    }

    public Money getPayedAmountLessPenalty() {
        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.calculatePayedAmount.on.invalid.events");
        }

        final DateTime now = new DateTime();
        Money result = Money.ZERO;

        for (final Installment installment : getGratuityPaymentPlan().getInstallmentsSet()) {
            if (!getGratuityPaymentPlan().isInstallmentInDebt(installment, this, now, BigDecimal.ZERO)) {
                result = result.add(installment.getAmount());
            }
        }

        if (result.isPositive()) {
            result = result.subtract(getTotalDiscount());
        }

        return result.isPositive() ? result.add(getPayedAmountLessInstallments()) : getPayedAmountLessInstallments();
    }

    private Money getPayedAmountLessInstallments() {
        Money payedAmount = Money.ZERO;
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (!transaction.isInstallment()) {
                payedAmount = payedAmount.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }
        return payedAmount;
    }

    @Override
    public GratuityWithPaymentPlanPR getPostingRule() {
        return (GratuityWithPaymentPlanPR) super.getPostingRule();
    }

    @Override
    public boolean isPaymentPlanChangeAllowed() {
        return true;
    }

    @Override
    public Map<LocalDate, Money> getDueDateAmountMap(PostingRule postingRule, DateTime when) {
        return getGratuityPaymentPlan().getInstallmentsSet().stream().filter( i -> i.calculateBaseAmount(this).greaterThan(Money.ZERO))
                   .collect(Collectors.toMap(i -> i.getEndDate(this), i -> i.calculateBaseAmount(this)));
    }

    @Override
    public PaymentPlan getPaymentPlan() {
        return getGratuityPaymentPlan();
    }
}
