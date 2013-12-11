package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import pt.ist.bennu.core.annotation.CustomGroupArgument;
import pt.ist.bennu.core.annotation.CustomGroupOperator;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.groups.Group;
import pt.ist.fenixframework.Atomic;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

@CustomGroupOperator("role")
public class RoleCustomGroup extends RoleCustomGroup_Base {

    protected RoleCustomGroup(Role role) {
        super();
        this.setRole(role);
    }

    @Override
    public String getPresentationName() {
        return "RoleCustomGroup - " + getRole().getRoleType().getLocalizedName();
    }

    @Override
    public Set<User> getMembers() {
        return Sets.newHashSet(Collections2.transform(getRole().getAssociatedPersonsSet(), new Function<Person, User>() {
            @Override
            public User apply(Person input) {
                return input.getUser();
            }
        }));
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

    @Atomic
    public static RoleCustomGroup getInstance(final Role role) {
        return select(RoleCustomGroup.class, new Predicate<RoleCustomGroup>() {
            @Override
            public boolean apply(RoleCustomGroup group) {
                return group.getRole().equals(role);
            }
        }, new Supplier<RoleCustomGroup>() {
            @Override
            public RoleCustomGroup get() {
                return new RoleCustomGroup(role);
            }
        });
    }

}
