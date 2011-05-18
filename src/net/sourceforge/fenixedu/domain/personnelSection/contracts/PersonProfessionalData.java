package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
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
	    if (giafProfessionalData.getProfessionalCategory().getCategoryType().equals(categoryType)) {
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
	}
	if (lastPersonProfessionalCategory == null) {
	    PersonContractSituation lastPersonContractSituationByCategoryType = getLastPersonContractSituationByCategoryType(
		    categoryType, date, date);
	    return lastPersonContractSituationByCategoryType == null ? null : lastPersonContractSituationByCategoryType
		    .getProfessionalCategory();
	}
	return lastPersonProfessionalCategory == null ? null : lastPersonProfessionalCategory.getProfessionalCategory();
    }

    public ProfessionalCategory getLastProfessionalCategoryByCategoryType(CategoryType categoryType) {
	return getLastProfessionalCategoryByCategoryType(categoryType, null, null);
    }

    public ProfessionalCategory getLastProfessionalCategoryByCategoryType(CategoryType categoryType, LocalDate beginDate,
	    LocalDate endDate) {
	Interval dateInterval = null;
	if (beginDate != null && endDate != null) {
	    dateInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay());
	}
	PersonProfessionalCategory lastPersonProfessionalCategory = null;
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	if (giafProfessionalDataByCategoryType != null) {
	    for (final PersonProfessionalCategory category : giafProfessionalDataByCategoryType
		    .getValidPersonProfessionalCategories()) {
		if ((dateInterval == null || category.overlaps(dateInterval))
			&& (lastPersonProfessionalCategory == null || category.isAfter(lastPersonProfessionalCategory))) {
		    lastPersonProfessionalCategory = category;
		}
	    }
	}
	if (lastPersonProfessionalCategory == null) {
	    PersonContractSituation lastPersonContractSituationByCategoryType = getLastPersonContractSituationByCategoryType(
		    categoryType, beginDate, endDate);
	    return lastPersonContractSituationByCategoryType == null ? null : lastPersonContractSituationByCategoryType
		    .getProfessionalCategory();
	}
	return lastPersonProfessionalCategory == null ? null : lastPersonProfessionalCategory.getProfessionalCategory();
    }

    public PersonContractSituation getLastPersonContractSituationByCategoryType(CategoryType categoryType, LocalDate beginDate,
	    LocalDate endDate) {
	Interval dateInterval = null;
	if (beginDate != null && endDate != null) {
	    dateInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay());
	}
	PersonContractSituation lastPersonContractSituation = null;
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	if (giafProfessionalDataByCategoryType != null) {
	    for (final PersonContractSituation situation : giafProfessionalDataByCategoryType.getValidPersonContractSituations()) {
		if ((dateInterval == null || situation.overlaps(dateInterval))
			&& (lastPersonContractSituation == null || situation.isAfter(lastPersonContractSituation))) {
		    lastPersonContractSituation = situation;
		}
	    }
	}
	return lastPersonContractSituation;
    }

}
