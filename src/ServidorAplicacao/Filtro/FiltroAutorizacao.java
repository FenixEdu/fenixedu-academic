package ServidorAplicacao.Filtro;

/**
 * This class is responsible for verifying if a given user has the
 * authorization to run a service with certain attributes.
 * 
 * @author Joao Pereira
 * @version
 */

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

public class FiltroAutorizacao extends Filtro
{

    public FiltroAutorizacao()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView requester = getRemoteUser(request);
        if (requester == null || !(requester instanceof UserView))
        {
            throw new FenixServiceException("Invalid user ID");
        }

    }
}