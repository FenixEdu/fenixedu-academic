/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop.utils
 * 
 * Created on 9/Dez/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop.utils;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 * 
 * 
 */
public abstract class ServiceUtils {

    public static Object executeService(IUserView userView, String serviceName, Object... serviceArgs)
            throws FenixServiceException, FenixFilterException {
        if (serviceArgs == null)
            serviceArgs = new Object[0];
        return ServiceManagerServiceFactory.executeService(userView, serviceName, serviceArgs);
    }

}