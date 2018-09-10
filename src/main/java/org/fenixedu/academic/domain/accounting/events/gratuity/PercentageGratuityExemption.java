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

import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemption}
 */
@Deprecated
public class PercentageGratuityExemption extends PercentageGratuityExemption_Base {

    private PercentageGratuityExemption() { }

    @Override
    public void setPercentage(BigDecimal percentage) {
        super.setPercentage(percentage);
        final DateTime now = new DateTime();
        getGratuityEvent().forceChangeState(EventState.OPEN, now);
        getGratuityEvent().recalculateState(now);
    }

    @Override
    public BigDecimal calculateDiscountPercentage(Money amount) {
        return getPercentage();
    }

    @Override
    public boolean isPercentageExemption() {
        return true;
    }

    @Override
    public Money getExemptionAmount(Money money) {
        return money.multiply(getPercentage());
    }
}
