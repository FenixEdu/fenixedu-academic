package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class ManagementAssiduousnessAuthorizationFilter extends AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
	return RoleType.MANAGEMENT_ASSIDUOUSNESS;
    }
}