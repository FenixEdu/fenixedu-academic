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

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class PersonProfessionalRegime extends PersonProfessionalRegime_Base {

    public PersonProfessionalRegime(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final ProfessionalRegime professionalRegime, final String professionalRegimeGiafId,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setProfessionalRegime(professionalRegime);
        setProfessionalRegimeGiafId(professionalRegimeGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    public boolean isValid() {
        return getProfessionalRegime() != null && getBeginDate() != null
                && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

    private Interval getInterval() {
        return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
                getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
    }

    public boolean overlaps(final Interval interval) {
        Interval regimeInterval = getInterval();
        return getBeginDate() != null
                && ((regimeInterval != null && regimeInterval.overlaps(interval)) || (regimeInterval == null && !getBeginDate()
                        .isAfter(interval.getEnd().toLocalDate())));
    }

    public boolean isActive(LocalDate date) {
        return !getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date));
    }

    public int getDaysInInterval(Interval interval) {
        LocalDate beginDate =
                getBeginDate().isBefore(interval.getStart().toLocalDate()) ? interval.getStart().toLocalDate() : getBeginDate();
        LocalDate endDate =
                getEndDate() == null || getEndDate().isAfter(interval.getEnd().toLocalDate()) ? interval.getEnd().toLocalDate() : getEndDate();
        return Days.daysBetween(beginDate, endDate).getDays();
    }

    public boolean betweenDates(LocalDate beginDate, LocalDate endDate) {
        if (isValid()) {
            if (getEndDate() == null) {
                return endDate == null || !getBeginDate().isAfter(endDate);
            }
            if (endDate == null) {
                return !beginDate.isAfter(getEndDate());
            }
            Interval dateInterval =
                    new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
            return getInterval().overlaps(dateInterval);
        }
        return false;
    }

    public boolean isAfter(PersonProfessionalRegime otherPersonProfessionalRegime) {
        if (!isValid()) {
            return false;
        }
        if (!otherPersonProfessionalRegime.isValid()) {
            return true;
        }
        if (getEndDate() == null) {
            return otherPersonProfessionalRegime.getEndDate() == null ? getBeginDate().isAfter(
                    otherPersonProfessionalRegime.getBeginDate()) : true;
        } else {
            return otherPersonProfessionalRegime.getEndDate() == null ? false : getEndDate().isAfter(
                    otherPersonProfessionalRegime.getEndDate());
        }
    }

}
