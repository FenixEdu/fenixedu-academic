package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;


public class AssiduousnessRoleAuthorizationFilter extends AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
        return RoleType.MANAGEMENT_ASSIDUOUSNESS;
    }
}