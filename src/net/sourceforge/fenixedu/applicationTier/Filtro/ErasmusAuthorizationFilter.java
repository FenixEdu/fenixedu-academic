package net.sourceforge.fenixedu.applicationTier.Filtro;

/**
 * This class is responsible for verifying if a given user has the authorization
 * to run a service with certain attributes.
 * 
 * @author Angela
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ErasmusAuthorizationFilter extends Filtro {
    public ErasmusAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView requester = getRemoteUser(request);
        boolean authorizedRequester = false;

        if (requester.hasRoleType( RoleType.ERASMUS))
            authorizedRequester = true;
        if (!authorizedRequester) {
            throw new NotAuthorizedFilterException(" -----------> User = " + requester.getUtilizador()
                    + "ACCESS NOT GRANTED!");
        }
    }
}