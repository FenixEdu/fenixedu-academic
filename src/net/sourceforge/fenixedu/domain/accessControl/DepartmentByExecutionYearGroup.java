package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public abstract class DepartmentByExecutionYearGroup extends LeafGroup {

    private DomainReference<ExecutionYear> executionYearReference;

    private DomainReference<Department> departmentReference;

    public DepartmentByExecutionYearGroup(ExecutionYear executionYear, Department department) {

        this.executionYearReference = new DomainReference<ExecutionYear>(executionYear);
        this.departmentReference = new DomainReference<Department>(department);

    }

    public Department getDepartment() {
        return this.departmentReference.getObject();
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYearReference.getObject();
    }
}
