package net.sourceforge.fenixedu.framework.factory;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import net.sourceforge.fenixedu.applicationTier.IServiceManagerWrapper;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.ServiceManagerHome;
import net.sourceforge.fenixedu.applicationTier.ServiceManagerLocalHome;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixRemoteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.framework.ejb.EJBHomeFactory;

import org.apache.log4j.Logger;

import pt.utl.ist.berserk.logic.filterManager.FilterInvocationTimingType;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.FilterChainFailedException;

/**
 * @author Luis Cruz
 * @author José Pedro Pereira
 *  
 */
public class ServiceManagerServiceFactory {

    private static final Logger logger = Logger.getLogger(ServiceManagerServiceFactory.class);

    private static ServiceManagerServiceFactory instance = null;

    private IServiceManagerWrapper service = null;

    // The purpose of this variable is to synchronize access to the constructor
    // of this class, in the getInstance method.
    // Creating an int[0] is usually faster than creating an Object.
    private static int[] objectSynchCreateInstance = new int[0];

    private ServiceManagerServiceFactory() {
        super();
        init();
    }

    private void init() {
        if (service != null) {
            return;
        }

        try {
            ServiceManagerLocalHome serviceManagerLocalHome = (ServiceManagerLocalHome) EJBHomeFactory
                    .getInstance().lookupLocalHome("Fenix/ServidorAplicacao/ServiceManagerLocal",
                            ServiceManagerLocalHome.class);
            
            logger.info("Using local ejb service manager calls.");
            service = serviceManagerLocalHome.create();
        } catch (Exception e) {
            try {
                ServiceManagerHome serviceManagerHome = (ServiceManagerHome) EJBHomeFactory
                        .getInstance().lookupHome("Fenix/ServidorAplicacao/ServiceManager",
                                ServiceManagerHome.class);
                logger.info("Using remote ejb service manager calls.");
                service = serviceManagerHome.create();
            } catch (Exception e2) {
                try {
                    Class serviceMagagerBeanClass = Class
                            .forName("net.sourceforge.fenixedu.applicationTier.ServiceManagerBean");
                    service = (IServiceManagerWrapper) serviceMagagerBeanClass.getConstructor((Class[]) null)
                            .newInstance((Object[]) null);
                    logger.info("Using direct service manager calls.");
                } catch (Exception e1) {
                    service = null;
                    throw new RuntimeException("Unable to create any service manager.", e2);
                }
            }
        }
    }

    private static ServiceManagerServiceFactory getInstance() {
        synchronized (objectSynchCreateInstance) {
            if (instance == null) {
                instance = new ServiceManagerServiceFactory();
            }
            if (instance.service == null) {
                instance.init();
            }
        }

        return instance;
    }

    public static IServiceManagerWrapper createService() {
        return getInstance().createServiceDelegate();
    }

    public IServiceManagerWrapper createServiceDelegate() {
        return service;
    }

    public static Object executeService(IUserView userView, String serviceName, Object[] serviceArgs)
            throws FenixServiceException, FenixFilterException {
        return getInstance().executeServiceDelegate(userView, serviceName, serviceArgs);
    }

