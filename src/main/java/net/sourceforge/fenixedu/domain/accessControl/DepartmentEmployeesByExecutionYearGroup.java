package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;

public class DepartmentEmployeesByExecutionYearGroup extends DepartmentByExecutionYearGroup {

    /**
         * 
         */
    private static final long serialVersionUID = 8466471514890333054L;

    public DepartmentEmployeesByExecutionYearGroup(ExecutionYear executionYear, Department department) {
        super(executionYear, department);
    }

    public DepartmentEmployeesByExecutionYearGroup(String executionYear, String department) {
        super(executionYear, department);

    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        Collection<Employee> departmentEmployees =
                getDepartment().getAllWorkingEmployees(getExecutionYear().getBeginDateYearMonthDay(),
                        getExecutionYear().getEndDateYearMonthDay());

        for (Employee employee : departmentEmployees) {
            elements.add(employee.getPerson());
        }

        return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasEmployee()) {
            final Department lastDepartmentWorkingPlace =
                    person.getEmployee().getLastDepartmentWorkingPlace(getExecutionYear().getBeginDateYearMonthDay(),
                            getExecutionYear().getEndDateYearMonthDay());
            return (lastDepartmentWorkingPlace != null && lastDepartmentWorkingPlace.equals(getDepartment()));
        }
        return false;
    }

    public static class Builder extends DepartmentByExecutionYearGroup.Builder {

        @Override
        protected DepartmentByExecutionYearGroup buildConcreteGroup(String year, String department) {
            return new DepartmentEmployeesByExecutionYearGroup(year, department);
        }

    }

    @Override
    public String getPresentationNameKey() {
        return "label.name.employees.by.department.and.execution.year";
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDepartment().getName(), getExecutionYear().getYear() };
    }

    @Override
    public PersistentUnitGroup convert() {
        ///// TODO: attention, we are loosing the ExecutionYear here. make sure that when you want it time framed use isMember(user, when)
        if (getDepartment() == null) {
            throw new InvalidGroupException();
        }
        return PersistentUnitGroup.getInstance(getDepartment().getDepartmentUnit(), AccountabilityTypeEnum.WORKING_CONTRACT,
                null, false);
    }
}
