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
package org.fenixedu.academic.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class DFAGratuityByNumberOfEnrolmentsPR extends DFAGratuityByNumberOfEnrolmentsPR_Base {

    private static final int SCALE_FOR_INTERMEDIATE_CALCULATIONS = 8;

    protected DFAGratuityByNumberOfEnrolmentsPR() {
        super();
    }

    public DFAGratuityByNumberOfEnrolmentsPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                partialAcceptedPercentage);
    }

    @Override
    protected Money calculateDFAGratuityTotalAmountToPay(final Event event) {
        final GratuityEvent gratuityEvent = (GratuityEvent) event;
        final BigDecimal numberOfEnrolments = BigDecimal.valueOf(gratuityEvent.getEnrolmentsEctsForRegistration());
        final BigDecimal ectsCredits =
                BigDecimal.valueOf(gratuityEvent.getStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
                        .getDefaultEcts(gratuityEvent.getExecutionYear()));

        final Money result =
                getDfaTotalAmount().multiply(
                        numberOfEnrolments.divide(ectsCredits, SCALE_FOR_INTERMEDIATE_CALCULATIONS, RoundingMode.HALF_EVEN));
        return result.lessOrEqualThan(getDfaTotalAmount()) ? result : getDfaTotalAmount();
    }

    public DFAGratuityByNumberOfEnrolmentsPR edit(Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
        return edit(new DateTime(), dfaTotalAmount, partialAcceptedPercentage);
    }

    public DFAGratuityByNumberOfEnrolmentsPR edit(DateTime startDate, Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
        deactivate(startDate);

        return new DFAGratuityByNumberOfEnrolmentsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
                partialAcceptedPercentage);

    }

}
