/*
 * Created on Feb 19, 2004
 *  
 */
package ServidorAplicacao.Filtro.student;

import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import Util.RoleType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class DelegateAuthorizationFilter extends AuthorizationByRoleFilter
{
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.DELEGATE;
    }
}
