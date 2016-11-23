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

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class SpecializationDegreeGratuityByAmountPerEctsPR extends SpecializationDegreeGratuityByAmountPerEctsPR_Base {

    protected SpecializationDegreeGratuityByAmountPerEctsPR() {
        super();
    }

    public SpecializationDegreeGratuityByAmountPerEctsPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal partialAcceptedPercentage, Money specializationDegreeAmountPerEctsCredit) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate,
                specializationDegreeTotalAmount, partialAcceptedPercentage, specializationDegreeAmountPerEctsCredit);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal specializationDegreePartialAcceptedPercentage, Money specializationDegreeAmountPerEctsCredit) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, specializationDegreeTotalAmount,
                specializationDegreePartialAcceptedPercentage);

        checkParameters(specializationDegreeAmountPerEctsCredit);
        super.setSpecializationDegreeAmountPerEctsCredit(specializationDegreeAmountPerEctsCredit);
    }

    private void checkParameters(Money specializationDegreeAmountPerEctsCredit) {
        if (specializationDegreeAmountPerEctsCredit == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR.specializationDegreeAmountPerEctsCredit.cannot.be.null");
        }
    }

    @Override
    public void setSpecializationDegreeAmountPerEctsCredit(Money specializationDegreeAmountPerEctsCredit) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR.cannot.modify.specializationDegreeAmountPerEctsCredit");
    }

    @Override
    protected Money calculateSpecializationDegreeGratuityTotalAmountToPay(final Event event) {
        final Money result;
        final double enrolmentsEctsForRegistration = ((GratuityEvent) event).getEnrolmentsEctsForRegistration();
        result = getSpecializationDegreeAmountPerEctsCredit().multiply(new BigDecimal(enrolmentsEctsForRegistration));

        return result;
    }

    public SpecializationDegreeGratuityByAmountPerEctsPR edit(Money specializationDegreeTotalAmount,
            Money specializationDegreeAmountPerEctsCredit, BigDecimal partialAcceptedPercentage) {
        return edit(new DateTime(), specializationDegreeTotalAmount, specializationDegreeAmountPerEctsCredit,
                partialAcceptedPercentage);
    }

    public SpecializationDegreeGratuityByAmountPerEctsPR edit(DateTime startDate, Money specializationDegreeTotalAmount,
            Money specializationDegreeAmountPerEctsCredit, BigDecimal partialAcceptedPercentage) {
        deactivate(startDate);

        return new SpecializationDegreeGratuityByAmountPerEctsPR(startDate, null, getServiceAgreementTemplate(),
                specializationDegreeTotalAmount, partialAcceptedPercentage, specializationDegreeAmountPerEctsCredit);
    }

}
