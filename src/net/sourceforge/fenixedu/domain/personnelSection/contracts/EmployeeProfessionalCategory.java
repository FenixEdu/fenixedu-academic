package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeProfessionalCategory extends EmployeeProfessionalCategory_Base {

    public EmployeeProfessionalCategory(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final ProfessionalCategory professionalCategory, final ProfessionalRegime professionalRegime,
	    final ProfessionalRelation professionalRelation, final String step, final String level, final DateTime creationDate,
	    final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setProfessionalCategory(professionalCategory);
	setProfessionalRegime(professionalRegime);
	setProfessionalRelation(professionalRelation);
	setStep(step);
	setLevel(level);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean hasEndDate() {
	return getEndDate() != null;
    }

    public boolean contains(final LocalDate date) {
	return getBeginDate() != null && (!getBeginDate().isAfter(date) && (!hasEndDate() || !getEndDate().isBefore(date)));
    }

}
