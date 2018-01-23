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
import org.fenixedu.academic.domain.accounting.events.administrativeOfficeFee.IAdministrativeOfficeFeeEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeePR extends AdministrativeOfficeFeePR_Base implements IAdministrativeOfficeFeeAndInsurancePR {

    protected AdministrativeOfficeFeePR() {
        super();
    }

    public AdministrativeOfficeFeePR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money fixedAmount, Money fixedAmountPenalty, YearMonthDay whenToApplyFixedAmountPenalty) {
        this();
        init(EntryType.ADMINISTRATIVE_OFFICE_FEE, EventType.ADMINISTRATIVE_OFFICE_FEE, startDate, endDate,
                serviceAgreementTemplate, fixedAmount, fixedAmountPenalty, whenToApplyFixedAmountPenalty);

    }

    @Override
    protected Optional<LocalDate> getPenaltyDueDate(Event event) {
        final IAdministrativeOfficeFeeEvent administrativeOfficeFeeEvent = (IAdministrativeOfficeFeeEvent) event;

        final YearMonthDay paymentEndDate =
            administrativeOfficeFeeEvent.getPaymentEndDate() != null ? administrativeOfficeFeeEvent
                                                                                       .getPaymentEndDate() : getWhenToApplyFixedAmountPenalty();

        return Optional.of(paymentEndDate.toLocalDate());
    }

    public AdministrativeOfficeFeePR edit(DateTime startDate, Money fixedAmount, Money penaltyAmount,
            YearMonthDay whenToApplyFixedAmountPenalty) {

        if (!startDate.isAfter(getStartDate())) {
            throw new DomainException(
                    "error.AdministrativeOfficeFeePR.startDate.is.before.then.start.date.of.previous.posting.rule");
        }

        deactivate(startDate);

        return new AdministrativeOfficeFeePR(startDate.minus(1000), null, getServiceAgreementTemplate(), fixedAmount,
                penaltyAmount, whenToApplyFixedAmountPenalty);
    }

    @Override
    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate) {
        return getWhenToApplyFixedAmountPenalty();
    }

    @Override
    public Money getInsuranceAmount(DateTime startDate, DateTime endDate) {
        return Money.ZERO;
    }

    @Override
    public Money getAdministrativeOfficeFeeAmount(Event event, DateTime startDate, DateTime endDate) {
        return getFixedAmount();
    }

    @Override
    public Money getAdministrativeOfficeFeePenaltyAmount(Event event, DateTime startDate, DateTime endDate) {
        return getFixedAmountPenalty();
    }
}
