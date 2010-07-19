package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonProfessionalRelation extends PersonProfessionalRelation_Base {

    public PersonProfessionalRelation(final PersonProfessionalData personProfessionalData, final LocalDate beginDate,
	    final LocalDate endDate, final ProfessionalRelation professionalRelation, final String professionalRelationGiafId,
	    final ProfessionalCategory professionalCategory, final String professionalCategoryGiafId,
	    final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPersonProfessionalData(personProfessionalData);
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

}
