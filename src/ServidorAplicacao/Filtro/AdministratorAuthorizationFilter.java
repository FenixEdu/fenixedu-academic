/*
 * Created on 2003/07/27
 * 
 */
package ServidorAplicacao.Filtro;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 **/

import Util.RoleType;

public class AdministratorAuthorizationFilter extends AuthorizationByRoleFilter {

  
	// the singleton of this class
	public final static AdministratorAuthorizationFilter instance = new AdministratorAuthorizationFilter();
	
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
		return RoleType.ADMINISTRATOR;
	}
}