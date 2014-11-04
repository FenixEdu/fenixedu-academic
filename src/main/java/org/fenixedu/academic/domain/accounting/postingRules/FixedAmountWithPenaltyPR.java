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
package org.fenixedu.academic.domain.accounting.postingRules;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public abstract class FixedAmountWithPenaltyPR extends FixedAmountWithPenaltyPR_Base {

    protected FixedAmountWithPenaltyPR() {
        super();
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount, Money fixedAmountPenalty) {
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
        checkParameters(fixedAmountPenalty);
        super.setFixedAmountPenalty(fixedAmountPenalty);
    }

    private void checkParameters(Money fixedAmountPenalty) {
        if (fixedAmountPenalty == null) {
            throw new DomainException("error.accounting.postingRules.FixedAmountWithPenaltyPR.fixedAmountPenalty.cannot.be.null");
        }
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        return super.doCalculationForAmountToPay(event, when, applyDiscount).add(
                hasPenalty(event, when) ? getFixedAmountPenalty() : Money.ZERO);
    }

    @Override
    public void setFixedAmountPenalty(Money fixedAmountPenalty) {
        throw new DomainException("error.accounting.postingRules.FixedAmountWithPenaltyPR.cannot.modify.fixedAmountPenalty");
    }

    abstract protected boolean hasPenalty(Event event, DateTime when);

}
