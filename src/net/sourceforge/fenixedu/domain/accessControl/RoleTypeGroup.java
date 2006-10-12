package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class RoleTypeGroup extends Group {

    private static final long serialVersionUID = 1L;
    private RoleType roleType;

    public RoleTypeGroup(RoleType roleType) {
        this.roleType = roleType;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    @Override
    public Set<Person> getElements() {
        return new HashSet<Person>(Role.getRoleByRoleType(getRoleType()).getAssociatedPersons());
    }
    
    @Override
    public boolean isMember(Person person) {
	return (person == null) ? false : person.hasRole(getRoleType());
    }

    @Override
    public boolean allows(IUserView userView) {
	return (userView == null) ? false : isMember(userView.getPerson());
    }
    
}
