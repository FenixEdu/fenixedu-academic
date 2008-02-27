package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;

public class EmployeeMonthyBalanceResume implements Serializable {

    Employee employee;

    List<EmployeeBalanceResume> employeeBalanceResumeList;

    public EmployeeMonthyBalanceResume(Employee employee) {
	setEmployee(employee);
	setEmployeeBalanceResumeList(new ArrayList<EmployeeBalanceResume>());
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public List<EmployeeBalanceResume> getEmployeeBalanceResumeList() {
	return employeeBalanceResumeList;
    }

    public void setEmployeeBalanceResumeList(List<EmployeeBalanceResume> employeeBalanceResumeList) {
	this.employeeBalanceResumeList = employeeBalanceResumeList;
    }

}
