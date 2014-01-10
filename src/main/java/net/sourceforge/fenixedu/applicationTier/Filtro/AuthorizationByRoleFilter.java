/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author jpvl
 */
public abstract class AuthorizationByRoleFilter extends Filtro {
    /**
     * This method returns the role that we want to authorize.
     * 
     * @return RoleType
     */
    abstract protected RoleType getRoleType();

    public void execute() throws NotAuthorizedException {
        User userView = Authenticate.getUser();
        if (((userView != null && userView.getPerson().getPersonRolesSet() != null && !userView.getPerson()
                .hasRole(getRoleType()))) || (userView == null) || (userView.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }

    }
}