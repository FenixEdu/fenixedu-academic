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
public class CreditsManagerAuthorizationFilter
	extends ServidorAplicacao.Filtro.AuthorizationByRoleFilter
{

	public CreditsManagerAuthorizationFilter()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType()
	{
		return RoleType.CREDITS_MANAGER;
	}

}
