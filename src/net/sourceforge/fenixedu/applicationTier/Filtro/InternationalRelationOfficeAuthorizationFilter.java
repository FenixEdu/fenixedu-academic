package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class InternationalRelationOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static InternationalRelationOfficeAuthorizationFilter instance = new InternationalRelationOfficeAuthorizationFilter();

    public static Filtro getInstance() {
	return instance;
    }

    @Override
    protected RoleType getRoleType() {
	return RoleType.INTERNATIONAL_RELATION_OFFICE;
    }

}
