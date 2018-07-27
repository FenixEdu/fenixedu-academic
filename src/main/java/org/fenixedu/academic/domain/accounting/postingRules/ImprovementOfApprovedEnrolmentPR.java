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

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.EnrolmentEvaluationEvent;
import org.fenixedu.academic.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ImprovementOfApprovedEnrolmentPR extends ImprovementOfApprovedEnrolmentPR_Base implements IEnrolmentEvaluationPR {

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
    /***
     * will not be used for {@link EnrolmentEvaluationEvent}
     */
    protected Money doCalculationForAmountToPay(Event event, DateTime when) {
        return getFixedAmount().multiply(((ImprovementOfApprovedEnrolmentEvent) event).getImprovementEnrolmentEvaluationsSet().size());
    }

    private Optional<LocalDate> getDueDate(Event event) {
        Optional<EnrolmentEvaluation> enrolmentEvaluation;

        if (event instanceof EnrolmentEvaluationEvent) {
            enrolmentEvaluation = Optional.of(((EnrolmentEvaluationEvent) event).getEnrolmentEvaluation());
        } else {
            enrolmentEvaluation = ((ImprovementOfApprovedEnrolmentEvent) event).getImprovementEnrolmentEvaluationsSet().stream()
                    .findAny();
        }

        return enrolmentEvaluation.map(this::getEnrolmentPeriodInImprovementOfApprovedEnrolment).map
                (EnrolmentPeriodInImprovementOfApprovedEnrolment::getEndDateDateTime).map(DateTime::toLocalDate);
    }
    

    @Override
    public Map<LocalDate,Money> getDueDatePenaltyAmountMap(Event event, DateTime when) {
        Optional<LocalDate> dueDate = getDueDate(event);
        if (!dueDate.isPresent()) {
            return Collections.emptyMap();
        }
        return Collections.singletonMap(dueDate.get(), getFixedAmountPenalty().subtract(getFixedAmount()));
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

}
