/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author jpvl
 */
public class TeacherAuthorizationFilter extends AuthorizationByRoleFilter
{
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.TEACHER;
    }
}
