/*
 * Created on 25/Set/2003, 17:01:42
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 25/Set/2003, 17:01:42
 *  
 */
public class SeminaryCoordinatorFilter extends AuthorizationByRoleFilter {
    //  the singleton of this class
    public final static SeminaryCoordinatorFilter instance = new SeminaryCoordinatorFilter();

    public static Filtro getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}