package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("persistentMembers")
public class PersistentMembersLinkGroup extends PersistentMembersLinkGroup_Base {
    protected PersistentMembersLinkGroup(PersistentGroupMembers persistentGroupMembers) {
        super();
        setPersistentGroupMembers(persistentGroupMembers);
    }

    @CustomGroupArgument
    public static Argument<PersistentGroupMembers> persistentGroupMembersArgument() {
        return new SimpleArgument<PersistentGroupMembers, PersistentMembersLinkGroup>() {
            @Override
            public PersistentGroupMembers parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<PersistentGroupMembers> getDomainObject(argument);
            }

            @Override
            public Class<? extends PersistentGroupMembers> getType() {
                return PersistentGroupMembers.class;
            }

            @Override
            public String extract(PersistentMembersLinkGroup group) {
                return group.getPersistentGroupMembers() != null ? group.getPersistentGroupMembers().getExternalId() : null;
            }
        };
    }

    @Override
    public String getPresentationName() {
        return getPersistentGroupMembers().getName();
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : getPersistentGroupMembers().getPersonsSet()) {
            if (person.getUser() != null) {
                users.add(person.getUser());
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
        return user != null && getPersistentGroupMembers().getPersonsSet().contains(user.getPerson());
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
        setPersistentGroupMembers(null);
        super.gc();
    }

    public static PersistentMembersLinkGroup getInstance(PersistentGroupMembers persistentGroupMembers) {
        PersistentMembersLinkGroup instance = persistentGroupMembers.getMembersLinkGroup();
        return instance != null ? instance : create(persistentGroupMembers);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentMembersLinkGroup create(PersistentGroupMembers persistentGroupMembers) {
        PersistentMembersLinkGroup instance = persistentGroupMembers.getMembersLinkGroup();
        return instance != null ? instance : new PersistentMembersLinkGroup(persistentGroupMembers);
    }
}
