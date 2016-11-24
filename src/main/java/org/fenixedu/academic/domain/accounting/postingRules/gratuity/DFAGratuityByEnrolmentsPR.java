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
import org.fenixedu.academic.domain.accounting.events.gratuity.DfaGratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class DFAGratuityByEnrolmentsPR extends DFAGratuityByEnrolmentsPR_Base {
    private static final int SCALE_FOR_INTERMEDIATE_CALCULATIONS = 8;

    protected DFAGratuityByEnrolmentsPR() {
        super();
    }

    public DFAGratuityByEnrolmentsPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money dfaTotalAmount, BigDecimal partialAcceptedPercentage, Money dfaAmountPerEnrolment) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEnrolment);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage,
            Money dfaAmountPerEnrolment) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                dfaPartialAcceptedPercentage);

        checkParameters(dfaAmountPerEnrolment);
        super.setDfaAmountPerEnrolment(dfaAmountPerEnrolment);
    }

    private void checkParameters(Money dfaAmountPerEnrolment) {
        if (dfaAmountPerEnrolment == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.dfaAmountPerEnrolment.cannot.be.null");
        }
    }

    @Override
    protected Money calculateDFAGratuityTotalAmountToPay(Event event) {
        DfaGratuityEvent dfaGratuity = (DfaGratuityEvent) event;

        return getDfaAmountPerEnrolment().multiply(
                dfaGratuity.getRegistration().getEnrolments(dfaGratuity.getExecutionYear()).size());
    }

    @Override
    public void setDfaAmountPerEnrolment(Money dfaAmountPerEnrolment) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.DFAGratuityByEnrolmentsPR.cannot.modify.dfaAmountPerEnrolment");
    }

    public DFAGratuityByEnrolmentsPR edit(Money dfaTotalAmount, Money dfaAmountPerEnrolment, BigDecimal partialAcceptedPercentage) {
        return edit(new DateTime(), dfaTotalAmount, dfaAmountPerEnrolment, partialAcceptedPercentage);
    }

    public DFAGratuityByEnrolmentsPR edit(DateTime startDate, Money dfaTotalAmount, Money dfaAmountPerEnrolment,
            BigDecimal partialAcceptedPercentage) {
        deactivate(startDate);

        return new DFAGratuityByEnrolmentsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEnrolment);
    }

}
