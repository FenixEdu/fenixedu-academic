/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import Util.RoleType;

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

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView userView = getRemoteUser(request);
        if (((userView != null && userView.getRoles() != null && !AuthorizationUtils.containsRole(
                userView.getRoles(), getRoleType())))
                || (userView == null) || (userView.getRoles() == null)) {
            throw new NotAuthorizedFilterException();
        }

    }
}