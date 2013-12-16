package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

abstract public class Filtro {

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection<RoleType> getNeededRoleTypes() {
        return null;
    }

    protected boolean containsRoleType(Collection<Role> roles) {
        final Collection<RoleType> neededRoleTypes = getNeededRoleTypes();
        if (neededRoleTypes == null || neededRoleTypes.isEmpty()) {
            return true;
        }
        if (roles != null) {
            for (final Role role : roles) {
                if (neededRoleTypes.contains(role.getRoleType())) {
                    return true;
                }
            }
        }
        return false;
    }

}
