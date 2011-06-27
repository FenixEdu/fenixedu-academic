package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonSabbatical extends PersonSabbatical_Base {

    public PersonSabbatical(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate, final LocalDate endDate,
	    final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafProfessionalData(giafProfessionalData);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    @Override
    public boolean isValid() {
	return getBeginDate() != null && getEndDate() != null && !getBeginDate().isAfter(getEndDate());
    }

    @Override
    public boolean getIsSabaticalOrEquivalent() {
	return true;
    }

    @Override
    public boolean getHasMandatoryCredits() {
	return true;
    }

    @Override
    public boolean getGiveCredits() {
	return true;
    }

}
