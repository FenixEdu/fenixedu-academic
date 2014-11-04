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
package org.fenixedu.academic.domain.organizationalStructure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class PersonFunctionShared extends PersonFunctionShared_Base {
    private static final BigDecimal MAX_PERCENTAGE = new BigDecimal(100);

    public PersonFunctionShared(Party parentParty, Party childParty, SharedFunction sharedfunction,
            ExecutionInterval executionInterval, BigDecimal percentage) {
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(sharedfunction);
        setOccupationInterval(executionInterval);
        setPercentage(percentage);
    }

    public SharedFunction getSharedFunction() {
        return (SharedFunction) getAccountabilityType();
    }

    @Override
    public boolean isPersonFunctionShared() {
        return true;
    }

    @Override
    public void setPercentage(BigDecimal percentage) {
        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) < 0) {
            percentage = BigDecimal.ZERO;
        }
        if (percentage.compareTo(MAX_PERCENTAGE) > 0) {
            throw new DomainException("label.percentage.exceededMaxAllowed");
        }
        super.setPercentage(percentage);
        recalculateCredits();
    }

    public void recalculateCredits() {
        BigDecimal percentage = getPercentage();
        if (percentage != null && getSharedFunction().getCredits() != null) {
            setCredits(getSharedFunction().getCredits().multiply(percentage.divide(MAX_PERCENTAGE))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
        } else {
            setCredits(0.0);
        }
    }

}
