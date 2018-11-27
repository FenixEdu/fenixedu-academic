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
package org.fenixedu.academic.domain.accounting.events.candidacy;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemption}
 */
@Deprecated
public class SecondCycleIndividualCandidacyExemption extends SecondCycleIndividualCandidacyExemption_Base {

    private SecondCycleIndividualCandidacyExemption() {
        super();
    }

    @Override
    public SecondCycleIndividualCandidacyEvent getEvent() {
        return (SecondCycleIndividualCandidacyEvent) super.getEvent();
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        super.delete();
    }

    private void checkRulesToDelete() {
        if (getEvent().getIndividualCandidacy().isAccepted()) {
            throw new DomainException(
                    "error.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption.cannot.delete.candidacy.is.accepted");
        }
    }

    @Override
    public boolean isSecondCycleIndividualCandidacyExemption() {
        return true;
    }

    @Override
    public Money getExemptionAmount(Money money) {
        return money;
    }
}
