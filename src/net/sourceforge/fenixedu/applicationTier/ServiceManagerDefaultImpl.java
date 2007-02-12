package net.sourceforge.fenixedu.applicationTier;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.logging.ServiceExecutionLog;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.applicationTier.logging.UserExecutionLog;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.stm.IllegalWriteException;
import net.sourceforge.fenixedu.stm.ServiceInfo;
import net.sourceforge.fenixedu.stm.Transaction;

import org.apache.commons.collections.FastHashMap;
import org.apache.log4j.Logger;

import pt.utl.ist.berserk.logic.filterManager.FilterInvocationTimingType;
import pt.utl.ist.berserk.logic.filterManager.exceptions.ClassNotIFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterRetrieveException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.IncompatibleFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.InvalidFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.InvalidFilterExpressionException;
import pt.utl.ist.berserk.logic.serviceManager.IServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.ServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.FilterChainFailedException;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.InvalidServiceException;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.ServiceManagerException;

public class ServiceManagerDefaultImpl implements IServiceManagerWrapper {

    private static final Logger logger = Logger.getLogger(ServiceManagerDefaultImpl.class);

    private static boolean serviceLoggingIsOn;

    private static FastHashMap mapServicesToWatch;

    private static boolean userLoggingIsOn;

    private static FastHashMap mapUsersToWatch;

    private static final Map<String,String> KNOWN_WRITE_SERVICES = new ConcurrentHashMap<String,String>();

    static {
        serviceLoggingIsOn = false;
        mapServicesToWatch = new FastHashMap();
        mapServicesToWatch.setFast(true);

        userLoggingIsOn = false;
        mapUsersToWatch = new FastHashMap();
        mapUsersToWatch.setFast(true);
    }

    public class ExceptionWrapper extends RuntimeException {
        public ExceptionWrapper(Throwable t) {
            super(t);
        }
    }

    /**
     * Executes a given service.
     * 
     * @param id
     *            represents the identification of the entity that desires to
     *            run the service
     * 
     * @param service
     *            is a string containing the name of the service to execute
     * 
     * @param argumentos
     *            is a vector with the arguments of the service to execute.
     * @throws FenixFilterException 
     * 
     */
    public Object execute(IUserView id, String service, Object args[])
            throws FenixServiceException, FenixFilterException {
        return execute(id, service, "run", args);
    }

