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

public class DFAGratuityByAmountPerEctsPR extends DFAGratuityByAmountPerEctsPR_Base {

    protected DFAGratuityByAmountPerEctsPR() {
        super();
    }

    public DFAGratuityByAmountPerEctsPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money dfaTotalAmount, BigDecimal partialAcceptedPercentage, Money dfaAmountPerEctsCredit) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEctsCredit);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage,
            Money dfaAmountPerEctsCredit) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                dfaPartialAcceptedPercentage);

        checkParameters(dfaAmountPerEctsCredit);
        super.setDfaAmountPerEctsCredit(dfaAmountPerEctsCredit);
    }

    private void checkParameters(Money dfaAmountPerEctsCredit) {
        if (dfaAmountPerEctsCredit == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.dfaAmountPerEctsCredit.cannot.be.null");
        }
    }

    @Override
    public void setDfaAmountPerEctsCredit(Money dfaAmountPerEctsCredit) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.cannot.modify.dfaAmountPerEctsCredit");
    }

    @Override
    protected Money calculateDFAGratuityTotalAmountToPay(final Event event) {
        final Money result;
        final double enrolmentsEctsForRegistration = ((GratuityEvent) event).getEnrolmentsEctsForRegistration();
        result = getDfaAmountPerEctsCredit().multiply(new BigDecimal(enrolmentsEctsForRegistration));
        return result;
    }

    public DFAGratuityByAmountPerEctsPR edit(Money dfaTotalAmount, Money dfaAmountPerEctsCredit,
            BigDecimal partialAcceptedPercentage) {
        return edit(new DateTime(), dfaTotalAmount, dfaAmountPerEctsCredit, partialAcceptedPercentage);
    }

    public DFAGratuityByAmountPerEctsPR edit(DateTime startDate, Money dfaTotalAmount, Money dfaAmountPerEctsCredit,
            BigDecimal partialAcceptedPercentage) {
        deactivate(startDate);

        return new DFAGratuityByAmountPerEctsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEctsCredit);

    }

}
