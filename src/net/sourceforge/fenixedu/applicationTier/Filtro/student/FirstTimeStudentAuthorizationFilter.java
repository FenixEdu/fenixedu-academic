/*
 * Created on 3/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class FirstTimeStudentAuthorizationFilter extends AuthorizationByRoleFilter {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.FIRST_TIME_STUDENT;
    }

}