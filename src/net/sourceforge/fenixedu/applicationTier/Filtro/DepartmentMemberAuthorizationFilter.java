package net.sourceforge.fenixedu.applicationTier.Filtro;

/**
 * This class is responsible for verifying if a given user has the authorization
 * to run a service with certain attributes.
 * 
 * @author Nuno Nunes & Joana Mota
 * @version
 */

import net.sourceforge.fenixedu.domain.person.RoleType;

public class DepartmentMemberAuthorizationFilter extends AuthorizationByRoleFilter {

    // the singleton of this class
    public final static DepartmentMemberAuthorizationFilter instance = new DepartmentMemberAuthorizationFilter();

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
        return RoleType.DEPARTMENT_MEMBER;
    }
}