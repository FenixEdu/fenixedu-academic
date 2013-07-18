package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class TutorshipAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final TutorshipAuthorizationFilter instance = new TutorshipAuthorizationFilter();
    @Override
    protected RoleType getRoleType() {
        return RoleType.TUTORSHIP;
    }
}