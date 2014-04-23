package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

@CustomGroupOperator("role")
public class RoleCustomGroup extends RoleCustomGroup_Base {

    protected RoleCustomGroup(Role role) {
        super();
        this.setRole(role);
    }

    @Override
    public String getPresentationName() {
        return getRole().getRoleType().getLocalizedName();
    }

    @Override
    public Set<User> getMembers() {
        return FluentIterable.from(getRole().getAssociatedPersonsSet()).filter(new Predicate<Person>() {
            @Override
            public boolean apply(Person person) {
                return person.getUser() != null;
            }
        }).transform(new Function<Person, User>() {
            @Override
            public User apply(Person person) {
                return person.getUser();
            }
        }).toSet();
    }

    /*
     * Time Machine method not available
     */
    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        return user.getPerson().hasRole(getRole().getRoleType());
    }

    /*
     * Time Machine method not available
     */
    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @CustomGroupArgument
    public static Argument<Role> roleArg() {
        return new SimpleArgument<Role, RoleCustomGroup>() {

            private static final long serialVersionUID = 7891697517211605643L;

            @Override
            public Role parse(String argument) {
                RoleType roleType = RoleType.valueOf(argument);
                return Role.getRoleByRoleType(roleType);
            }

            @Override
            public Class<? extends Role> getType() {
                return Role.class;
            }

            @Override
            public String extract(RoleCustomGroup group) {
                return group.getRole().getRoleType().name();
            }
        };
    }

    public static Set<Group> groupsForUser(User user) {
        return Sets.newHashSet(Collections2.transform(user.getPerson().getPersonRolesSet(), new Function<Role, Group>() {
            @Override
            public Group apply(Role input) {
                return getInstance(input);
            }
        }));
    }

    public static RoleCustomGroup getInstance(final RoleType role) {
        return getInstance(Role.getRoleByRoleType(role));
    }

    public static RoleCustomGroup getInstance(final Role role) {
        RoleCustomGroup instance = role.getRoleCustomGroup();
        return instance != null ? instance : create(role);
    }

    @Atomic(mode = TxMode.WRITE)
    private static RoleCustomGroup create(final Role role) {
        RoleCustomGroup instance = role.getRoleCustomGroup();
        return instance != null ? instance : new RoleCustomGroup(role);
    }
}
