/*
 * Created on 13/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author lmac1
 */
public class ManagerAuthorizationFilter extends AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
        return RoleType.MANAGER;
    }
}