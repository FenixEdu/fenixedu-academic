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
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.AcademicEventExemption;
import org.fenixedu.academic.domain.accounting.postingRules.FixedAmountPR;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class StandaloneIndividualCandidacyPR extends StandaloneIndividualCandidacyPR_Base {

    protected StandaloneIndividualCandidacyPR() {
        super();
    }

    public StandaloneIndividualCandidacyPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
        this();
        init(EntryType.STANDALONE_INDIVIDUAL_CANDIDACY_FEE, EventType.STANDALONE_INDIVIDUAL_CANDIDACY, startDate, endDate,
                serviceAgreementTemplate, fixedAmount);
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        if (event.getExemptionsSet().isEmpty()) {
            return amountToPay;
        }

        for (Exemption exemption : event.getExemptionsSet()) {
            if (exemption.isAcademicEventExemption()) {
                AcademicEventExemption academicEventExemption = (AcademicEventExemption) exemption;
                amountToPay = amountToPay.subtract(academicEventExemption.getValue());
            }
        }

        if (amountToPay.isNegative()) {
            return Money.ZERO;
        }

        return amountToPay;
    }

    @Override
    public FixedAmountPR edit(final Money fixedAmount) {

        deactivate();
        return new FixedAmountPR(getEntryType(), getEventType(), new DateTime().minus(1000), null, getServiceAgreementTemplate(),
                fixedAmount);
    }

}
