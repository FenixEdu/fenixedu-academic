/*
 * Created on 3/Ago/2004
 *
 */
package ServidorAplicacao.Filtro.student;

import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import Util.RoleType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class FirstTimeStudentAuthorizationFilter extends AuthorizationByRoleFilter {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.FIRST_TIME_STUDENT;
    }

}