package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class PersonProfessionalData extends PersonProfessionalData_Base {

    public PersonProfessionalData(Person person) {
	super();
	setPerson(person);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getPerson().getRootDomainObject();
    }

    public GiafProfessionalData getGiafProfessionalDataByCategoryType(CategoryType categoryType) {
	for (GiafProfessionalData giafProfessionalData : getGiafProfessionalDatasSet()) {
	    if (giafProfessionalData.getProfessionalCategory() != null
		    && giafProfessionalData.getProfessionalCategory().getCategoryType().equals(categoryType)) {
		return giafProfessionalData;
	    }
	}
	return null;
    }

    public GiafProfessionalData getGiafProfessionalDataByGiafPersonIdentification(String giafPersonIdentification) {
	for (GiafProfessionalData giafProfessionalData : getGiafProfessionalDatasSet()) {
	    if (giafProfessionalData.getGiafPersonIdentification().equalsIgnoreCase(giafPersonIdentification)) {
		return giafProfessionalData;
	    }
	}
	return null;
    }

    public ProfessionalCategory getProfessionalCategoryByCategoryType(CategoryType categoryType, LocalDate date) {
	PersonProfessionalCategory lastPersonProfessionalCategory = null;
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	if (giafProfessionalDataByCategoryType != null) {
	    for (final PersonProfessionalCategory category : giafProfessionalDataByCategoryType
		    .getValidPersonProfessionalCategories()) {
		if (category.isActive(date)
			&& (lastPersonProfessionalCategory == null || category.isAfter(lastPersonProfessionalCategory))) {
		    lastPersonProfessionalCategory = category;
		}
	    }
	    if (lastPersonProfessionalCategory == null) {
		PersonContractSituation lastPersonContractSituationByCategoryType = getLastPersonContractSituationByCategoryType(
			giafProfessionalDataByCategoryType, date, date);
		if (lastPersonContractSituationByCategoryType != null) {
		    return lastPersonContractSituationByCategoryType.getProfessionalCategory();
		}
		if (giafProfessionalDataByCategoryType.getProfessionalCategoryDate() != null
			&& !giafProfessionalDataByCategoryType.getProfessionalCategoryDate().isAfter(date)) {
		    return giafProfessionalDataByCategoryType.getProfessionalCategory();
		}
	    }
	}
	return lastPersonProfessionalCategory == null ? null : lastPersonProfessionalCategory.getProfessionalCategory();
    }

    public ProfessionalCategory getLastProfessionalCategoryByCategoryType(CategoryType categoryType) {
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	return getLastProfessionalCategory(giafProfessionalDataByCategoryType, null, null);
    }

    public ProfessionalCategory getLastProfessionalCategoryByCategoryType(CategoryType categoryType, LocalDate beginDate,
	    LocalDate endDate) {
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	return getLastProfessionalCategory(giafProfessionalDataByCategoryType, beginDate, endDate);
    }

    public ProfessionalCategory getLastProfessionalCategory(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
	    LocalDate endDate) {
	Interval dateInterval = null;
	if (beginDate != null && endDate != null) {
	    dateInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay());
	}
	PersonProfessionalCategory lastPersonProfessionalCategory = null;

	if (giafProfessionalData != null) {
	    for (final PersonProfessionalCategory category : giafProfessionalData.getValidPersonProfessionalCategories()) {
		if ((dateInterval == null || category.overlaps(dateInterval))
			&& (lastPersonProfessionalCategory == null || category.isAfter(lastPersonProfessionalCategory))) {
		    lastPersonProfessionalCategory = category;
		}
	    }
	    if (lastPersonProfessionalCategory == null) {
		PersonContractSituation lastPersonContractSituationByCategoryType = getLastPersonContractSituationByCategoryType(
			giafProfessionalData, beginDate, endDate);
		if (lastPersonContractSituationByCategoryType != null) {
		    return lastPersonContractSituationByCategoryType.getProfessionalCategory();
		}
		if (giafProfessionalData.getProfessionalCategoryDate() != null
			&& !giafProfessionalData.getProfessionalCategoryDate().isAfter(endDate)) {
		    return giafProfessionalData.getProfessionalCategory();
		}
	    }
	}
	return lastPersonProfessionalCategory == null ? null : lastPersonProfessionalCategory.getProfessionalCategory();
    }

    public Set<PersonContractSituation> getPersonContractSituationsByCategoryType(CategoryType categoryType) {
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	return giafProfessionalDataByCategoryType != null ? giafProfessionalDataByCategoryType.getValidPersonContractSituations()
		: new HashSet<PersonContractSituation>();
    }

    public Set<PersonContractSituation> getValidPersonProfessionalExemptionByCategoryType(CategoryType categoryType,
	    Interval intervalWithNextPeriods) {
	Set<PersonContractSituation> personProfessionalExemptions = new HashSet<PersonContractSituation>();
	Set<PersonContractSituation> personContractSituations = getPersonContractSituationsByCategoryType(categoryType);
	for (PersonContractSituation personContractSituation : personContractSituations) {
	    if (personContractSituation.betweenDates(intervalWithNextPeriods)) {
		PersonProfessionalExemption personProfessionalExemption = personContractSituation
			.getPersonProfessionalExemption();
		if (personContractSituation.getContractSituation().getServiceExemption()
			&& ((!personContractSituation.getContractSituation().getMustHaveAssociatedExemption()) || (personProfessionalExemption != null && personProfessionalExemption
				.isLongDuration()))) {
		    personProfessionalExemptions.add(personContractSituation);
		}
	    }
	}
	return personProfessionalExemptions;
    }

    public PersonContractSituation getLastPersonContractSituationByCategoryType(CategoryType categoryType) {
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	return getLastPersonContractSituationByCategoryType(giafProfessionalDataByCategoryType, null, null);
    }

    public PersonContractSituation getLastPersonContractSituationByCategoryType(GiafProfessionalData giafProfessionalData,
	    LocalDate beginDate, LocalDate endDate) {
	Interval dateInterval = null;
	if (beginDate != null && endDate != null) {
	    dateInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay());
	}
	PersonContractSituation lastPersonContractSituation = null;

	if (giafProfessionalData != null) {
	    for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
		if ((dateInterval == null || situation.overlaps(dateInterval))
			&& (lastPersonContractSituation == null || situation.isAfter(lastPersonContractSituation))) {
		    lastPersonContractSituation = situation;
		}
	    }
	}
	return lastPersonContractSituation;
    }

    public PersonContractSituation getCurrentPersonContractSituationByCategoryType(CategoryType categoryType) {
	LocalDate today = new LocalDate();
	PersonContractSituation currentPersonContractSituation = null;
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	if (giafProfessionalDataByCategoryType != null) {
	    for (final PersonContractSituation situation : giafProfessionalDataByCategoryType.getValidPersonContractSituations()) {
		if (situation.isActive(today)
			&& (currentPersonContractSituation == null || situation.isAfter(currentPersonContractSituation))) {
		    currentPersonContractSituation = situation;
		}
	    }
	}
	return currentPersonContractSituation;
    }

    public PersonContractSituation getCurrentOrLastPersonContractSituationByCategoryType(CategoryType categoryType) {
	return getCurrentOrLastPersonContractSituationByCategoryType(categoryType, null, null);
    }

    public PersonContractSituation getCurrentOrLastPersonContractSituationByCategoryType(CategoryType categoryType,
	    LocalDate beginDate, LocalDate endDate) {
	LocalDate today = new LocalDate();
	Interval dateInterval = null;
	if (beginDate != null && endDate != null) {
	    dateInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay());
	}
	PersonContractSituation lastPersonContractSituation = null;
	PersonContractSituation currentPersonContractSituation = null;
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	if (giafProfessionalDataByCategoryType != null) {
	    for (final PersonContractSituation situation : giafProfessionalDataByCategoryType.getValidPersonContractSituations()) {
		if ((dateInterval == null || situation.overlaps(dateInterval))) {
		    if (situation.isActive(today)
			    && (currentPersonContractSituation == null || situation.isAfter(currentPersonContractSituation))) {
			currentPersonContractSituation = situation;
		    }
		    lastPersonContractSituation = situation;
		}
	    }
	}
	return currentPersonContractSituation != null ? currentPersonContractSituation : lastPersonContractSituation;
    }

    public ProfessionalRegime getLastProfessionalRegime(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
	    LocalDate endDate) {
	PersonProfessionalRegime lastPersonProfessionalRegime = null;
	if (giafProfessionalData != null) {
	    for (final PersonProfessionalRegime regime : giafProfessionalData.getValidPersonProfessionalRegimes()) {
		if (regime.betweenDates(beginDate, endDate)
			&& (lastPersonProfessionalRegime == null || regime.isAfter(lastPersonProfessionalRegime))) {
		    lastPersonProfessionalRegime = regime;
		}
	    }
	    if (lastPersonProfessionalRegime == null) {
		if (giafProfessionalData.getProfessionalRegimeDate() != null
			&& !giafProfessionalData.getProfessionalRegimeDate().isAfter(endDate)) {
		    return giafProfessionalData.getProfessionalRegime();
		}
	    }
	}
	return lastPersonProfessionalRegime == null ? null : lastPersonProfessionalRegime.getProfessionalRegime();
    }

    public ProfessionalRelation getLastProfessionalRelation(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
	    LocalDate endDate) {
	PersonProfessionalRelation lastPersonProfessionalRelation = null;
	if (giafProfessionalData != null) {
	    for (final PersonProfessionalRelation relation : giafProfessionalData.getValidPersonProfessionalRelations()) {
		if (relation.betweenDates(beginDate, endDate)
			&& (lastPersonProfessionalRelation == null || relation.isAfter(lastPersonProfessionalRelation))) {
		    lastPersonProfessionalRelation = relation;
		}
	    }
	    if (lastPersonProfessionalRelation == null) {
		if (giafProfessionalData.getProfessionalRegimeDate() != null
			&& !giafProfessionalData.getProfessionalRegimeDate().isAfter(endDate)) {
		    return giafProfessionalData.getProfessionalRelation();
		}
	    }
	}
	return lastPersonProfessionalRelation == null ? null : lastPersonProfessionalRelation.getProfessionalRelation();
    }

    public String getEmployer(final RoleType roleType) {
	final CategoryType categoryType = getCategoryTypeFor(roleType);
	final GiafProfessionalData giafProfessionalData = categoryType == null ? null
		: getGiafProfessionalDataByCategoryType(categoryType);
	return giafProfessionalData == null ? null : giafProfessionalData.getEmployer();
    }

    private static CategoryType getCategoryTypeFor(final RoleType roleType) {
	for (final CategoryType categoryType : CategoryType.values()) {
	    if (categoryType.name().equals(roleType.name())) {
		return categoryType;
	    }
	}
	return null;
    }

}
