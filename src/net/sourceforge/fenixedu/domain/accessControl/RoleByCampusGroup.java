package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.util.BundleUtil;

public abstract class RoleByCampusGroup extends Group {

    private final RoleType roleType;
    private final Campus campus;

    public RoleByCampusGroup(RoleType type, Campus campus) {
	this.roleType = type;
	this.campus = campus;
    }

    public Campus getCampus() {
	return campus;
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> people = new HashSet<Person>();
	Campus campus = getCampus();
	for (Person person : Role.getRoleByRoleType(roleType).getAssociatedPersons()) {
	    if (isPersonInCampus(person, campus) && isPersonAccepted(person)) {
		people.add(person);
	    }
	}
	return people;
    }

    protected abstract boolean isPersonInCampus(Person person, Campus campus);

    protected boolean isPersonAccepted(Person person) {
	return true;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return null;
    }

    @Override
    public String getName() {
	String name = BundleUtil.getStringFromResourceBundle("resources.GroupNameResources",
		"label.name." + getClass().getSimpleName(), new String[] { getCampus().getName() });
	return name != null ? name : super.getName();
    }

}
