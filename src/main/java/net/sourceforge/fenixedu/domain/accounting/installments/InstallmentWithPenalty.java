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
package net.sourceforge.fenixedu.domain.accounting.installments;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class InstallmentWithPenalty extends InstallmentWithPenalty_Base {

    protected InstallmentWithPenalty() {
        super();
    }

    protected void init(final PaymentPlan paymentCondition, final Money amount, YearMonthDay startDate, YearMonthDay endDate,
            final BigDecimal penaltyPercentage) {

        super.init(paymentCondition, amount, startDate, endDate);
        checkParameters(penaltyPercentage);

        super.setPenaltyPercentage(penaltyPercentage);

    }

    private void checkParameters(BigDecimal penaltyPercentage) {
        if (penaltyPercentage == null) {
            throw new DomainException("error.accounting.installments.InstallmentWithPenalty.penaltyPercentage.cannot.be.null");
        }
    }

    @Override
    public Money calculateAmount(Event event, DateTime when, BigDecimal discountPercentage, boolean applyPenalty) {
        return super.calculateAmount(event, when, discountPercentage, applyPenalty).add(
                (applyPenalty ? calculatePenaltyAmount(event, when, discountPercentage) : Money.ZERO));
    }

    abstract protected Money calculatePenaltyAmount(Event event, DateTime when, BigDecimal discountPercentage);

    @Deprecated
    public boolean hasPenaltyPercentage() {
        return getPenaltyPercentage() != null;
    }

}
