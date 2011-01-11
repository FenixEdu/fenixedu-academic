package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

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

    public String getProfessionalCategoryNameByCategoryType(CategoryType categoryType, LocalDate date) {
	GiafProfessionalData giafProfessionalDataByCategoryType = getGiafProfessionalDataByCategoryType(categoryType);
	if (giafProfessionalDataByCategoryType != null) {
	    for (final PersonProfessionalCategory category : giafProfessionalDataByCategoryType.getPersonProfessionalCategories()) {
		if (category.contains(date)) {
		    return category.getProfessionalCategory().getName().getContent();
		}
	    }
	}
	return null;
    }

}
