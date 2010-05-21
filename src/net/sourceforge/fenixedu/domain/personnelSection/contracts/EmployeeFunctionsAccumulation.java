package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeFunctionsAccumulation extends EmployeeFunctionsAccumulation_Base {

    public EmployeeFunctionsAccumulation(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final BigDecimal hours, final FunctionsAccumulation functionsAccumulation, final String functionsAccumulationGiafId,
	    final ProfessionalRegime professionalRegime, final String professionalRegimeGiafId, final DateTime creationDate,
	    final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setHours(hours);
	setFunctionsAccumulation(functionsAccumulation);
	setFunctionsAccumulationGiafId(functionsAccumulationGiafId);
	setProfessionalRegime(professionalRegime);
	setProfessionalRegimeGiafId(professionalRegimeGiafId);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

}
