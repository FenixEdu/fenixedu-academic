/*
 * Created on Jan 11, 2005
 * 
 */

package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author Susana Fernandes
 */
public class ProjectsManagerAuthorizationFilter extends AuthorizationByRoleFilter {
    public final static ProjectsManagerAuthorizationFilter instance = new ProjectsManagerAuthorizationFilter();

    public static Filtro getInstance() {
        return instance;
    }

    protected RoleType getRoleType() {
        return RoleType.PROJECTS_MANAGER;
    }

}