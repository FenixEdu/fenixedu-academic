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
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.AcademicEventExemption;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class Over23IndividualCandidacyPR extends Over23IndividualCandidacyPR_Base {

    public Over23IndividualCandidacyPR() {
        super();
    }

    public Over23IndividualCandidacyPR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
        this();
        init(EntryType.OVER23_INDIVIDUAL_CANDIDACY_FEE, EventType.OVER23_INDIVIDUAL_CANDIDACY, startDate, endDate,
                serviceAgreementTemplate, fixedAmount);
    }

    @Override
    public Over23IndividualCandidacyPR edit(final Money fixedAmount) {
        deactivate();
        return new Over23IndividualCandidacyPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    public PaymentCodeType calculatePaymentCodeTypeFromEvent(Event event, DateTime when, boolean applyDiscount) {
        return PaymentCodeType.OVER_23_INDIVIDUAL_CANDIDACY_PROCESS;
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

}
