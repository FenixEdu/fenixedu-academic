/*
 * Created on 30/Oct/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class DepartmentCreditsManagerAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public DepartmentCreditsManagerAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.DEPARTMENT_CREDITS_MANAGER;
    }

}