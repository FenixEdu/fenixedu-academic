package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class BennuGroupBridge extends net.sourceforge.fenixedu.domain.accessControl.Group {

    private static final long serialVersionUID = 3085739530671656778L;

    private final Group bennuGroup;

    public BennuGroupBridge(Group group) {
        this.bennuGroup = group;
    }

    @Override
    public String getName() {
        return bennuGroup.getPresentationName();
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
        return getElements().size();
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null) {
            return bennuGroup.isMember(person.getUser());
        }
        return bennuGroup.isMember(null);
    }

    @Override
    public boolean allows(User user) {
        return bennuGroup.isMember(user);
    }

    @Override
    public String getExpression() {
        return bennuGroup.getExternalId();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {};
    }
}
