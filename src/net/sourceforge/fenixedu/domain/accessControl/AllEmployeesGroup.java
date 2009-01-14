package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class AllEmployeesGroup extends RoleTypeGroup {

    public AllEmployeesGroup() {
	super(RoleType.EMPLOYEE);
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> people = new HashSet<Person>();
	for (final Person person : Role.getRoleByRoleType(getRoleType()).getAssociatedPersons()) {
	    if (!person.hasRole(RoleType.TEACHER)) {
		people.add(person);
	    }
	}
	return people;
    }

    @Override
    public boolean isMember(final Person person) {
	return person != null && person.hasRole(RoleType.EMPLOYEE) && !person.hasRole(RoleType.TEACHER);
    }

}
