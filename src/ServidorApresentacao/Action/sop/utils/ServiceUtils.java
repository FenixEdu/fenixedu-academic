/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 9/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 * 
 *  
 */
public abstract class ServiceUtils {

    public static Object executeService(IUserView userView, String serviceName, Object[] serviceArgs)
            throws FenixServiceException, FenixFilterException {
        if (serviceArgs == null)
            serviceArgs = new Object[0];
        return ServiceManagerServiceFactory.executeService(userView, serviceName, serviceArgs);
    }

}