package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.NoArgumentsGroupBuilder;

/**
 * A group that contains no one.
 * 
 * @author cfgi
 */
public class NoOneGroup extends LeafGroup {

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Set<Person> getElements() {
        return super.buildSet();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[0];
    }

    /**
     * @return <code>false</code>
     */
    @Override
    public boolean allows(IUserView userView) {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    @Override
    public boolean isMember(Person person) {
        return false;
    }
    
}
