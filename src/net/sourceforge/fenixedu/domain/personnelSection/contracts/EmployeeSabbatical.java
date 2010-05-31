package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeSabbatical extends EmployeeSabbatical_Base {

    public EmployeeSabbatical(final EmployeeProfessionalData employeeProfessionalData, final LocalDate beginDate,
	    final LocalDate endDate, final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployeeProfessionalData(employeeProfessionalData);
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
