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
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 * 
 *  
 */
public abstract class ServiceUtils {

    public static Object executeService(IUserView userView, String serviceName, Object[] serviceArgs)
            throws FenixServiceException {
        return ServiceManagerServiceFactory.executeService(userView, serviceName, serviceArgs);
    }

}