/*
 * Created on 30/Oct/2003
 *  
 */
package ServidorAplicacao.Filtro.credits;

import Util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class DepartmentCreditsManagerAuthorizationFilter extends
        ServidorAplicacao.Filtro.AuthorizationByRoleFilter {

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