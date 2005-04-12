package net.sourceforge.fenixedu.applicationTier;

import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;

/**
 * The business interface for the service manager seassion bean. *
 * 
 * @author José Pedro Pereira
 * @version
 */
public interface ServiceManagerLocal extends EJBLocalObject, IServiceManagerWrapper {

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
     * @throws FenixServiceException
     * @throws NotAuthorizedException
     */
    public Object execute(IUserView id, String service, Object argumentos[])
            throws FenixServiceException;

    public Object execute(IUserView id, String service, String methods, Object argumentos[])
            throws FenixServiceException;

    public HashMap getMapServicesToWatch(IUserView id);

    public HashMap getMapUsersToWatch(IUserView id);

    public Boolean serviceLoggingIsOn(IUserView id);

    public Boolean userLoggingIsOn(IUserView id);

    public void turnServiceLoggingOn(IUserView id);

    public void turnServiceLoggingOff(IUserView id);

    public void turnUserLoggingOn(IUserView id);

    public void turnUserLoggingOff(IUserView id);

    public void clearServiceLogHistory(IUserView id);

    public void clearUserLogHistory(IUserView id);

    public SystemInfo getSystemInfo(IUserView id);

}