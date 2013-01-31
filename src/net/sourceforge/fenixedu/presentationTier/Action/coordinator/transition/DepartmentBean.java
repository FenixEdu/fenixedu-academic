package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Department;

public class DepartmentBean implements Serializable {

	public Department department;

	public DepartmentBean() {
		setDepartment(null);
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Department getDepartment() {
		return this.department;
	}
}
