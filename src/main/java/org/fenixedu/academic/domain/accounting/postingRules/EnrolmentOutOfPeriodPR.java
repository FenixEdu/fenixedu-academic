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

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.EnrolmentOutOfPeriodEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class EnrolmentOutOfPeriodPR extends EnrolmentOutOfPeriodPR_Base {

    protected EnrolmentOutOfPeriodPR() {
        super();
    }

    public EnrolmentOutOfPeriodPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money baseAmount, Money amountPerDay, Money maxAmount) {
        this();
        init(EntryType.ENROLMENT_OUT_OF_PERIOD_PENALTY, EventType.ENROLMENT_OUT_OF_PERIOD, startDate, endDate,
                serviceAgreementTemplate, baseAmount, amountPerDay, maxAmount);
    }

    private void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerDay, Money maxAmount) {

        checkParameters(baseAmount, amountPerDay, maxAmount);
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
        super.setBaseAmount(baseAmount);
        super.setAmountPerDay(amountPerDay);
        super.setMaxAmount(maxAmount);

    }

    private void checkParameters(Money baseAmount, Money amountPerDay, Money maxAmount) {

        if (baseAmount == null || baseAmount.isZero()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.postingRules.EnrolmentOutOfPeriodPR.baseAmount.cannot.be.null.and.must.be.greater.than.zero");
        }

        if (amountPerDay == null || amountPerDay.isZero()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.postingRules.EnrolmentOutOfPeriodPR.amountPerDay.cannot.be.null.and.must.be.greater.than.zero");
        }

        if (maxAmount == null || maxAmount.isZero()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.postingRules.EnrolmentOutOfPeriodPR.maxAmount.cannot.be.null.and.must.be.greater.than.zero");
        }

    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        final Money result = getBaseAmount().add(getAmountPerDay().multiply(new BigDecimal(calculatNumberOfDays(event))));
        return result.greaterThan(getMaxAmount()) ? getMaxAmount() : result;
    }

    private Integer calculatNumberOfDays(Event event) {
        final EnrolmentOutOfPeriodEvent enrolmentOutOfPeriodEvent = ((EnrolmentOutOfPeriodEvent) event);
        final Integer result = enrolmentOutOfPeriodEvent.getNumberOfDelayDays() - 1;

        return result < 0 ? 0 : result;
    }

    public EnrolmentOutOfPeriodPR edit(final Money baseAmount, final Money amountPerDay, final Money maxAmount) {

        deactivate();
        return new EnrolmentOutOfPeriodPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), baseAmount,
                amountPerDay, maxAmount);
    }

}
