/*
 * Created on 13/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author lmac1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ManagerAuthorizationFilter extends AuthorizationByRoleFilter {

// the singleton of this class
public final static ManagerAuthorizationFilter instance = new ManagerAuthorizationFilter();

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
	return RoleType.MANAGER;
}
}
