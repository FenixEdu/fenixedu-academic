/*
 * Created on Dec 16, 2003
 *  
 */
package ServidorAplicacao.Filtro.gep;

import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class GEPAuthorizationFilter extends AuthorizationByRoleFilter
{
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.GEP;
    }
}
