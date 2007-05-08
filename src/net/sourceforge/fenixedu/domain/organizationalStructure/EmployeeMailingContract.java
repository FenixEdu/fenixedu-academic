package net.sourceforge.fenixedu.domain.organizationalStructure;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;

public class EmployeeMailingContract extends EmployeeMailingContract_Base {
    
    public EmployeeMailingContract(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit) {
        super();
	super.init(person, beginDate, endDate, unit);
	AccountabilityType accountabilityType = AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.EMPLOYEE_CONTRACT);
	setAccountabilityType(accountabilityType);
    }
    
    @Override
    public Unit getMailingUnit() {
	return getUnit();
    }
    
    @Override
    public Employee getEmployee() {
	return getPerson().getEmployee();
    }
}
