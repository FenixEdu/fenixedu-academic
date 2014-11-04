/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.util.Money;

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
