/*
 * Created on 30/Oct/2003
 *  
 */
package ServidorAplicacao.Filtro.grant;

import Util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantOwnerManagerAuthorizationFilter
	extends ServidorAplicacao.Filtro.AuthorizationByRoleFilter
{

	public GrantOwnerManagerAuthorizationFilter()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType()
	{
		return RoleType.GRANT_OWNER_MANAGER;
	}

}
