package net.sourceforge.fenixedu.applicationTier;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * The home interface for the service manager seassion bean.
 * 
 * @author Luis Cruz
 * @author José Pedro Pereira
 * @version
 */
public interface ServiceManagerHome extends EJBHome {
    public ServiceManager create() throws CreateException, RemoteException;
}