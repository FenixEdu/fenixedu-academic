package net.sourceforge.fenixedu.applicationTier.Filtro.webSiteManager;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.util.RoleType;

/*
 * 
 * @author Fernanda Quitério 23/Abr/2004
 *  
 */
public class WebSiteManagerAuthorizationFilter extends AuthorizationByRoleFilter {
    // the singleton of this class
    public final static WebSiteManagerAuthorizationFilter instance = new WebSiteManagerAuthorizationFilter();

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
        return RoleType.WEBSITE_MANAGER;
    }

}