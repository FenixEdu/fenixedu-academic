package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.BundleUtil;

public class AllEmployeesGroup extends Group {

    private static final long serialVersionUID = 1L;

    public AllEmployeesGroup() {
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> people = new HashSet<Person>();
	for (final Person person : Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersons()) {
	    if (!person.hasRole(RoleType.TEACHER)) {
		people.add(person);
	    }
	}
	return people;
    }

    @Override
    public boolean isMember(Person person) {
	return person != null && person.hasRole(RoleType.EMPLOYEE) && !person.hasRole(RoleType.TEACHER);
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return null;
    }

    @Override
    public String getName() {
	String name = BundleUtil.getStringFromResourceBundle("resources.GroupNameResources",
		"label.name." + getClass().getSimpleName() + "." + RoleType.EMPLOYEE);
	return name != null ? name : super.getName();
    }

}
