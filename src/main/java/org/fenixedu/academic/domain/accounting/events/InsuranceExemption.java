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
package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.accounting.postingRules.InsurancePR;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemption}
 */
@Deprecated
public class InsuranceExemption extends InsuranceExemption_Base {

    private InsuranceExemption() {
        super();
    }

    @Override
    public boolean isInsuranceExemption() {
        return true;
    }

    @Override
    public boolean isForInsurance() {
        return true;
    }

    @Override
    public Money getExemptionAmount(Money money) {
        final Event event = getEvent();
        if (event instanceof InsuranceEvent) {
            return ((InsurancePR)event.getPostingRule()).getFixedAmount();
        } else if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent){
            return ((AdministrativeOfficeFeeAndInsuranceEvent)event).getInsuranceAmount();
        }
        throw new DomainException("no amount for type %s%n", event.getClass().getSimpleName());
    }
}
