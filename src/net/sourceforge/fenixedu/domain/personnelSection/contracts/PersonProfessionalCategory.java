package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

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
	setRootDomainObject(RootDomainObject.getInstance());
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
		&& (getEndDate() == null || !getBeginDate().isAfter(getEndDate())) && getProfessionalRegime() != null
		&& getProfessionalRelation() != null;
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
	    return lastPersonProfessionalCategory.getEndDate() == null ? true : getEndDate().isAfter(
		    lastPersonProfessionalCategory.getEndDate());
	}
    }
}
