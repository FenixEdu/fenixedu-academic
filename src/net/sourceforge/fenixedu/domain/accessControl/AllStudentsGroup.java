package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class AllStudentsGroup extends Group {

    public AllStudentsGroup() {

    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = new HashSet<Person>();
	List<Person> people = Role.getRoleByRoleType(RoleType.STUDENT).getAssociatedPersons();

	for (Person person : people) {
	    if (!person.getStudent().getActiveRegistrations().isEmpty()) {
		elements.add(person);
	    }
	}
	return elements;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return null;
    }

}
