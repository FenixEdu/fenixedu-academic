package net.sourceforge.fenixedu.framework.factory;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IServiceManagerWrapper;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.ServiceManagerDefaultImpl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixRemoteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ServiceManagerServiceFactory {

    private static final IServiceManagerWrapper service = new ServiceManagerDefaultImpl();

    public static Object executeService(IUserView userView, String serviceName, Object[] serviceArgs)
            throws FenixServiceException, FenixFilterException {
        try {
            return service.execute(userView, serviceName, serviceArgs);
        } catch (ServiceManagerDefaultImpl.ExceptionWrapper e) {
            if (e != null && e.getCause() != null && e.getCause() instanceof FenixServiceException) {
                FenixServiceException fenixServiceException = (FenixServiceException) e.getCause();
                throw fenixServiceException;
            }
            if (e != null && e.getCause() != null && e.getCause() instanceof DomainException) {
                DomainException domainException = (DomainException) e.getCause();
                throw domainException;
            }
            if (e != null && e.getCause() != null && e.getCause() instanceof FenixFilterException) {
                FenixFilterException fenixFilterException = (FenixFilterException) e.getCause();
                throw fenixFilterException;
            }
            throw new FenixRemoteServiceException(e);
        }
    }

    public static Map getServicesLogInfo(IUserView userView) throws FenixServiceException {
        return service.getMapServicesToWatch(userView);
    }

    public static Map getUsersLogInfo(IUserView userView) throws FenixServiceException {
        return service.getMapUsersToWatch(userView);
    }

    public static Boolean serviceLoggingIsOn(IUserView userView) throws FenixServiceException {
        return service.serviceLoggingIsOn(userView);
    }

    public static Boolean userLoggingIsOn(IUserView userView) throws FenixServiceException {
        return service.userLoggingIsOn(userView);
    }

    public static SystemInfo getSystemInfo(IUserView userView) throws FenixServiceException {
        return service.getSystemInfo(userView);
    }

    public static void turnServiceLoggingOn(IUserView userView) throws FenixServiceException {
        service.turnServiceLoggingOn(userView);
    }

    public static void turnUserLoggingOn(IUserView userView) throws FenixServiceException {
        service.turnUserLoggingOn(userView);
    }

    public static void turnServiceLoggingOff(IUserView userView) throws FenixServiceException {
        service.turnServiceLoggingOff(userView);
    }

    public static void turnUserLoggingOff(IUserView userView) throws FenixServiceException {
        service.turnUserLoggingOff(userView);
    }

    public static void clearServiceLogHistory(IUserView userView) throws FenixServiceException {
        service.clearServiceLogHistory(userView);
    }

    public static void clearUserLogHistory(IUserView userView) throws FenixServiceException {
        service.clearUserLogHistory(userView);
    }

}