package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author David Santos
 *
 */

public class DegreeAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

  
	public final static DegreeAdministrativeOfficeAuthorizationFilter instance = new DegreeAdministrativeOfficeAuthorizationFilter();
	
	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
	  return instance;
	}
	
	protected RoleType getRoleType() {
		return RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
	}
}