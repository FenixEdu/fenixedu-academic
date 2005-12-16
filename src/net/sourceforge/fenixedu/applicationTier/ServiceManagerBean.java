package net.sourceforge.fenixedu.applicationTier;

import java.util.Calendar;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.logging.ServiceExecutionLog;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.applicationTier.logging.UserExecutionLog;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.stm.ServiceInfo;

import org.apache.commons.collections.FastHashMap;
import org.apache.log4j.Logger;

import pt.utl.ist.berserk.logic.serviceManager.IServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.ServiceManager;

/**
 * This class is the entry point of the system to execute a service. It receives
 * the service to execute, its arguments and an identificator of the entity that
 * wants to run the service.
 * 
 * @author Luis Cruz
 * @author José Pedro Pereira
 * @version
 */
public class ServiceManagerBean implements SessionBean, IServiceManagerWrapper {

    private static final Logger logger = Logger.getLogger(ServiceManagerBean.class);

    private static boolean serviceLoggingIsOn;

    private static FastHashMap mapServicesToWatch;

    private static boolean userLoggingIsOn;

    private static FastHashMap mapUsersToWatch;

    static {
        serviceLoggingIsOn = false;
        mapServicesToWatch = new FastHashMap();
        mapServicesToWatch.setFast(true);

        userLoggingIsOn = false;
        mapUsersToWatch = new FastHashMap();
        mapUsersToWatch.setFast(true);
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
     * 
     */
    public Object execute(IUserView id, String service, Object args[]) {
        return execute(id, service, "run", args);
    }

    public Object execute(IUserView id, String service, String method, Object[] args)
            throws EJBException {
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

            Object serviceResult = null;
            while (true) {
                try {
                    serviceResult = manager.execute(id, service, method, args);
                    break;
                } catch (jvstm.CommitException ce) {
                    System.out.println("Restarting TX because of CommitException");
                    // repeat service
                } catch (DomainObject.UnableToDetermineIdException ce) {
                    System.out.println("Restarting TX because of UnableToDetermineIdException");
                    // repeat service
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
        } catch (Exception e) {
            if (e instanceof FenixServiceException || e instanceof DomainException) {
                e.printStackTrace();
                logger.warn(e);
            } else {
                e.printStackTrace();
                logger.error(e);
            }
            throw (EJBException) new EJBException(e).initCause(e);
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error(t);
            throw new EJBException(t.getMessage());
        }
    }

    public void ejbCreate() {
        // nothing to do
    }

    public void ejbActivate() throws EJBException {
        // nothing to do
    }

    public void ejbPassivate() throws EJBException {
        // nothing to do
    }

    public void ejbRemove() throws EJBException {
        // nothing to do
    }

    public void setSessionContext(SessionContext arg0) throws EJBException {
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
