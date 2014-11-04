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
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class PersonProfessionalCategory extends PersonProfessionalCategory_Base {

    public PersonProfessionalCategory(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final ProfessionalCategory professionalCategory, final String professionalCategoryGiafId,
            final ProfessionalRegime professionalRegime, final String professionalRegimeGiafId,
            final ProfessionalRelation professionalRelation, final String professionalRelationGiafId, final String step,
            final String level, final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setProfessionalCategory(professionalCategory);
        setProfessionalCategoryGiafId(professionalCategoryGiafId);
        setProfessionalRegime(professionalRegime);
        setProfessionalRegimeGiafId(professionalRegimeGiafId);
        setProfessionalRelation(professionalRelation);
        setProfessionalRelationGiafId(professionalRelationGiafId);
        setStep(step);
        setLevel(level);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    public boolean contains(final LocalDate date) {
        return getBeginDate() != null && (!getBeginDate().isAfter(date) && (!hasEndDate() || !getEndDate().isBefore(date)));
    }

    public boolean overlaps(final Interval interval) {
        Interval categoryInterval = getInterval();
        return getBeginDate() != null
                && ((categoryInterval != null && categoryInterval.overlaps(interval)) || (categoryInterval == null && !getBeginDate()
                        .isAfter(interval.getEnd().toLocalDate())));
    }

    private Interval getInterval() {
        return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
                getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
    }

    public boolean isValid() {
        return getProfessionalCategory() != null && getBeginDate() != null
                && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

    public boolean isAfter(PersonProfessionalCategory lastPersonProfessionalCategory) {
        if (!isValid()) {
            return false;
        }
        if (!lastPersonProfessionalCategory.isValid()) {
            return true;
        }
        if (getEndDate() == null) {
            return lastPersonProfessionalCategory.getEndDate() == null ? getBeginDate().isAfter(
                    lastPersonProfessionalCategory.getBeginDate()) : true;
        } else {
            return lastPersonProfessionalCategory.getEndDate() == null ? false : getEndDate().isAfter(
                    lastPersonProfessionalCategory.getEndDate());
        }
    }

    public boolean isActive(LocalDate date) {
        return !getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date));
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

}
