package ServidorAplicacao;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.commons.collections.FastHashMap;

import pt.utl.ist.berserk.logic.serviceManager.IServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.ServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.ExecutedFilterException;
import pt.utl.ist.berserk.logic.serviceManager.exceptions.ExecutedServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.logging.ServiceExecutionLog;
import ServidorAplicacao.logging.SystemInfo;
import ServidorAplicacao.logging.UserExecutionLog;

/**
 * This class is the entry point of the system to execute a service. It receives the service to execute,
 * its arguments and an identificator of the entity that wants to run the service.
 * 
 * @author Luis Cruz
 * @version
 */

public class ServiceManagerBean implements SessionBean, IServiceManagerWrapper
{

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
			Calendar serviceStartTime = null;
			Calendar serviceEndTime = null;

			IServiceManager manager = ServiceManager.getInstance();
			if (serviceLoggingIsOn || (userLoggingIsOn && id != null))
			{
				serviceStartTime = Calendar.getInstance();
			}
			Object serviceResult = manager.execute(id, service, method, args);
			if (serviceLoggingIsOn || (userLoggingIsOn && id != null))
			{
				serviceEndTime = Calendar.getInstance();
			}
			if (serviceLoggingIsOn)
			{
				registerServiceExecutionTime(service, method, args, serviceStartTime, serviceEndTime);
			}
			if (userLoggingIsOn && id != null)
			{
				registerUserExecutionOfService(id, service, method, args, serviceStartTime, serviceEndTime);
			}

			return serviceResult;
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

	public synchronized void turnServiceLoggingOn(IUserView id)
	{
		serviceLoggingIsOn = true;
	}

	public synchronized void turnServiceLoggingOff(IUserView id)
	{
		serviceLoggingIsOn = false;
	}

	public synchronized void clearServiceLogHistory(IUserView id)
	{
		mapServicesToWatch.clear();
	}

	public synchronized void turnUserLoggingOn(IUserView id)
	{
		userLoggingIsOn = true;
	}

	public synchronized void turnUserLoggingOff(IUserView id)
	{
		userLoggingIsOn = false;
	}

	public synchronized void clearUserLogHistory(IUserView id)
	{
		mapUsersToWatch.clear();
	}

	/**
	 * @param service
	 * @param method
	 * @param args
	 * @param serviceStartTime
	 * @param serviceEndTime
	 */
	private void registerServiceExecutionTime(
		String service,
		String method,
		Object[] args,
		Calendar serviceStartTime,
		Calendar serviceEndTime)
	{
		String hashKey = generateServiceHashKey(service, method, args);
		long serviceExecutionTime = calculateServiceExecutionTime(serviceStartTime, serviceEndTime);
		ServiceExecutionLog serviceExecutionLog = (ServiceExecutionLog) mapServicesToWatch.get(hashKey);
		if (serviceExecutionLog == null)
		{
			serviceExecutionLog = new ServiceExecutionLog(hashKey);
			mapServicesToWatch.put(hashKey, serviceExecutionLog);
		}

		serviceExecutionLog.addExecutionTime(serviceExecutionTime);
	}

	/**
	 * @param id
	 * @param service
	 * @param method
	 * @param args
	 * @param serviceStartTime
	 * @param serviceEndTime
	 */
	private void registerUserExecutionOfService(IUserView id, String service, String method, Object[] args, Calendar serviceStartTime, Calendar serviceEndTime)
	{
		UserExecutionLog userExecutionLog = (UserExecutionLog) mapUsersToWatch.get(id.getUtilizador());
		if (userExecutionLog == null)
		{
			userExecutionLog = new UserExecutionLog(id);
			mapUsersToWatch.put(id.getUtilizador(), userExecutionLog);
		}

		userExecutionLog.addServiceCall(generateServiceHashKey(service, method, args), serviceStartTime);
	}

	/**
	 * @param serviceStartTime
	 * @param serviceEndTime
	 * @return
	 */
	private long calculateServiceExecutionTime(Calendar serviceStartTime, Calendar serviceEndTime)
	{
		return serviceEndTime.getTimeInMillis() - serviceStartTime.getTimeInMillis();
	}

	/**
	 * @param service
	 * @param method
	 * @param args
	 * @return
	 */
	private String generateServiceHashKey(String service, String method, Object[] args)
	{
		String hashKey = service + "." + method + "(";
		if (args != null)
		{
			for (int i = 0; i < args.length; i++)
			{
				hashKey += args[i].getClass().getName();
				if (i + 1 < args.length)
				{
					hashKey += ", ";
				}
			}
		}
		hashKey += ")";
		return hashKey;
	}

	/**
	 * @return Returns the mapServicesToWatch.
	 */
	public HashMap getMapServicesToWatch(IUserView id)
	{
		return mapServicesToWatch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServiceManagerWrapper#loggingIsOn()
	 */
	public Boolean serviceLoggingIsOn(IUserView id)
	{
		return new Boolean(serviceLoggingIsOn);
	}

	/**
	 * @return Returns the mapUsersToWatch.
	 */
	public HashMap getMapUsersToWatch(IUserView id)
	{
		return mapUsersToWatch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServiceManagerWrapper#loggingIsOn()
	 */
	public Boolean userLoggingIsOn(IUserView id)
	{
		return new Boolean(userLoggingIsOn);
	}

	public SystemInfo getSystemInfo(IUserView id) {
		return new SystemInfo();
	}

}
