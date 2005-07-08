package net.sourceforge.fenixedu.applicationTier;

import java.rmi.RemoteException;
import java.util.HashMap;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;

/**
 * The business interface for the service manager seassion bean. *
 * 
 * @author Luis Cruz
 * @author José Pedro Pereira
 * @version
 */
public interface IServiceManagerWrapper {

    /**
     * Executes a given service.
     * 
     * @param id
     *            represents the identification of the entity that desires to
     *            run the service
     * @param service
     *            is a string containing the name of the service to execute
     * @param argumentos
     *            is a vector with the arguments of the service to execute.
     * @throws FenixServiceException
     * @throws NotAuthorizedException
     */
    public Object execute(IUserView id, String service, Object argumentos[])
            throws FenixServiceException, RemoteException;

    public Object execute(IUserView id, String service, String methods, Object argumentos[])
            throws FenixServiceException, RemoteException;

    public HashMap getMapServicesToWatch(IUserView id) throws RemoteException;

    public HashMap getMapUsersToWatch(IUserView id) throws RemoteException;

    public Boolean serviceLoggingIsOn(IUserView id) throws RemoteException;

    public Boolean userLoggingIsOn(IUserView id) throws RemoteException;

    public void turnServiceLoggingOn(IUserView id) throws RemoteException;

    public void turnServiceLoggingOff(IUserView id) throws RemoteException;

    public void turnUserLoggingOn(IUserView id) throws RemoteException;

    public void turnUserLoggingOff(IUserView id) throws RemoteException;

    public void clearServiceLogHistory(IUserView id) throws RemoteException;

    public void clearUserLogHistory(IUserView id) throws RemoteException;

    public SystemInfo getSystemInfo(IUserView id) throws RemoteException;

}