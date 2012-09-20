package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class DepartmentCreditsBean implements Serializable {

    protected Department department;

    protected ExecutionSemester executionSemester;

    protected List<Department> availableDepartments;

    public DepartmentCreditsBean() {
	setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
    }

    public Department getDepartment() {
	return department;
    }

    public void setDepartment(Department department) {
	this.department = department;
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public List<Department> getAvailableDepartments() {
	return availableDepartments;
    }

    public void setAvailableDepartments(List<Department> availableDepartments) {
	this.availableDepartments = availableDepartments;
	if (availableDepartments.size() == 1) {
	    setDepartment(availableDepartments.get(0));
	}
    }

}