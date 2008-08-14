package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class ResourceManagerAuthorizationFilter extends AuthorizationByRoleFilter {

    // the singleton of this class
    public final static ResourceManagerAuthorizationFilter instance = new ResourceManagerAuthorizationFilter();

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
	return RoleType.RESOURCE_MANAGER;
    }

}
