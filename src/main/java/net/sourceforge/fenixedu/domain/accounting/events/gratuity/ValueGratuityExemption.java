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
package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

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
        check(this, RolePredicates.MANAGER_PREDICATE);
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

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

}
