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

import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.util.Money;

import java.math.BigDecimal;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemption}
 */
@Deprecated
public abstract class GratuityExemption extends GratuityExemption_Base {

    protected GratuityExemption() {
        super();
    }

    public GratuityEvent getGratuityEvent() {
        return (GratuityEvent) getEvent();
    }

    public EventExemptionJustificationType getJustificationType() {
        return getExemptionJustification().getJustificationType();
    }

    @Override
    public GratuityExemptionJustification getExemptionJustification() {
        return (GratuityExemptionJustification) super.getExemptionJustification();
    }

    public boolean isValueExemption() {
        return false;
    }

    public boolean isPercentageExemption() {
        return false;
    }

    abstract public BigDecimal calculateDiscountPercentage(Money amount);

    @Override
    public boolean isGratuityExemption() {
        return true;
    }

}
