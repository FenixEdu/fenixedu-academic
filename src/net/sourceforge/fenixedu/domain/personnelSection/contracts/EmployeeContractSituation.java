package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class EmployeeContractSituation extends EmployeeContractSituation_Base {

    public EmployeeContractSituation(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final String step, final ContractSituation contractSituation, final ProfessionalCategory professionalCategory,
	    final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	check(beginDate, endDate);
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setStep(step);
	setContractSituation(contractSituation);
	setProfessionalCategory(professionalCategory);
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

    public static EmployeeContractSituation getCurrentEmployeeContractSituation(Employee employee) {
	LocalDate today = new LocalDate();
	for (final EmployeeContractSituation employeeContractSituation : employee.getEmployeeContractSituations()) {
	    if (employeeContractSituation.isValid() && employeeContractSituation.isActive(today)) {
		return employeeContractSituation;
	    }
	}
	return null;
    }

    public boolean isValid() {
	return getProfessionalCategory() != null && getBeginDate() != null
		&& (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

    private boolean isActive(LocalDate day) {
	return getEndDate() == null ? !getBeginDate().isAfter(day) : getInterval().contains(day.toDateTimeAtStartOfDay());
    }

    private Interval getInterval() {
	return getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(), getEndDate().toDateTimeAtStartOfDay()
		.plusMillis(1)) : null;
    }

    public static EmployeeContractSituation getLastEmployeeContractSituation(Employee employee) {
	LocalDate today = new LocalDate();
	EmployeeContractSituation lastEmployeeContractSituation = null;
	for (EmployeeContractSituation employeeContractSituation : employee.getEmployeeContractSituations()) {
	    if (employeeContractSituation.isValid()) {
		if (employeeContractSituation.isActive(today)) {
		    return employeeContractSituation;
		} else if (lastEmployeeContractSituation == null
			|| employeeContractSituation.getBeginDate().isAfter(lastEmployeeContractSituation.getBeginDate())) {
		    lastEmployeeContractSituation = employeeContractSituation;
		}
	    }
	}
	return lastEmployeeContractSituation;
    }
}
