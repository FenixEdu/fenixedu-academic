package net.sourceforge.fenixedu.domain.organizationalStructure;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;

public class EmployeeWorkingContract extends EmployeeWorkingContract_Base {
    
    public EmployeeWorkingContract(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit) {
        super();        
	super.init(person, beginDate, endDate, unit);
	AccountabilityType accountabilityType = AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.EMPLOYEE_CONTRACT);
	setAccountabilityType(accountabilityType);
    }
 
    @Override
    public Unit getWorkingUnit() {
	return getUnit();
    } 
    
    @Override
    public Employee getEmployee() {
	return getPerson().getEmployee();
    }
    
}
