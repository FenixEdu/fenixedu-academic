package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeGrantOwnerEquivalent extends EmployeeGrantOwnerEquivalent_Base {

    public EmployeeGrantOwnerEquivalent(final EmployeeProfessionalData employeeProfessionalData, final LocalDate beginDate,
	    final LocalDate endDate, final String motive, final String local, final Country country,
	    final GrantOwnerEquivalent grantOwnerEquivalent, final String grantOwnerEquivalentGiafId,
	    final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployeeProfessionalData(employeeProfessionalData);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setMotive(motive);
	setLocal(local);
	setCountry(country);
	setGrantOwnerEquivalent(grantOwnerEquivalent);
	setGrantOwnerEquivalentGiafId(grantOwnerEquivalentGiafId);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean isValid() {
	return getCountry() != null && getBeginDate() != null && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()))
		&& getGrantOwnerEquivalent() != null;
    }
}
