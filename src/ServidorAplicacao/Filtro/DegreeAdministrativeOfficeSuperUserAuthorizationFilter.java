package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author David Santos
 *  
 */

public class DegreeAdministrativeOfficeSuperUserAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static DegreeAdministrativeOfficeSuperUserAuthorizationFilter instance = new DegreeAdministrativeOfficeSuperUserAuthorizationFilter();

    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }

    protected RoleType getRoleType() {
        return RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER;
    }
}