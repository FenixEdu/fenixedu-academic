/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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

    public void execute(Object[] parameters) throws Exception {
        IUserView userView = AccessControl.getUserView();
        if (((userView != null && userView.getRoleTypes() != null && !userView.hasRoleType(getRoleType()))) || (userView == null)
                || (userView.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }

    }
}