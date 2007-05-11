package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType;

public class EmployeeServiceExemption extends EmployeeServiceExemption_Base {
    
    public EmployeeServiceExemption(Employee employee, YearMonthDay beginDate, YearMonthDay endDate,
	    ServiceExemptionType type, String institution) {
	
	super();	
	super.init(beginDate, endDate, type, institution);
	setEmployee(employee);	
    }

    @Override
    public void setEmployee(Employee employee) {
	if(employee == null) {
	    throw new DomainException("error.employeeServiceExemption.empty.employee");
	}
	super.setEmployee(employee);
    }
    
    @Override
    public void delete() {
	super.setEmployee(null);
	super.delete();
    }
}
