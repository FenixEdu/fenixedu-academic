package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Department;

public class DepartmentCreditsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Department department;
	private String employeeNumber;

	public DepartmentCreditsBean() {

	}

	public DepartmentCreditsBean(Department department, String employeeNumber) {
		super();
		setDepartment(department);
		setEmployeeNumber(employeeNumber);
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

}
