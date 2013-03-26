package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class BolonhaManagerAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static BolonhaManagerAuthorizationFilter instance = new BolonhaManagerAuthorizationFilter();

    public static Filtro getInstance() {
        return instance;
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.BOLONHA_MANAGER;
    }
}