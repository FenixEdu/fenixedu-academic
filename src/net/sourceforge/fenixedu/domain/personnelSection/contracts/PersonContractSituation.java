package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class PersonContractSituation extends PersonContractSituation_Base {

    public PersonContractSituation(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
	    final LocalDate endDate, final String step, final ContractSituation contractSituation,
	    final String contractSituationGiafId, final ProfessionalCategory professionalCategory,
	    final String professionalCategoryGiafId, final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafProfessionalData(giafProfessionalData);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setStep(step);
	setContractSituation(contractSituation);
	setContractSituationGiafId(contractSituationGiafId);
	setProfessionalCategory(professionalCategory);
	setProfessionalCategoryGiafId(professionalCategoryGiafId);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean hasValidDates() {
	return getBeginDate() != null && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

    public boolean isValid() {
	return getProfessionalCategory() != null && hasValidDates();
    }

    public boolean contains(final LocalDate date) {
	return getBeginDate() != null
		&& (!getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date)));
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

    public boolean isAfter(PersonContractSituation otherPersonContractSituation) {
	if (!isValid()) {
	    return false;
	}
	if (!otherPersonContractSituation.isValid()) {
	    return true;
	}
	if (getEndDate() == null) {
	    return otherPersonContractSituation.getEndDate() == null ? getBeginDate().isAfter(
		    otherPersonContractSituation.getBeginDate()) : true;
	} else {
	    return otherPersonContractSituation.getEndDate() == null ? false : getEndDate().isAfter(
		    otherPersonContractSituation.getEndDate());
	}
    }
}
