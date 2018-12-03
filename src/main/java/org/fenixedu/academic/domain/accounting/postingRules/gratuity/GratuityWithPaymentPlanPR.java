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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
    protected Money doCalculationForAmountToPay(Event event) {
        throw new DomainException("not to be used anymore");
    }

    public BigDecimal getDiscountPercentage(final Event event) {
        PaymentPlan paymentPlan = getPaymentPlan(event);
        if (paymentPlan == null) {
            throw new DomainException("error.event.not.associated.paymentPlan", event.getClass().getName());
        }
        return ((GratuityEventWithPaymentPlan) event).calculateDiscountPercentage(paymentPlan.calculateBaseAmount(event));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan = (GratuityEventWithPaymentPlan) event;

        if (entryDTOs.size() > 1) {
            final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
            for (final EntryDTO each : entryDTOs) {
                if (each instanceof EntryWithInstallmentDTO) {
                    result.add(internalProcessInstallment(user, fromAccount, toAccount, each, gratuityEventWithPaymentPlan,
                                                          transactionDetail));
                }
                else {
                    result.add(makeAccountingTransaction(user, each.getEvent(), fromAccount, toAccount, each.getEntryType(), each.getAmountToPay(), transactionDetail));
                }

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

        return super.makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTO.getAmountToPay(),
                transactionDetail);

    }

    private AccountingTransaction internalProcessInstallment(User user, Account fromAccount, Account toAccount,
            EntryDTO entryDTO, GratuityEventWithPaymentPlan event, AccountingTransactionDetailDTO transactionDetail) {

        final EntryWithInstallmentDTO entryWithInstallmentDTO = (EntryWithInstallmentDTO) entryDTO;

        return makeAccountingTransactionForInstallment(user, event, fromAccount, toAccount, getEntryType(),
                entryDTO.getAmountToPay(), (entryWithInstallmentDTO).getInstallment(), transactionDetail);

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
                makeEntry(entryType, amount, to), installment, makeAccountingTransactionDetail(event, transactionDetail));
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
