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
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class InsurancePR extends InsurancePR_Base implements IAdministrativeOfficeFeeAndInsurancePR {

    protected InsurancePR() {
        super();
    }

    public InsurancePR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, Money
            fixedAmount, int numberOfDaysToCalculateDueDate) {
        this();
        init(EntryType.INSURANCE_FEE, EventType.INSURANCE, startDate, endDate, serviceAgreementTemplate, fixedAmount);
        setNumberOfDaysToCalculateDueDate(numberOfDaysToCalculateDueDate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        return getFixedAmount();
    }

    public FixedAmountPR edit(final Money fixedAmount, Integer numberOfDaysToCalculateDueDate) {
        deactivate();
        return new InsurancePR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount, numberOfDaysToCalculateDueDate);
    }

    @Override
    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate) {
        return endDate.toYearMonthDay();
    }

    @Override
    public Money getInsuranceAmount(DateTime startDate, DateTime endDate) {
        return getFixedAmount();
    }

    @Override
    public Money getAdministrativeOfficeFeeAmount(Event event, DateTime startDate, DateTime endDate) {
        return Money.ZERO;
    }

    @Override
    public Money getAdministrativeOfficeFeePenaltyAmount(Event event, DateTime startDate, DateTime endDate) {
        return Money.ZERO;
    }
}
