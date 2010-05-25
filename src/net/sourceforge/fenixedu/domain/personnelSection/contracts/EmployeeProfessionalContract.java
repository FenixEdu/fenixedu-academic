package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EmployeeProfessionalContract extends EmployeeProfessionalContract_Base {

    public EmployeeProfessionalContract(final Employee employee, final LocalDate beginDate, final LocalDate endDate,
	    final ContractSituation contractSituation, final String contractSituationGiafId, final DateTime creationDate,
	    final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployee(employee);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setContractSituation(contractSituation);
	setContractSituationGiafId(contractSituationGiafId);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean isValid() {
	return getContractSituation() != null && getBeginDate() != null
		&& (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }
}
