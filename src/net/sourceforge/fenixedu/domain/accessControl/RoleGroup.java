package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;

public class RoleGroup extends DomainBackedGroup<Role> {

    private static final long serialVersionUID = 1L;

    public RoleGroup(Role role) {
        super(role);
    }

    public Role getRole() {
        return getObject();
    }

    @Override
    public int getElementsCount() {
        return this.getRole().getAssociatedPersonsCount();
    }

    @Override
    public Set<Person> getElements() {
    	Set<Person> elements = new HashSet<Person>();
    	elements.addAll(this.getRole().getAssociatedPersons());

    	return elements;
    }
}
