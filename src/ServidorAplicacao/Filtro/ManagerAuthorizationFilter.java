/*
 * Created on 13/Mai/2003
 */
package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author lmac1
 */
public class ManagerAuthorizationFilter extends AuthorizationByRoleFilter {

    // the singleton of this class
    public final static ManagerAuthorizationFilter instance = new ManagerAuthorizationFilter();

    public static Filtro getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.MANAGER;
    }
}