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
package org.fenixedu.academic.domain.accounting.postingRules;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import org.fenixedu.academic.domain.accounting.events.ImprovementOfApprovedEnrolmentPenaltyExemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class ImprovementOfApprovedEnrolmentPR extends ImprovementOfApprovedEnrolmentPR_Base {

    protected ImprovementOfApprovedEnrolmentPR() {
        super();
    }

    public ImprovementOfApprovedEnrolmentPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount, Money fixedAmountPenalty) {
        super.init(EntryType.IMPROVEMENT_OF_APPROVED_ENROLMENT_FEE, EventType.IMPROVEMENT_OF_APPROVED_ENROLMENT, startDate,
                endDate, serviceAgreementTemplate);
        checkParameters(fixedAmount, fixedAmountPenalty);
        super.setFixedAmount(fixedAmount);
        super.setFixedAmountPenalty(fixedAmountPenalty);
    }

    private void checkParameters(Money fixedAmount, Money fixedAmountPenalty) {
        if (fixedAmount == null) {
            throw new DomainException("error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.fixedAmount.cannot.be.null");
        }
        if (fixedAmountPenalty == null) {
            throw new DomainException(
                    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.fixedAmountPenalty.cannot.be.null");
        }
    }

    @Override
    public void setFixedAmountPenalty(Money fixedAmountPenalty) {
        throw new DomainException(
                "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.cannot.modify.fixedAmountPenalty");
    }

    public ImprovementOfApprovedEnrolmentPR edit(final Money fixedAmount, final Money fixedAmountPenalty) {
        deactivate();
        return new ImprovementOfApprovedEnrolmentPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount,
                fixedAmountPenalty);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent =
                (ImprovementOfApprovedEnrolmentEvent) event;
        final boolean hasPenalty = hasPenalty(event, when);

        Money result = Money.ZERO;
        for (int i = 0; i < improvementOfApprovedEnrolmentEvent.getImprovementEnrolmentEvaluationsSet().size(); i++) {
            result = result.add(hasPenalty ? getFixedAmountPenalty() : getFixedAmount());
        }

        return result;
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        return amountToPay;
    }

    private boolean hasPenalty(final Event event, final DateTime when) {
        if (event.hasAnyPenaltyExemptionsFor(ImprovementOfApprovedEnrolmentPenaltyExemption.class)) {
            return false;
        } else {
            final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent =
                    (ImprovementOfApprovedEnrolmentEvent) event;
            final Set<EnrolmentEvaluation> enrolmentEvaluations =
                    improvementOfApprovedEnrolmentEvent.getImprovementEnrolmentEvaluationsSet();
            if (enrolmentEvaluations.isEmpty()) {
                return false;
            }
            return !getEnrolmentPeriodInImprovementOfApprovedEnrolment(enrolmentEvaluations.iterator().next()).containsDate(when);
        }
    }

    private EnrolmentPeriodInImprovementOfApprovedEnrolment getEnrolmentPeriodInImprovementOfApprovedEnrolment(
            EnrolmentEvaluation enrolmentEvaluation) {
        final DegreeCurricularPlan degreeCurricularPlan = enrolmentEvaluation.getDegreeCurricularPlan();
        final EnrolmentPeriod enrolmentPeriodInImprovementOfApprovedEnrolment =
                enrolmentEvaluation.getExecutionPeriod().getEnrolmentPeriod(
                        EnrolmentPeriodInImprovementOfApprovedEnrolment.class, degreeCurricularPlan);
        if (enrolmentPeriodInImprovementOfApprovedEnrolment == null) {
            throw new DomainException(
                    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.enrolmentPeriodInImprovementOfApprovedEnrolment.must.not.be.null");
        }
        return (EnrolmentPeriodInImprovementOfApprovedEnrolment) enrolmentPeriodInImprovementOfApprovedEnrolment;
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        final Money totalAmountToPay = calculateTotalAmountToPay(event, when);
        return Collections.singletonList(new EntryDTO(getEntryType(), event, totalAmountToPay, Money.ZERO, totalAmountToPay,
                event.getDescriptionForEntryType(getEntryType()), totalAmountToPay));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
        if (entryDTOs.size() != 1) {
            throw new DomainException(
                    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.iterator().next();
        checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(),
                entryDTO.getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(final Money amountToPay, final Event event, final DateTime when) {
        if (amountToPay.compareTo(calculateTotalAmountToPay(event, when)) < 0) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.amount.being.payed.must.match.amount.to.pay",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

}
