package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DomainBackedGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DepartmentAdministrativeOfficeGroup extends DomainBackedGroup<DepartmentUnit> {

    private static final long serialVersionUID = 1L;

    public DepartmentAdministrativeOfficeGroup(final DepartmentUnit unit) {
	super(unit);
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> result = new HashSet<Person>();

	final DepartmentUnit unit = getObject();
	final List<Employee> employees = unit.getAllCurrentActiveWorkingEmployees();
	for (final Employee employee : employees) {
	    final Person person = employee.getPerson();
	    if (person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)
		    && !person.hasRole(RoleType.TEACHER)
		    && !person.hasRole(RoleType.EMPLOYEE)) {
		result.add(person);
	    }
	}

	return result;
    }

    @Override
    public boolean isMember(final Person person) {
	if (person != null
		&& person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)
		&& !person.hasRole(RoleType.TEACHER)
		&& !person.hasRole(RoleType.EMPLOYEE)
		&& person.hasEmployee()) {
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
	return new Argument[] { new IdOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    try {
		return new DepartmentAdministrativeOfficeGroup((DepartmentUnit) arguments[0]);
	    } catch (ClassCastException e) {
		throw new GroupDynamicExpressionException("accessControl.group.builder.unitMembers.notUnit", arguments[0]
			.toString());
	    }
	}

	public int getMinArguments() {
	    return 1;
	}

	public int getMaxArguments() {
	    return 1;
	}

    }

}
