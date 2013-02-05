/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop.utils
 * 
 * Created on 9/Dez/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 * 
 * 
 */
public abstract class ServiceUtils {

    public static Object executeService(String serviceName, Object[] serviceArgs) throws FenixServiceException,
            FenixFilterException {
        if (serviceArgs == null) {
            serviceArgs = new Object[0];
        }
        return ServiceManagerServiceFactory.executeService(serviceName, serviceArgs);
    }
}