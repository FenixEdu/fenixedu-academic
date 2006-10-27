package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;

/**
 * This groups represents the group of everyone. All person belong to this group
 * and all UserView are allowed.
 * 
 * @author cfgi
 */
public class EveryoneGroup extends Group {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Set<Person> getElements() {
        return new HashSet<Person>(Person.readAllPersons());
    }

    @Override
    public boolean allows(IUserView userView) {
        return true;
    }

    @Override
    public boolean isMember(Person person) {
        return person != null;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof EveryoneGroup;
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}
