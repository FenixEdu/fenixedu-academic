/*
 * Created on 13/Mar/2003 by jpvl
 *
 */
package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author jpvl
 */
public class StudentAuthorizationFilter extends AuthorizationByRoleFilter {
    // the singleton of this class
    public final static StudentAuthorizationFilter instance = new StudentAuthorizationFilter();

    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }

}