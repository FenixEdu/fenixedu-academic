package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class AllStudentsGroup extends Group {

    public AllStudentsGroup() {

    }

    @Override
    public boolean equals(final Object other) {
        boolean result = other != null;

        if (result) {
            result = this.getClass().equals(other.getClass());
        }

        return result;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();
        Collection<Person> people = Role.getRoleByRoleType(RoleType.STUDENT).getAssociatedPersons();

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

    @Override
    public PersistentStudentGroup convert() {
        return PersistentStudentGroup.getInstance();
    }
}
