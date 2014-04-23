package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public class RoleTypeGroup extends Group {

    private static final long serialVersionUID = 1L;
    private final RoleType roleType;

    public RoleTypeGroup(RoleType roleType) {
        this.roleType = roleType;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    @Override
    public Set<Person> getElements() {
        return new HashSet<Person>(Role.getRoleByRoleType(getRoleType()).getAssociatedPersons());
    }

    @Override
    public boolean isMember(Person person) {
        return (person == null) ? false : person.hasRole(getRoleType());
    }

    @Override
    public String getExpression() {
        return new RoleGroup(Role.getRoleByRoleType(getRoleType())).getExpression();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

    @Override
    public String getPresentationNameKey() {
        return super.getPresentationNameKey() + "." + roleType;
    }

    @Override
    public boolean equals(final Object other) {
        boolean result = other != null;

        if (result) {
            result = this.getClass().equals(other.getClass());
        }

        if (result) {
            final RoleTypeGroup otherGroup = (RoleTypeGroup) other;
            result = this.roleType.equals(otherGroup.roleType);
        }

        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.roleType.hashCode();
    }

    @Override
    public RoleCustomGroup convert() {
        return RoleCustomGroup.getInstance(getRoleType());
    }
}
