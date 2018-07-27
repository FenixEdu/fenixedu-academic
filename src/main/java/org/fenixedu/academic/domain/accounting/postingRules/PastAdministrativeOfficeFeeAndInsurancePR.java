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
import org.fenixedu.academic.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class PastAdministrativeOfficeFeeAndInsurancePR extends PastAdministrativeOfficeFeeAndInsurancePR_Base implements IAdministrativeOfficeFeeAndInsurancePR {

    protected PastAdministrativeOfficeFeeAndInsurancePR() {
        super();
    }

    public PastAdministrativeOfficeFeeAndInsurancePR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        init(EntryType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, startDate, endDate,
                serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when) {
        final PastAdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                (PastAdministrativeOfficeFeeAndInsuranceEvent) event;
        return administrativeOfficeFeeAndInsuranceEvent.getPastAdministrativeOfficeFeeAndInsuranceAmount();
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate) {
        return endDate.toYearMonthDay();
    }

    @Override
    public Money getInsuranceAmount(DateTime startDate, DateTime endDate) {
        return Money.ZERO;
    }

    @Override
    public Money getAdministrativeOfficeFeeAmount(Event event, DateTime startDate, DateTime endDate) {
        return ((PastAdministrativeOfficeFeeAndInsuranceEvent) event).getPastAdministrativeOfficeFeeAndInsuranceAmount();
    }

    @Override
    public Money getAdministrativeOfficeFeePenaltyAmount(Event event, DateTime startDate, DateTime endDate) {
        return Money.ZERO;
    }
}
