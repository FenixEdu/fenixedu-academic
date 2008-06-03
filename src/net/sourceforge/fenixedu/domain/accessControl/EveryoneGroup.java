package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
    public String getName() {
    	return RenderUtils.getResourceString("SITE_RESOURCES", "label.net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup");
    }
    
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

}
