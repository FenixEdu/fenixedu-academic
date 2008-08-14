package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class AlumniAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static AlumniAuthorizationFilter instance = new AlumniAuthorizationFilter();

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
	return RoleType.ALUMNI;
    }

}