    public Object execute(IUserView id, String service, String method, Object[] args)
            throws FenixServiceException, FenixFilterException {
        try {
            Calendar serviceStartTime = null;
            Calendar serviceEndTime = null;

            IServiceManager manager = ServiceManager.getInstance();
            if (serviceLoggingIsOn || (userLoggingIsOn && id != null)) {
                serviceStartTime = Calendar.getInstance();
            }

            // Replace this line with the following block if conflicting
            // transactions should restart automatically
            // Object serviceResult = manager.execute(id, service, method,
            // args);

            ServiceInfo.setCurrentServiceInfo((id == null) ? null : id.getUtilizador(), service, args);

            // try read-only transaction first, but only for non-public sessions...
            Transaction.setDefaultReadOnly(! KNOWN_WRITE_SERVICES.containsKey(service));

            Object serviceResult = null;
            int tries = 0;
            try {
                while (true) {
                    tries++;
                    try {
                        serviceResult = manager.execute(id, service, method, args);
                        break;
                    } catch (jvstm.CommitException ce) {
                        //                    ce.printStackTrace();
                        System.out.println("Restarting TX because of CommitException");
                        // repeat service
                    } catch (DomainObject.UnableToDetermineIdException ce) {
                        //                    ce.printStackTrace();
                        System.out.println("Restarting TX because of UnableToDetermineIdException");
                        // repeat service
                    } catch (IllegalWriteException iwe) {
                        KNOWN_WRITE_SERVICES.put(service, service);
                        Transaction.setDefaultReadOnly(false);
                        // repeat service
                    }
                }
            } finally {
                Transaction.setDefaultReadOnly(false);
                if (tries > 1) {
                    System.out.println("Service " + service + " took " + tries + " tries.");
                }
            }

            if (serviceLoggingIsOn || (userLoggingIsOn && id != null)) {
                serviceEndTime = Calendar.getInstance();
            }
            if (serviceLoggingIsOn && serviceStartTime != null && serviceEndTime != null) {
                registerServiceExecutionTime(service, method, args, serviceStartTime, serviceEndTime);
            }
            if (userLoggingIsOn && id != null && serviceStartTime != null && serviceEndTime != null) {
                registerUserExecutionOfService(id, service, method, args, serviceStartTime,
                        serviceEndTime);
            }

            return serviceResult;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (InvalidServiceException e) {
            throw new FenixServiceException(e);
        } catch (FilterRetrieveException e) {
            throw new FenixServiceException(e);
        } catch (InvalidFilterExpressionException e) {
            throw new FenixServiceException(e);
        } catch (InvalidFilterException e) {
            throw new FenixServiceException(e);
        } catch (ClassNotIFilterException e) {
            throw new FenixServiceException(e);
        } catch (IncompatibleFilterException e) {
            throw new FenixServiceException(e);
        } catch (FilterChainFailedException e) {
            FilterChainFailedException filterChainFailedException = (FilterChainFailedException) e;
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
            throw new FenixServiceException(e);
        } catch (ServiceManagerException e) {
            throw new ExceptionWrapper(e);
        } catch (Throwable t) {
            throw new ExceptionWrapper(t);
        }
    }

    public synchronized void turnServiceLoggingOn(IUserView id) {
        serviceLoggingIsOn = true;
    }

    public synchronized void turnServiceLoggingOff(IUserView id) {
        serviceLoggingIsOn = false;
    }

    public synchronized void clearServiceLogHistory(IUserView id) {
        mapServicesToWatch.clear();
    }

    public synchronized void turnUserLoggingOn(IUserView id) {
        userLoggingIsOn = true;
    }

    public synchronized void turnUserLoggingOff(IUserView id) {
        userLoggingIsOn = false;
    }

    public synchronized void clearUserLogHistory(IUserView id) {
        mapUsersToWatch.clear();
    }

    private void registerServiceExecutionTime(String service, String method, Object[] args,
            Calendar serviceStartTime, Calendar serviceEndTime) {
        String hashKey = generateServiceHashKey(service, method, args);
        long serviceExecutionTime = calculateServiceExecutionTime(serviceStartTime, serviceEndTime);
        ServiceExecutionLog serviceExecutionLog = (ServiceExecutionLog) mapServicesToWatch.get(hashKey);
        if (serviceExecutionLog == null) {
            serviceExecutionLog = new ServiceExecutionLog(hashKey);
            mapServicesToWatch.put(hashKey, serviceExecutionLog);
        }

        serviceExecutionLog.addExecutionTime(serviceExecutionTime);
    }

    private void registerUserExecutionOfService(IUserView id, String service, String method,
            Object[] args, Calendar serviceStartTime, Calendar serviceEndTime) {
        UserExecutionLog userExecutionLog = (UserExecutionLog) mapUsersToWatch.get(id.getUtilizador());
        if (userExecutionLog == null) {
            userExecutionLog = new UserExecutionLog(id);
            mapUsersToWatch.put(id.getUtilizador(), userExecutionLog);
        }

        userExecutionLog.addServiceCall(generateServiceHashKey(service, method, args), serviceStartTime);
    }

    private long calculateServiceExecutionTime(Calendar serviceStartTime, Calendar serviceEndTime) {
        return serviceEndTime.getTimeInMillis() - serviceStartTime.getTimeInMillis();
    }

    private String generateServiceHashKey(String service, String method, Object[] args) {
        String hashKey = service + "." + method + "(";
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    hashKey += args[i].getClass().getName();
                } else {
                    hashKey += "null";
                }
                if (i + 1 < args.length) {
                    hashKey += ", ";
                }
            }
        }
        hashKey += ")";
        return hashKey;
    }

    public HashMap getMapServicesToWatch(IUserView id) {
        return mapServicesToWatch;
    }

    public Boolean serviceLoggingIsOn(IUserView id) {
        return new Boolean(serviceLoggingIsOn);
    }

    public HashMap getMapUsersToWatch(IUserView id) {
        return mapUsersToWatch;
    }

    public Boolean userLoggingIsOn(IUserView id) {
        return new Boolean(userLoggingIsOn);
    }

    public SystemInfo getSystemInfo(IUserView id) {
        return new SystemInfo();
    }

}
