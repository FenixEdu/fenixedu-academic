package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeProfessionalRelation extends EmployeeProfessionalRelation_Base {

    public EmployeeProfessionalRelation(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final ProfessionalRelation professionalRelation, final ProfessionalCategory professionalCategory,
	    final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setProfessionalRelation(professionalRelation);
	setProfessionalCategory(professionalCategory);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

}
