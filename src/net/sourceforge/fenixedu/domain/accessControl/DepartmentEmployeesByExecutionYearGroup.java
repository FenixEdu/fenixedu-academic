package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

public class DepartmentEmployeesByExecutionYearGroup extends DepartmentByExecutionYearGroup {

    /**
         * 
         */
    private static final long serialVersionUID = 8466471514890333054L;

    public DepartmentEmployeesByExecutionYearGroup(ExecutionYear executionYear, Department department) {
	super(executionYear, department);

    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();
	Collection<Employee> departmentEmployees = getDepartment().getAllWorkingEmployees(
		getExecutionYear().getBeginDateYearMonthDay(),
		getExecutionYear().getEndDateYearMonthDay());

	for (Employee employee : departmentEmployees) {
	    elements.add(employee.getPerson());
	}

	return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasEmployee()) {
	    final Department lastDepartmentWorkingPlace = person.getEmployee()
		    .getLastDepartmentWorkingPlace(getExecutionYear().getBeginDateYearMonthDay(),
			    getExecutionYear().getEndDateYearMonthDay());
	    return (lastDepartmentWorkingPlace != null && lastDepartmentWorkingPlace
		    .equals(getDepartment()));
	}
	return false;
    }
    
    public static class Builder extends DepartmentByExecutionYearGroup.Builder {

        @Override
        protected DepartmentByExecutionYearGroup buildConcreteGroup(ExecutionYear year, Department department) {
            return new DepartmentEmployeesByExecutionYearGroup(year, department);
        }
        
    }
}
