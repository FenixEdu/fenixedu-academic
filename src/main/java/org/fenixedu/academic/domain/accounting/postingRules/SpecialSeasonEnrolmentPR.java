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

import java.util.Optional;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.SpecialSeasonEnrolmentEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class SpecialSeasonEnrolmentPR extends SpecialSeasonEnrolmentPR_Base implements IEnrolmentEvaluationPR {

    protected SpecialSeasonEnrolmentPR() {
        super();
    }

    public SpecialSeasonEnrolmentPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money fixedAmount) {
        super.init(EntryType.SPECIAL_SEASON_ENROLMENT_FEE, EventType.SPECIAL_SEASON_ENROLMENT, startDate, endDate,
                serviceAgreementTemplate);
        checkParameters(fixedAmount);
        super.setFixedAmount(fixedAmount);
    }

    private void checkParameters(Money fixedAmount) {
        if (fixedAmount == null) {
            throw new DomainException("error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.fixedAmount.cannot.be.null");
        }
    }

    public SpecialSeasonEnrolmentPR edit(final Money fixedAmount) {
        deactivate();
        return new SpecialSeasonEnrolmentPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        final SpecialSeasonEnrolmentEvent specialSeasonEnrolmentEvent = (SpecialSeasonEnrolmentEvent) event;
        final int numberOfEnrolments = specialSeasonEnrolmentEvent.getSpecialSeasonEnrolmentEvaluationsSet().size();
        return getFixedAmount().multiply(numberOfEnrolments);
    }

    @Override
    public Money getFixedAmountPenalty() {
        return Money.ZERO;
    }

    @Override
    public Optional<LocalDate> getDueDate(final Event event) {
        return Optional.empty();
    }
}
