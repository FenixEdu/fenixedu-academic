package ServidorAplicacao.Filtro;

/**
 * This class is responsible for verifying if a given user has the
 * authorization to run a service with certain attributes.
 * 
 * @author Angela
 * @version
 */

import java.util.Collection;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;

public class ErasmusAuthorizationFilter extends Filtro
{
    public ErasmusAuthorizationFilter()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
                    Exception
    {
        IUserView requester = getRemoteUser(request);
        Collection roles = requester.getRoles();
        boolean authorizedRequester = false;

        if (AuthorizationUtils.containsRole(roles, RoleType.ERASUMS)) authorizedRequester = true;
        if (!authorizedRequester)
        {
            throw new NotAuthorizedException(" -----------> User = " + requester.getUtilizador()
                            + "ACCESS NOT GRANTED!");
        }
    }
}