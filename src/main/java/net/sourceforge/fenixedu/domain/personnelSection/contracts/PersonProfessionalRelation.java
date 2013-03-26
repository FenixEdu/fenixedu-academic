package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class PersonProfessionalRelation extends PersonProfessionalRelation_Base {

    public PersonProfessionalRelation(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final ProfessionalRelation professionalRelation, final String professionalRelationGiafId,
            final ProfessionalCategory professionalCategory, final String professionalCategoryGiafId,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
