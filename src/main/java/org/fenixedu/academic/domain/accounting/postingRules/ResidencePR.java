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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;


public class ResidencePR extends ResidencePR_Base {

    public ResidencePR(final DateTime startDate, final DateTime endDate, final ServiceAgreementTemplate serviceAgreementTemplate,
            Money penaltyPerDay) {
        super.init(EntryType.RESIDENCE_FEE, EventType.RESIDENCE_PAYMENT, startDate, endDate, serviceAgreementTemplate);
        setPenaltyPerDay(penaltyPerDay);
    }

    @Override
    public Map<LocalDate, Money> getDueDatePenaltyAmountMap(Event event, DateTime when) {
        ResidenceEvent residenceEvent = (ResidenceEvent) event;
        if (residenceEvent.getPaymentLimitDate().isAfter(when)) {
            return Collections.emptyMap();
        }

        final BigDecimal daysBetween = BigDecimal.valueOf(Days.daysBetween(residenceEvent.getPaymentLimitDate().toLocalDate(),
                when.toLocalDate()).getDays());
        final Money amount = getPenaltyPerDay().multiply(daysBetween);
        return Collections.singletonMap(residenceEvent.getPaymentLimitDate().toLocalDate(), amount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when) {
        return ((ResidenceEvent) event).getRoomValue();
    }

}
