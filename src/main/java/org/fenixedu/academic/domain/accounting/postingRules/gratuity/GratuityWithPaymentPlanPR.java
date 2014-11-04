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
package org.fenixedu.academic.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.accountingTransactions.InstallmentAccountingTransaction;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.EntryWithInstallmentDTO;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class GratuityWithPaymentPlanPR extends GratuityWithPaymentPlanPR_Base implements IGratuityPR {

    protected GratuityWithPaymentPlanPR() {
        super();
    }

    public GratuityWithPaymentPlanPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate) {
        this(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate);
    }

    public GratuityWithPaymentPlanPR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final BigDecimal discountPercentage = applyDiscount ? getDiscountPercentage(event) : BigDecimal.ZERO;
        return getPaymentPlan(event).calculateTotalAmount(event, when, discountPercentage);
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        return amountToPay;
    }

    public BigDecimal getDiscountPercentage(final Event event) {
        PaymentPlan paymentPlan = getPaymentPlan(event);
        if (paymentPlan == null) {
            throw new DomainException("error.event.not.associated.paymentPlan", event.getClass().getName());
        }
        return ((GratuityEventWithPaymentPlan) event).calculateDiscountPercentage(paymentPlan.calculateBaseAmount(event));
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        final List<EntryDTO> result = new ArrayList<EntryDTO>();
        final Map<Installment, Money> amountsByInstallment =
                getPaymentPlan(event).calculateInstallmentRemainingAmounts(event, when, getDiscountPercentage(event));

        for (final Installment installment : getPaymentPlan(event).getInstallmentsSortedByEndDate()) {
            final Money installmentAmount = amountsByInstallment.get(installment);

            if (installmentAmount == null || !installmentAmount.isPositive()) {
                continue;
            }

            result.add(new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, event, installmentAmount, event
                    .getDescriptionForEntryType(getEntryType()), installment));

        }

        if (needsTotalAmountEntry(getPaymentPlan(event), result, event, when)) {
            final Money amountToPay = event.calculateAmountToPay(when);
            result.add(new EntryDTO(EntryType.GRATUITY_FEE, event, amountToPay, event.getPayedAmount(), amountToPay, event
                    .getDescriptionForEntryType(getEntryType()), amountToPay));
        }

        return result;
    }

    private boolean needsTotalAmountEntry(final PaymentPlan paymentPlan, List<EntryDTO> result, final Event event,
            final DateTime when) {
        return (paymentPlan.getInstallmentsSet().size() != 1 && paymentPlan.getInstallmentsSet().size() == result.size());
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan = (GratuityEventWithPaymentPlan) event;

        if (entryDTOs.size() > 1) {
            final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
            for (final EntryDTO each : entryDTOs) {
                if (!(each instanceof EntryWithInstallmentDTO)) {
                    throw new DomainExceptionWithLabelFormatter(
                            "error.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR.cannot.mix.installments.with.total.payments",
                            event.getDescriptionForEntryType(getEntryType()));
                }

                result.add(internalProcessInstallment(user, fromAccount, toAccount, each, gratuityEventWithPaymentPlan,
                        transactionDetail));
            }

            return result;
        }

        final EntryDTO entryDTO = entryDTOs.iterator().next();

        if (entryDTO instanceof EntryWithInstallmentDTO) {
            return Collections.singleton(internalProcessInstallment(user, fromAccount, toAccount, entryDTO,
                    gratuityEventWithPaymentPlan, transactionDetail));
        }

        return Collections.singleton(internalProcessTotal(user, fromAccount, toAccount, entryDTO, gratuityEventWithPaymentPlan,
                transactionDetail));

    }

    private AccountingTransaction internalProcessTotal(User user, Account fromAccount, Account toAccount, EntryDTO entryDTO,
            GratuityEventWithPaymentPlan event, AccountingTransactionDetailDTO transactionDetail) {

        event.changeGratuityTotalPaymentCodeState(event.getPaymentCodeStateFor(transactionDetail.getPaymentMode()));

        if (!transactionDetail.isSibsTransactionDetail()) {
            checkIfCanAddAmount(entryDTO, transactionDetail.getWhenRegistered(), event);
        }

        return super.makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTO.getAmountToPay(),
                transactionDetail);

    }

    private AccountingTransaction internalProcessInstallment(User user, Account fromAccount, Account toAccount,
            EntryDTO entryDTO, GratuityEventWithPaymentPlan event, AccountingTransactionDetailDTO transactionDetail) {

        final EntryWithInstallmentDTO entryWithInstallmentDTO = (EntryWithInstallmentDTO) entryDTO;

        if (!transactionDetail.isSibsTransactionDetail()) {
            checkIfCanAddAmountForInstallment(entryWithInstallmentDTO, transactionDetail.getWhenRegistered(), event);
        }

        event.changeInstallmentPaymentCodeState(entryWithInstallmentDTO.getInstallment(),
                event.getPaymentCodeStateFor(transactionDetail.getPaymentMode()));

        return makeAccountingTransactionForInstallment(user, event, fromAccount, toAccount, getEntryType(),
                entryDTO.getAmountToPay(), (entryWithInstallmentDTO).getInstallment(), transactionDetail);

    }

    private void checkIfCanAddAmount(EntryDTO entryDTO, DateTime whenRegistered, Event event) {
        if (entryDTO.getAmountToPay().compareTo(event.calculateAmountToPay(whenRegistered)) < 0) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR.amount.to.pay.must.match.value",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    private void checkIfCanAddAmountForInstallment(EntryWithInstallmentDTO entryDTO, DateTime whenRegistered, Event event) {
        final Money installmentAmount =
                getPaymentPlan(event).calculateRemainingAmountFor(entryDTO.getInstallment(), event, whenRegistered,
                        getDiscountPercentage(event));
        if (entryDTO.getAmountToPay().compareTo(installmentAmount) < 0) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR.amount.to.pay.must.match.value",
                    event.getDescriptionForEntryType(getEntryType()));
        }

    }

    private PaymentPlan getPaymentPlan(Event event) {
        return ((GratuityEventWithPaymentPlan) event).getGratuityPaymentPlan();
    }

    @Override
    public DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
        return (DegreeCurricularPlanServiceAgreementTemplate) super.getServiceAgreementTemplate();
    }

    protected AccountingTransaction makeAccountingTransactionForInstallment(User responsibleUser, Event event, Account from,
            Account to, EntryType entryType, Money amount, Installment installment,
            AccountingTransactionDetailDTO transactionDetail) {
        return new InstallmentAccountingTransaction(responsibleUser, event, makeEntry(entryType, amount.negate(), from),
                makeEntry(entryType, amount, to), installment, makeAccountingTransactionDetail(transactionDetail));
    }

    @Override
    public Money getDefaultGratuityAmount(ExecutionYear executionYear) {
        return getServiceAgreementTemplate().getDefaultPaymentPlan(executionYear).calculateOriginalTotalAmount();
    }

    public Money getDefaultGratuityAmount() {
        return getDefaultGratuityAmount(ExecutionYear.readByDateTime(getCreationDate()));
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (getServiceAgreementTemplate().hasActivePostingRuleFor(EventType.STANDALONE_ENROLMENT_GRATUITY)) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                    "error.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR.standalone.cannot.delete"));
        }
    }

}
