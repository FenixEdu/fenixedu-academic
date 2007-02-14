package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public class DepartmentEmployeesGroup extends DomainBackedGroup<Department> {

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;

    public DepartmentEmployeesGroup(Department department) {
        super(department);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> result = new HashSet<Person>();
        
        for (Employee employee : getObject().getAllCurrentActiveWorkingEmployees()) {
            result.add(employee.getPerson());
        }
        
        return result;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new IdOperator(getObject())
        };
    }

    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            return new DepartmentEmployeesGroup((Department) arguments[0]);
        }

        public int getMinArguments() {
            return 1;
        }

        public int getMaxArguments() {
            return 1;
        }
        
    }
}
