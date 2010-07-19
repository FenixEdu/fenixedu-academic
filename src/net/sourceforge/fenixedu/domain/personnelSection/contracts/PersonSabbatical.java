package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonSabbatical extends PersonSabbatical_Base {

    public PersonSabbatical(final PersonProfessionalData personProfessionalData, final LocalDate beginDate,
	    final LocalDate endDate, final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPersonProfessionalData(personProfessionalData);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean isValid() {
	return getBeginDate() != null && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

}
