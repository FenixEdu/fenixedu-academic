/*
 * Created on 30/Oct/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class CreditsManagerAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public CreditsManagerAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.CREDITS_MANAGER;
    }

}