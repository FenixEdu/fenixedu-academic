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

import org.fenixedu.academic.util.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemption}
 */
@Deprecated
public class ExternalScholarshipGratuityExemption extends ExternalScholarshipGratuityExemption_Base {

    private ExternalScholarshipGratuityExemption() { }

    public BigDecimal calculateDiscountPercentage(Money amount) {
        final BigDecimal amountToDiscount = new BigDecimal(getValue().toString());
        return amountToDiscount.divide(amount.getAmount(), 10, RoundingMode.HALF_EVEN);
    }

    public void doDelete() {
        setExternalScholarshipGratuityContributionEvent(null);
        setParty(null);
        GratuityContributionFile document = getDocument();
        setDocument(null);
        document.delete();
        super.delete();
    }

    @Override
    public Money getExemptionAmount(Money money) {
        return getValue();
    }
}
