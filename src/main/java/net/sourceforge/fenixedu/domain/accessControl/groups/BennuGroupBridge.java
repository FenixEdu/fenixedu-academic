package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class BennuGroupBridge extends net.sourceforge.fenixedu.domain.accessControl.Group {

    private static final long serialVersionUID = 3085739530671656778L;

    private final Group bennuGroup;

    private BennuGroupBridge(String bennuExpression) {
        this.bennuGroup = Group.parse(bennuExpression);
    }

    @Override
    public Set<Person> getElements() {
        return Sets.newHashSet(Iterables.transform(bennuGroup.getMembers(), new Function<User, Person>() {
            @Override
            public Person apply(User input) {
                return input.getPerson();
            }
        }));
    }

    @Override
    public int getElementsCount() {
        return bennuGroup.getMembers().size();
    }

    @Override
    public boolean isMember(Person person) {
        if (person.getUser() == null) {
            return false;
        } else {
            return bennuGroup.isMember(person.getUser());
        }
    }

    @Override
    public boolean allows(User user) {
        return bennuGroup.isMember(user);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new StaticArgument(bennuGroup.expression()) };
    }

    public static class Builder implements GroupBuilder {
        @Override
        public net.sourceforge.fenixedu.domain.accessControl.Group build(Object[] arguments) {
            return new BennuGroupBridge((String) arguments[0]);
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }
    }
}
