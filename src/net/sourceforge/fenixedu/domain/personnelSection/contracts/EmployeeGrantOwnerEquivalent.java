package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeGrantOwnerEquivalent extends EmployeeGrantOwnerEquivalent_Base {

    public EmployeeGrantOwnerEquivalent(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final String motive, final String local, final Country country, final GrantOwnerEquivalent grantOwnerEquivalent,
	    final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setMotive(motive);
	setLocal(local);
	setCountry(country);
	setGrantOwnerEquivalent(grantOwnerEquivalent);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean isValid() {
	return getCountry() != null && getBeginDate() != null && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()))
		&& getGrantOwnerEquivalent() != null;
    }
}
