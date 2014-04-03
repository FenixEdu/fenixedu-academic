package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.NobodyGroup;

/**
 * 
 * @author cfgi
 */
public class RoleGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final RoleType roleType;

    public RoleGroup(RoleType roleType) {
        super();

        if (roleType == null) {
            throw new DomainException("accessControl.group.domainBacked.null");
        }

        this.roleType = roleType;
    }

    public RoleGroup(Role role) {
        this(role == null ? null : role.getRoleType());
    }

    public Role getRole() {
        return Role.getRoleByRoleType(roleType);
    }

    @Override
    public int getElementsCount() {
        return getRole().getAssociatedPersons().size();
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();
        elements.addAll(getRole().getAssociatedPersons());
        return elements;
    }

    /**
     * The host may have restriction on the roles available so checking if a <tt>UserView</tt> is allowed or checking directly is
     * a person is member
     * of this groups has different behaviours. The <tt>UserView</tt> as all the
     * person roles that are allowed in the current host while the person has
     * all the roles assigned to it.
     * 
     * <p>
     * This method checks if the <tt>UserView</tt> contains the same role as this group.
     */
    @Override
    public boolean allows(User userView) {
        return userView != null && userView.getPerson().hasRole(roleType);
    }

    @Override
    public boolean isMember(Person person) {
        return person != null && person.hasRole(roleType);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new StaticArgument(roleType.getName()) };
    }

    @Override
    public boolean equals(final Object other) {
        boolean result = other != null;

        if (result) {
            result = this.getClass().equals(other.getClass());
        }

        if (result) {
            final RoleGroup otherGroup = (RoleGroup) other;
            result = this.roleType.equals(otherGroup.roleType);
        }

        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.roleType.hashCode();
    }

    @Override
    public String getPresentationNameKey() {
        return "label.name." + RoleTypeGroup.class.getSimpleName() + "." + roleType;
    }

    /**
     * Builder used to create a RoleGroup from a group expression.
     * 
     * @author cfgi
     */
    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            if (arguments.length != 1) {
                throw new WrongNumberOfArgumentsException(arguments.length, getMinArguments(), getMaxArguments());
            }

            try {
                String roleName = (String) arguments[0];
                RoleType roleType = RoleType.valueOf(roleName);

                if (roleType == null) {
                    throw new GroupDynamicExpressionException("accessControl.group.builder.role.type.notAvailable", roleName);
                }

                return new RoleGroup(roleType);
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, String.class, arguments[0].getClass());
            } catch (IllegalArgumentException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.role.type.doesNotExist",
                        String.valueOf(arguments[0]));
            }
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

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        if (getRole() != null) {
            return RoleCustomGroup.getInstance(getRole());
        }
        return NobodyGroup.getInstance();
    }
}
