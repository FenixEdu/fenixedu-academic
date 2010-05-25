package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeProfessionalCategory extends EmployeeProfessionalCategory_Base {

    public EmployeeProfessionalCategory(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final ProfessionalCategory professionalCategory, final String professionalCategoryGiafId,
	    final ProfessionalRegime professionalRegime, final String professionalRegimeGiafId,
	    final ProfessionalRelation professionalRelation, final String professionalRelationGiafId, final String step,
	    final String level, final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setProfessionalCategory(professionalCategory);
	setProfessionalCategoryGiafId(professionalCategoryGiafId);
	setProfessionalRegime(professionalRegime);
	setProfessionalRegimeGiafId(professionalRegimeGiafId);
	setProfessionalRelation(professionalRelation);
	setProfessionalRelationGiafId(professionalRelationGiafId);
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

    public boolean isValid() {
	return getProfessionalCategory() != null && getBeginDate() != null
		&& (getEndDate() == null || !getBeginDate().isAfter(getEndDate())) && getProfessionalRegime() != null
		&& getProfessionalRelation() != null;
    }
}
