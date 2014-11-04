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

public class PersonProfessionalRelation extends PersonProfessionalRelation_Base {

    public PersonProfessionalRelation(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final ProfessionalRelation professionalRelation, final String professionalRelationGiafId,
            final ProfessionalCategory professionalCategory, final String professionalCategoryGiafId,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setProfessionalRelation(professionalRelation);
        setProfessionalRelationGiafId(professionalRelationGiafId);
        setProfessionalCategory(professionalCategory);
        setProfessionalCategoryGiafId(professionalCategoryGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    public boolean isValid() {
        return getProfessionalCategory() != null && getBeginDate() != null
                && (getEndDate() == null || !getBeginDate().isAfter(getEndDate())) && getProfessionalRelation() != null;
    }

    private Interval getInterval() {
        return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
                getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
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

    public boolean isAfter(PersonProfessionalRelation otherPersonProfessionalRelation) {
        if (!isValid()) {
            return false;
        }
        if (!otherPersonProfessionalRelation.isValid()) {
            return true;
        }
        if (getEndDate() == null) {
            return otherPersonProfessionalRelation.getEndDate() == null ? getBeginDate().isAfter(
                    otherPersonProfessionalRelation.getBeginDate()) : true;
        } else {
            return otherPersonProfessionalRelation.getEndDate() == null ? false : getEndDate().isAfter(
                    otherPersonProfessionalRelation.getEndDate());
        }
    }

}
