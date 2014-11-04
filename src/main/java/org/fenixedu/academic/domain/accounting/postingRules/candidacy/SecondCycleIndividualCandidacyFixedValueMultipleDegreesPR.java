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
package org.fenixedu.academic.domain.accounting.postingRules.candidacy;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR extends
        SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR_Base {

    public SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR() {
        super();
    }

    public SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
        this();
        init(EntryType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_FEE, EventType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY, startDate, endDate,
                serviceAgreementTemplate, fixedAmount);
    }

    @Override
    public SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR edit(final Money fixedAmount) {
        deactivate();
        return new SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR(new DateTime().minus(1000), null,
                getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        return getFixedAmount();
    }

}
