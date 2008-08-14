package net.sourceforge.fenixedu.applicationTier.Filtro.research;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class ResearcherFilter extends AuthorizationByRoleFilter {

    @Override
    protected RoleType getRoleType() {
	return RoleType.RESEARCHER;
    }
}