    private Object executeServiceDelegate(IUserView userView, String serviceName, Object[] serviceArgs)
            throws FenixServiceException, FenixFilterException {
        try {
            return service.execute(userView, serviceName, serviceArgs);
        } catch (EJBException e) {
            if (e != null && e.getCause() != null && e.getCause() instanceof FenixServiceException) {
                FenixServiceException fenixServiceException = (FenixServiceException) e.getCause();
                throw fenixServiceException;
            }
            if (e != null && e.getCause() != null && e.getCause() instanceof FilterChainFailedException) {
                FilterChainFailedException filterChainFailedException = (FilterChainFailedException) e.getCause();
                Map failedPreFilters = filterChainFailedException.getFailedFilters(FilterInvocationTimingType.PRE);
                Map failedPostFilters = filterChainFailedException.getFailedFilters(FilterInvocationTimingType.POST);
                if (failedPreFilters != null && !failedPreFilters.isEmpty()) {
                    List failledExceptions = (List) failedPreFilters.values().iterator().next();
                    throw (FenixFilterException) failledExceptions.get(0);
                }
                if (failedPostFilters != null && !failedPostFilters.isEmpty()) {
                    List failledExceptions = (List) failedPostFilters.values().iterator().next();
                    throw (FenixFilterException) failledExceptions.get(0);
                }
            }
            throw new FenixRemoteServiceException(e);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static Map getServicesLogInfo(IUserView userView) throws FenixServiceException {
        return getInstance().getServicesLogInfoDelegate(userView);
    }

    private Map getServicesLogInfoDelegate(IUserView userView) throws FenixServiceException {
        try {
            return service.getMapServicesToWatch(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static Map getUsersLogInfo(IUserView userView) throws FenixServiceException {
        return getInstance().getUsersLogInfoDelegate(userView);
    }

    private Map getUsersLogInfoDelegate(IUserView userView) throws FenixServiceException {
        try {
            return service.getMapUsersToWatch(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static Boolean serviceLoggingIsOn(IUserView userView) throws FenixServiceException {
        return getInstance().serviceLoggingIsOnDelegate(userView);
    }

    private Boolean serviceLoggingIsOnDelegate(IUserView userView) throws FenixServiceException {
        try {
            return service.serviceLoggingIsOn(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static Boolean userLoggingIsOn(IUserView userView) throws FenixServiceException {
        return getInstance().userLoggingIsOnDelegate(userView);
    }

    private Boolean userLoggingIsOnDelegate(IUserView userView) throws FenixServiceException {
        try {
            return service.userLoggingIsOn(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static SystemInfo getSystemInfo(IUserView userView) throws FenixServiceException {
        return getInstance().getSystemInfoDelegate(userView);
    }

    private SystemInfo getSystemInfoDelegate(IUserView userView) throws FenixServiceException {
        try {
            return service.getSystemInfo(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static void turnServiceLoggingOn(IUserView userView) throws FenixServiceException {
        getInstance().turnServiceLoggingOnDelegate(userView);
    }

    private void turnServiceLoggingOnDelegate(IUserView userView) throws FenixServiceException {
        try {
            service.turnServiceLoggingOn(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static void turnUserLoggingOn(IUserView userView) throws FenixServiceException {
        getInstance().turnUserLoggingOnDelegate(userView);
    }

    private void turnUserLoggingOnDelegate(IUserView userView) throws FenixServiceException {
        try {
            service.turnUserLoggingOn(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static void turnServiceLoggingOff(IUserView userView) throws FenixServiceException {
        getInstance().turnServiceLoggingOffDelegate(userView);
    }

    private void turnServiceLoggingOffDelegate(IUserView userView) throws FenixServiceException {
        try {
            service.turnServiceLoggingOff(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static void turnUserLoggingOff(IUserView userView) throws FenixServiceException {
        getInstance().turnUserLoggingOffDelegate(userView);
    }

    private void turnUserLoggingOffDelegate(IUserView userView) throws FenixServiceException {
        try {
            service.turnUserLoggingOff(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static void clearServiceLogHistory(IUserView userView) throws FenixServiceException {
        getInstance().clearServiceLogHistoryDelegate(userView);
    }

    private void clearServiceLogHistoryDelegate(IUserView userView) throws FenixServiceException {
        try {
            service.clearServiceLogHistory(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

    public static void clearUserLogHistory(IUserView userView) throws FenixServiceException {
        getInstance().clearUserLogHistoryDelegate(userView);
    }

    private void clearUserLogHistoryDelegate(IUserView userView) throws FenixServiceException {
        try {
            service.clearUserLogHistory(userView);
        } catch (RemoteException e) {
            service = null;
            throw new FenixRemoteServiceException(e);
        }
    }

}