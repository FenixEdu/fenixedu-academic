package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;

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
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            return new DepartmentEmployeesGroup((Department) arguments[0]);
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

    }

    @Override
    public PersistentUnitGroup convert() {
        return PersistentUnitGroup.getInstance(getObject().getDepartmentUnit(), AccountabilityTypeEnum.WORKING_CONTRACT, null,
                true);
    }
}
