package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("grouping")
public class PersistentGroupingGroup extends PersistentGroupingGroup_Base {
    protected PersistentGroupingGroup(Grouping grouping) {
        super();
        setGrouping(grouping);
    }

    @CustomGroupArgument
    public static Argument<Grouping> degreeArgument() {
        return new SimpleArgument<Grouping, PersistentGroupingGroup>() {
            @Override
            public Grouping parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Grouping> getDomainObject(argument);
            }

            @Override
            public Class<? extends Grouping> getType() {
                return Grouping.class;
            }

            @Override
            public String extract(PersistentGroupingGroup group) {
                return group.getGrouping() != null ? group.getGrouping().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getGrouping().getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Attends attend : getGrouping().getAttendsSet()) {
            User user = attend.getRegistration().getStudent().getPerson().getUser();
            if (user != null) {
                users.add(user);
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
        return getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentGroupingGroup getInstance(Grouping grouping) {
        PersistentGroupingGroup instance = grouping.getGroupingGroup();
        return instance != null ? instance : create(grouping);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentGroupingGroup create(Grouping grouping) {
        PersistentGroupingGroup instance = grouping.getGroupingGroup();
        return instance != null ? instance : new PersistentGroupingGroup(grouping);
    }
}
