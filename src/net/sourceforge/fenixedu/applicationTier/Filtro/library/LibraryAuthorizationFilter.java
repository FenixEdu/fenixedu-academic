package net.sourceforge.fenixedu.applicationTier.Filtro.library;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class LibraryAuthorizationFilter extends AuthorizationByRoleFilter {

	@Override
	protected RoleType getRoleType() {
		return RoleType.LIBRARY;
	}

}
