package net.sourceforge.fenixedu.applicationTier.Filtro.spaceManager;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class SpaceManagerAuthorizationFilter extends AuthorizationByRoleFilter{

    // the singleton of this class
    public final static SpaceManagerAuthorizationFilter instance = new SpaceManagerAuthorizationFilter();

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
        return RoleType.SPACE_MANAGER;
    }
}
