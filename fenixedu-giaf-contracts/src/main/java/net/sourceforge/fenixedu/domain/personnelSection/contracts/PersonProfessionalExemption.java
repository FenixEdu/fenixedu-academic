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
package org.fenixedu.academic.domain.personnelSection.contracts;

import org.joda.time.Interval;
import org.joda.time.PeriodType;

public class PersonProfessionalExemption extends PersonProfessionalExemption_Base {

    public PersonProfessionalExemption() {
        super();
    }

    public boolean isValid() {
        return false;
    }

    private Interval getInterval() {
        return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
                getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
    }

    public boolean betweenDates(Interval interval) {
        if (isValid()) {
            if (getEndDate() == null) {
                return !getBeginDate().isAfter(interval.getEnd().toLocalDate());
            }
            return getInterval().overlaps(interval);
        }
        return false;
    }

    public boolean getIsSabaticalOrEquivalent() {
        return false;
    }

    public boolean getHasMandatoryCredits() {
        return false;
    }

    public boolean getGiveCredits() {
        return false;
    }

    public boolean isLongDuration() {
        Integer daysBetween =
                new Interval(getBeginDate().toDateTimeAtStartOfDay(), getEndDate().toDateTimeAtStartOfDay().plusDays(1))
                        .toPeriod(PeriodType.days()).getDays();
        return (daysBetween == null || daysBetween >= 90);
    }

}
