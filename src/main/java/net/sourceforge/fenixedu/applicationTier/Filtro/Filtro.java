package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.person.RoleType;

abstract public class Filtro {

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection<RoleType> getNeededRoleTypes() {
        return null;
    }

    protected boolean containsRoleType(Collection<RoleType> roleTypes) {
        final Collection<RoleType> neededRoleTypes = getNeededRoleTypes();
        if (neededRoleTypes == null || neededRoleTypes.isEmpty()) {
            return true;
        }
        if (roleTypes != null) {
            for (final RoleType roleType : roleTypes) {
                if (neededRoleTypes.contains(roleType)) {
                    return true;
                }
            }
        }
        return false;
    }

}
