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
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class InsurancePR extends InsurancePR_Base {

    protected InsurancePR() {
        super();
    }

    public InsurancePR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
        this();
        init(EntryType.INSURANCE_FEE, EventType.INSURANCE, startDate, endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        return getFixedAmount();
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        if (event instanceof InsuranceEvent) {
            InsuranceEvent insuranceEvent = (InsuranceEvent) event;
            if (insuranceEvent.hasInsuranceExemption()) {
                return Money.ZERO;
            }
        } else if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent) {
            final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                    (AdministrativeOfficeFeeAndInsuranceEvent) event;
            if (administrativeOfficeFeeAndInsuranceEvent.hasInsuranceExemption()) {
                return Money.ZERO;
            }
        }

        return amountToPay;
    }

    @Override
    public FixedAmountPR edit(final Money fixedAmount) {
        deactivate();
        return new InsurancePR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

}
