package ServidorAplicacao;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import pt.utl.ist.berserk.logic.serviceManager.IServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.ServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.ExecutedFilterException;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.ExecutedServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * This class is the entry point of the system to execute a service. It receives the service to execute,
 * its arguments and an identificator of the entity that wants to run the service.
 * 
 * The static method init must be executed before accessing the singleton of this class.
 * 
 * @author Luis Cruz
 * @version
 */

public class ServiceManagerBean implements SessionBean, IServiceManagerWrapper
{

	/**
	 * Executes a given service.
	 * 
	 * @param id
	 *            represents the identification of the entity that desires to run the service
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
	public Object execute(IUserView id, String service, Object args[]) throws FenixServiceException
	{
		return execute(id, service, "run", args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.serviceManager.IServiceManagerWrapper#execute(java.lang.Object,
	 *      java.lang.String, java.lang.String, java.lang.Object[])
	 */
	public Object execute(IUserView id, String service, String method, Object[] args)
		throws FenixServiceException
	{
		try
		{
			IServiceManager manager = ServiceManager.getInstance();
			return manager.execute(id, service, method, args);
		}
		catch (ExecutedServiceException ex)
		{
			if (ex.getServiceThrownException() instanceof FenixServiceException)
			{
				throw (FenixServiceException) ex.getServiceThrownException();
			}
			else
			{
				throw new FenixServiceException(ex);
			}

		}
		catch (ExecutedFilterException ex)
		{
			if (ex.getCause() instanceof FenixServiceException)
			{
				throw (FenixServiceException) ex.getCause();
			}
			else
			{
				throw new FenixServiceException(ex);
			}
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbCreate() throws CreateException
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException
	{
	}

}
