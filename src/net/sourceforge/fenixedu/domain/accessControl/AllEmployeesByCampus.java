package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AllEmployeesByCampus extends Group {

    private final Campus campus;

    public AllEmployeesByCampus(Campus campus) {
	super();
	this.campus = campus;
    }

    public Campus getCampus() {
	return campus;
    }

    @Override
    public boolean isMember(final Person person) {
	return isMember(person, getCampus());
    }

    public boolean isMember(final Person person, final Campus campus) {
	return person.hasRole(RoleType.EMPLOYEE) && !person.hasRole(RoleType.TEACHER) && person.getEmployee().worksAt(campus);
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> people = new HashSet<Person>();
	final Campus campus = getCampus();
	for (final Person person : Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersons()) {
	    if (isMember(person, campus)) {
		people.add(person);
	    }
	}
	return people;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getCampus()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    final Campus campus = (Campus) arguments[0];
	    if (campus == null) {
		throw new VariableNotDefinedException("campus");
	    }
	    return new AllEmployeesByCampus(campus);

	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}

    }

}
