package ServidorAplicacao.Filtro;

import ServidorAplicacao.IUserView;
import Util.RoleType;

/**
 * @author David Santos
 *
 */

public class DegreeAndMasterAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

  
		public final static DegreeAndMasterAdministrativeOfficeAuthorizationFilter instance = new DegreeAndMasterAdministrativeOfficeAuthorizationFilter();
        
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

		public void preFiltragem(IUserView id, Object[] argumentos) throws Exception {
				if(!AuthorizationUtils.containsRole(id.getRoles(), RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
						if(!AuthorizationUtils.containsRole(id.getRoles(), RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
								super.preFiltragem(id, argumentos);
						}
				}
		}

}