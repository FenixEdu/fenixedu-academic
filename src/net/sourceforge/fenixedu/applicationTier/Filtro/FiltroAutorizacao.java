package net.sourceforge.fenixedu.applicationTier.Filtro;

/**
 * This class is responsible for verifying if a given user has the authorization
 * to run a service with certain attributes.
 * 
 * @author Joao Pereira
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class FiltroAutorizacao extends Filtro {

    public FiltroAutorizacao() {
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView requester = getRemoteUser(request);
        if (requester == null || !(Authenticate.isValidUserView(requester))) {
            throw new NotAuthorizedFilterException("Invalid user ID");
        }

    }
}