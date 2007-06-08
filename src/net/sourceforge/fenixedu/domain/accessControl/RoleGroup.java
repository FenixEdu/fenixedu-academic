package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
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

/**
 * 
 * @author cfgi
 */
public class RoleGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private RoleType roleType;

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
        return getRole().getAssociatedPersonsCount();
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();
        elements.addAll(getRole().getAssociatedPersons());
        return elements;
    }

    /**
     * The host may have restriction on the roles available so checking if a
     * <tt>UserView</tt> is allowed or checking directly is a person is member
     * of this groups has different behaviours. The <tt>UserView</tt> as all
     * the person roles that are allowed in the current host while the person
     * has all the roles assigned to it.
     * 
     * <p>
     * This method checks if the <tt>UserView</tt> contains the same role as
     * this group.
     */
    @Override
    public boolean allows(IUserView userView) {
	return userView != null && !userView.isPublicRequester() && userView.hasRoleType(roleType);
    }

    @Override
    public boolean isMember(Person person) {
	return person != null && person.hasRole(roleType);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new StaticArgument(roleType.getName())
        };
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof RoleGroup)) {
    		return false;
    	}
    	
    	return this.roleType.equals(((RoleGroup) obj).roleType);
    }
    
    @Override
    public int hashCode() {
    	return this.roleType.hashCode();
    }
    
    /**
     * Builder used to create a RoleGroup from a group expression.
     * 
     * @author cfgi
     */
    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            if (arguments.length != 1) {
                throw new WrongNumberOfArgumentsException(arguments.length, getMinArguments(),
                        getMaxArguments());
            }

            try {
                String roleName = (String) arguments[0];
                RoleType roleType = RoleType.valueOf(roleName);

                if (roleType == null) {
                    throw new GroupDynamicExpressionException(
                            "accessControl.group.builder.role.type.notAvailable", roleName);
                }

                return new RoleGroup(roleType);
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, String.class, arguments[0].getClass());
            } catch (IllegalArgumentException e) {
                throw new GroupDynamicExpressionException(
                        "accessControl.group.builder.role.type.doesNotExist", String
                                .valueOf(arguments[0]));
            }
        }

        public int getMinArguments() {
            return 1;
        }

        public int getMaxArguments() {
            return 1;
        }

    }

}
