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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

abstract public class DFAGratuityPR extends DFAGratuityPR_Base implements IGratuityPR {

    protected DFAGratuityPR() {
        super();
    }

    public DFAGratuityPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                partialAcceptedPercentage);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);

        checkParameters(dfaTotalAmount, dfaPartialAcceptedPercentage);

        super.setDfaTotalAmount(dfaTotalAmount);
        super.setDfaPartialAcceptedPercentage(dfaPartialAcceptedPercentage);
    }

    private void checkParameters(Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage) {
        if (dfaTotalAmount == null) {
            throw new DomainException("error.accounting.postingRules.gratuity.DFAGratuityPR.dfaTotalAmount.cannot.be.null");
        }

        if (dfaPartialAcceptedPercentage == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.DFAGratuityPR.dfaPartialAcceptedPercentage.cannot.be.null");
        }
    }

    @Override
    public void setDfaTotalAmount(Money dfaTotalAmount) {
        throw new DomainException("error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaTotalAmount");
    }

    @Override
    public void setDfaPartialAcceptedPercentage(BigDecimal dfaPartialAcceptedPercentage) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaPartialAcceptedPercentage");
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        final Money result;
        if (((GratuityEvent) event).isCustomEnrolmentModel()) {
            result = calculateDFAGratuityTotalAmountToPay(event);
        } else {
            result = getDfaTotalAmount();
        }

        return result;
    }

    abstract protected Money calculateDFAGratuityTotalAmountToPay(Event event);

    @Override
    public Money getDefaultGratuityAmount(ExecutionYear executionYear) {
        return getDfaTotalAmount();
    }

}
