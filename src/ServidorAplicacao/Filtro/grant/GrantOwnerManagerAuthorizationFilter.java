/*
 * Created on 30/Oct/2003
 *
 */
package ServidorAplicacao.Filtro.grant;

import ServidorAplicacao.Filtro.Filtro;
import Util.RoleType;

/**
 * @author  Barbosa
 * @author  Pica
 */
public class GrantOwnerManagerAuthorizationFilter extends ServidorAplicacao.Filtro.AuthorizationByRoleFilter {

  
	// the singleton of this class
	public final static GrantOwnerManagerAuthorizationFilter instance = new GrantOwnerManagerAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
	  return instance;
	}


	/* (non-Javadoc)
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType() {
		return RoleType.GRANT_OWNER_MANAGER;
	}

}
