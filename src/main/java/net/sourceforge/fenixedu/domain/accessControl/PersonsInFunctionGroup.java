package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

/**
 * This group is an abstraction of all person currently performing a {@link Function}. The elements of the group are all person
 * that have and
 * active {@link PersonFunction} connected to the function.
 * 
 * @author cfgi
 */
@GroupOperator("function")
public class PersonsInFunctionGroup extends FenixGroup {
    private static final long serialVersionUID = -3773344582156056407L;

    @GroupArgument
    private Function function;

    private PersonsInFunctionGroup() {
        super();
    }

    private PersonsInFunctionGroup(Function function) {
        this();
        this.function = function;
    }

    public static PersonsInFunctionGroup get(Function function) {
        return new PersonsInFunctionGroup(function);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { function.getName(), function.getUnit().getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (PersonFunction f : function.getPersonFunctions()) {
            if (f.isActive()) {
                User user = f.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentPersonsInFunctionGroup.getInstance(function);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PersonsInFunctionGroup) {
            return Objects.equal(function, ((PersonsInFunctionGroup) object).function);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(function);
    }
}
