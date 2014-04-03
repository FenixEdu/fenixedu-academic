package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.AnyoneGroup;

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
    public String getPresentationNameBundle() {
        return "resources.SiteResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label.net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup";
    }

    @Override
    public Set<Person> getElements() {
        return new HashSet<Person>(Person.readAllPersons());
    }

    @Override
    public boolean allows(User userView) {
        return true;
    }

    @Override
    public boolean isMember(Person person) {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof EveryoneGroup;
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[0];
    }

    @Override
    public AnyoneGroup convert() {
        return AnyoneGroup.getInstance();
    }
}
