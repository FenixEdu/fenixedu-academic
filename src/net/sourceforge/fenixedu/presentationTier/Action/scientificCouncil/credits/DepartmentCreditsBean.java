package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.services.Service;

public class DepartmentCreditsBean implements Serializable {

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

    @Service
    public static void assignPermission(Department department, Employee employee) throws Exception {
	employee.getPerson().getManageableDepartmentCredits().add(department);
	employee.getPerson().addPersonRoleByRoleType(RoleType.DEPARTMENT_CREDITS_MANAGER);
	employee.getPerson().addPersonRoleByRoleType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }

    @Service
    public static void removePermission(Department department, Employee employee) throws Exception {
	employee.getPerson().removeRoleByType(RoleType.DEPARTMENT_CREDITS_MANAGER);
	employee.getPerson().removeRoleByType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
	employee.getPerson().getManageableDepartmentCredits().remove(department);

    }

}