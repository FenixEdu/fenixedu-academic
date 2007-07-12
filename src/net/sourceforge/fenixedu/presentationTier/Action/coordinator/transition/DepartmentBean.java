package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;

public class DepartmentBean implements Serializable {

    public DomainReference<Department> department;
    
    public DepartmentBean() {
	setDepartment(null);
    }
    
    public void setDepartment(Department department) {
	this.department = new DomainReference<Department>(department);
    }
    
    public Department getDepartment() {
	return this.department.getObject();
    }
}
