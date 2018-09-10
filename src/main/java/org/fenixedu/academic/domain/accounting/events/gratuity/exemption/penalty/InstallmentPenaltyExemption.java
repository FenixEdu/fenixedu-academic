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
package org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Set;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.FixedAmountInterestExemption} or
 *     {@link org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.FixedAmountFineExemption}
 */
@Deprecated
public class InstallmentPenaltyExemption extends InstallmentPenaltyExemption_Base {

    private InstallmentPenaltyExemption() {
        super();
    }

    @Override
    public void delete() {
        super.setInstallment(null);
        super.delete();
    }

    @Override
    public boolean isForInterest() {
        return true;
    }

    @Override
    public boolean isForFine() {
        return false;
    }

    @Override
    public Set<LocalDate> getDueDates(DateTime when) {
        return Collections.singleton(getInstallment().getEndDate(getEvent()));
    }
}
