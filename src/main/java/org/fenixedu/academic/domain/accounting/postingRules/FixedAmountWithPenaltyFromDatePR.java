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

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class FixedAmountWithPenaltyFromDatePR extends FixedAmountWithPenaltyFromDatePR_Base {

    protected FixedAmountWithPenaltyFromDatePR() {
        super();
    }

    public FixedAmountWithPenaltyFromDatePR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount, Money fixedAmountPenalty,
            YearMonthDay whenToApplyFixedAmountPenalty) {
        init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount, fixedAmountPenalty,
                whenToApplyFixedAmountPenalty);

    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount, Money fixedAmountPenalty,
            YearMonthDay whenToApplyFixedAmountPenalty) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);

        checkParameters(whenToApplyFixedAmountPenalty);

        super.setWhenToApplyFixedAmountPenalty(whenToApplyFixedAmountPenalty);
    }

    private void checkParameters(YearMonthDay whenToApplyFixedAmountPenalty) {
        if (whenToApplyFixedAmountPenalty == null) {
            throw new DomainException(
                    "error.accounting.postingRules.FixedAmountWithPenaltyFromDatePR.whenToApplyFixedAmountPenalty.cannot.be.null");
        }
    }

    @Override
    public void setWhenToApplyFixedAmountPenalty(YearMonthDay whenToApplyFixedAmountPenalty) {
        throw new DomainException(
                "error.accounting.postingRules.FixedAmountWithPenaltyFromDatePR.cannot.modify.whenToApplyFixedAmountPenalty");
    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
        return when.toYearMonthDay().isAfter(getWhenToApplyFixedAmountPenalty());
    }

    public FixedAmountWithPenaltyFromDatePR edit(Money fixedAmount, Money penaltyAmount,
            YearMonthDay whenToApplyFixedAmountPenalty) {

        deactivate();

        return new FixedAmountWithPenaltyFromDatePR(getEntryType(), getEventType(), new DateTime().minus(1000), null,
                getServiceAgreementTemplate(), fixedAmount, penaltyAmount, whenToApplyFixedAmountPenalty);

    }

}
