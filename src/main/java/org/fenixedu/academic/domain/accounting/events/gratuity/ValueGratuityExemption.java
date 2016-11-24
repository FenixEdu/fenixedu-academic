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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class ValueGratuityExemption extends ValueGratuityExemption_Base {

    public ValueGratuityExemption(final Person responsible, final GratuityEvent gratuityEvent,
            final GratuityExemptionJustificationType gratuityExemptionType, final String reason, final YearMonthDay dispatchDate,
            final Money value) {
        super();
        init(responsible, gratuityEvent, gratuityExemptionType, reason, dispatchDate, value);
    }

    public ValueGratuityExemption(final GratuityEvent gratuityEvent,
            final GratuityExemptionJustificationType gratuityExemptionType, final String reason, final YearMonthDay dispatchDate,
            final Money value) {
        this(null, gratuityEvent, gratuityExemptionType, reason, dispatchDate, value);
    }

    protected void init(Person responsible, GratuityEvent gratuityEvent, GratuityExemptionJustificationType exemptionType,
            String reason, YearMonthDay dispatchDate, Money value) {

        checkParameters(value);
        super.setValue(value);

        super.init(responsible, gratuityEvent, exemptionType, reason, dispatchDate);
    }

    private void checkParameters(Money value) {
        if (value == null) {
            throw new DomainException("error.accounting.events.gratuity.ValueGratuityExemption.value.cannot.be.null");
        }
    }

    @Override
    public void setValue(Money value) {
        super.setValue(value);
        final DateTime now = new DateTime();
        getGratuityEvent().forceChangeState(EventState.OPEN, now);
        getGratuityEvent().recalculateState(now);
    }

    @Override
    public BigDecimal calculateDiscountPercentage(Money amount) {
        final BigDecimal amountToDiscount = new BigDecimal(getValue().toString());
        return amountToDiscount.divide(amount.getAmount(), 10, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean isValueExemption() {
        return true;
    }

}
