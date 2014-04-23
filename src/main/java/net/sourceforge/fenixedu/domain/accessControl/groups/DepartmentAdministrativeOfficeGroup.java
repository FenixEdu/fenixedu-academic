package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DomainBackedGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PersistentUnitGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleCustomGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.groups.IntersectionGroup;

public class DepartmentAdministrativeOfficeGroup extends DomainBackedGroup<DepartmentUnit> {

    private static final long serialVersionUID = 1L;

    public DepartmentAdministrativeOfficeGroup(final DepartmentUnit unit) {
        super(unit);
    }

    private boolean hasRoles(final Person person) {
        return person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE) && person.hasRole(RoleType.EMPLOYEE)
                && !person.hasRole(RoleType.TEACHER) && !person.hasRole(RoleType.RESEARCHER);
    }

    @Override
    public Set<Person> getElements() {
        final Set<Person> result = new HashSet<Person>();

        final DepartmentUnit unit = getObject();
        final List<Employee> employees = unit.getAllCurrentActiveWorkingEmployees();
        for (final Employee employee : employees) {
            final Person person = employee.getPerson();
            if (hasRoles(person)) {
                result.add(person);
            }
        }

        return result;
    }

    @Override
    public boolean isMember(final Person person) {
        if (person != null && hasRoles(person)) {
            final DepartmentUnit unit = getObject();
            final List<Employee> employees = unit.getAllCurrentActiveWorkingEmployees();
            if (employees.contains(person.getEmployee())) {
                return true;
            }
        }
        return false;

    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new DepartmentAdministrativeOfficeGroup((DepartmentUnit) arguments[0]);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.unitMembers.notUnit",
                        arguments[0].toString());
            }
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
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        return IntersectionGroup.getInstance(
                PersistentUnitGroup.getInstance(getObject(), AccountabilityTypeEnum.WORKING_CONTRACT, null, true),
                RoleCustomGroup.getInstance(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE),
                RoleCustomGroup.getInstance(RoleType.EMPLOYEE), RoleCustomGroup.getInstance(RoleType.TEACHER).not(),
                RoleCustomGroup.getInstance(RoleType.RESEARCHER).not());
    }
}
