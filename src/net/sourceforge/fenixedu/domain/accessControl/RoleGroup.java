package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * 
 * @author cfgi
 */
public class RoleGroup extends DomainBackedGroup<Role> {

    private static final long serialVersionUID = 1L;

    public RoleGroup(Role role) {
        super(role);
    }

    public Role getRole() {
        return getObject();
    }

    @Override
    public int getElementsCount() {
        return this.getRole().getAssociatedPersonsCount();
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();
        elements.addAll(this.getRole().getAssociatedPersons());

        return elements;
    }
    
    /**
     * The host may have restriction on the roles available so checking if a
     * <tt>UserView</tt> is allowed or checking directly is a person is member
     * of this groups has different behaviours. The <tt>UserView</tt> as all
     * the person roles that are allowed in the current host while the person
     * has all the roles assigned to it.
     */
    @Override
    public boolean allows(IUserView userView) {
        if (userView.getRoleTypes().contains(getRole().getRoleType())) {
            return super.allows(userView);
        }
        else {
            return false;
        }
    }
    
    /**
     * Builder used to create a RoleGroup from a group expression.
     * 
     * @author cfgi
     */
    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            if (arguments.length != 1) {
                throw new WrongNumberOfArgumentsException(arguments.length, getMinArguments(), getMaxArguments());
            }
            
            try {
                String roleName = (String) arguments[0];
                Role role = Role.getRoleByRoleType(RoleType.valueOf(roleName));
                
                if (role == null) {
                    throw new GroupDynamicExpressionException("accessControl.group.builder.role.type.notAvailable", roleName);
                }
                
                return new RoleGroup(role);
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, String.class, arguments[0].getClass());
            } catch (IllegalArgumentException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.role.type.doesNotExist", String.valueOf(arguments[0]));
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
