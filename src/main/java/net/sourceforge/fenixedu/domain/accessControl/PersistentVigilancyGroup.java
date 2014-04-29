package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

@CustomGroupOperator("vigilancy")
public class PersistentVigilancyGroup extends PersistentVigilancyGroup_Base {

    public PersistentVigilancyGroup(Vigilancy vigilancy) {
        super();
        setVigilancy(vigilancy);
    }

    @CustomGroupArgument(index = 1)
    public static Argument<Vigilancy> vigilancyArgument() {
        return new SimpleArgument<Vigilancy, PersistentVigilancyGroup>() {
            @Override
            public Vigilancy parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Vigilancy> getDomainObject(argument);
            }

            @Override
            public Class<? extends Vigilancy> getType() {
                return Vigilancy.class;
            }

            @Override
            public String extract(PersistentVigilancyGroup group) {
                return group.getVigilancy() != null ? group.getVigilancy().getExternalId() : "";
            }
        };
    }

    @Override
    public String getPresentationName() {
        return Joiner.on('\n').join(FluentIterable.from(getMembers()).transform(new Function<User, String>() {
            @Override
            public String apply(User user) {
                return user.getPresentationName();
            }
        }));
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : getVigilancy().getTeachers()) {
            User user = person.getUser();
            if (user != null) {
                users.add(user);
            }
        }
        User user = getVigilancy().getVigilantWrapper().getPerson().getUser();
        if (user != null) {
            users.add(user);
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

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    @Override
    protected void gc() {
        setVigilancy(null);
        super.gc();
    }

    public static PersistentVigilancyGroup getInstance(Vigilancy vigilancy) {
        PersistentVigilancyGroup instance = vigilancy.getVigilancyGroup();
        return instance != null ? instance : create(vigilancy);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentVigilancyGroup create(Vigilancy vigilancy) {
        PersistentVigilancyGroup instance = vigilancy.getVigilancyGroup();
        return instance != null ? instance : new PersistentVigilancyGroup(vigilancy);
    }

}
