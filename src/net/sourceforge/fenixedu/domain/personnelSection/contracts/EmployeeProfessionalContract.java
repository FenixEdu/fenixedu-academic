package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeProfessionalContract extends EmployeeProfessionalContract_Base {

    public EmployeeProfessionalContract(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final ContractSituation contractSituation, final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	check(beginDate, endDate);
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setContractSituation(contractSituation);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    protected void check(LocalDate begin, LocalDate end) {
	if (begin == null) {
	    throw new DomainException("");
	}
	if (end != null && begin.isAfter(end)) {
	    throw new DomainException("");
	}
    }
}
