/*
 * Created on Dec 16, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gep;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class GEPAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final GEPAuthorizationFilter instance = new GEPAuthorizationFilter();

    /*
     * (non-Javadoc)
     * 
     * @seeServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#
     * getRoleType()
     */
    @Override
    protected RoleType getRoleType() {
        return RoleType.GEP;
    }
}