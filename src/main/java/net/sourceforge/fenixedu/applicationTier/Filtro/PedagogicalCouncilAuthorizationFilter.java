package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class PedagogicalCouncilAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final PedagogicalCouncilAuthorizationFilter instance = new PedagogicalCouncilAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.PEDAGOGICAL_COUNCIL;
    }

}
