package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import ServidorAplicacao.IUserView;

/**
 * This class is the superclass of all Filter. A Filter class is responsible for
 * ensuring that a given service can be invoked by a certain user or by changing
 * the arguments that should be processed by the service. Filter classes must
 * also define a public static method, called getInstance, that returns an
 * instance of the filter class. This method is called through reflection. If
 * the Filter class does not maintain any state, then this method should return
 * a singleton instance.
 * 
 * @author Joao Pereira
 * @version
 */

abstract public class Filtro extends AccessControlFilter
/* implements IFilter */
{
    protected IUserView getRemoteUser(ServiceRequest request) {
        return (IUserView) request.getRequester();
    }

    protected Object[] getServiceCallArguments(ServiceRequest request) {
        return request.getArguments();
    }
    
}