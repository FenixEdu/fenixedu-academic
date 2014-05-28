/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

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

    @Deprecated
    public boolean hasFixedAmountPenalty() {
        return getFixedAmountPenalty() != null;
    }

}
