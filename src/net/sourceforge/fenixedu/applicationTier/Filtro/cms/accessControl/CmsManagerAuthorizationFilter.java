/*
 * Created on 13/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author lmac1
 */
public class CmsManagerAuthorizationFilter extends AuthorizationByRoleFilter {

    // the singleton of this class
    public final static CmsManagerAuthorizationFilter instance = new CmsManagerAuthorizationFilter();

    public static Filtro getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.CMS_MANAGER;
    }
}