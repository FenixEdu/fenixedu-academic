package framework.delegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.exceptions.FilterChainFailedException;
import ServidorAplicacao.IServiceManagerWrapper;
import ServidorAplicacao.ServiceManagerHome;
import ServidorAplicacao.Servico.exceptions.FenixRemoteServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import framework.ejb.EJBHomeFactory;

/**
 * This class is a dynamic proxy implementation of the IServiceManagerWrapper interface.
 * 
 * @author Luis Cruz
 * @version
 */

public class DynamicServiceManagerEJBDelegate implements InvocationHandler
{

	private static IServiceManagerWrapper serviceManager;
	private static Map serviceManagerMethodMap;

	public DynamicServiceManagerEJBDelegate()
	{
		//init();
	}

	static {
		try
		{
			try
			{
				Class serviceMagagerBeanClass = Class.forName("ServidorAplicacao.ServiceManagerBean");
				serviceManager =
					(IServiceManagerWrapper) serviceMagagerBeanClass.getConstructor(null).newInstance(
						null);
				System.out.println("Using local service manager calls.");
			}
			catch (Exception e1)
			{
				serviceManager = null;
			}

			if (serviceManager == null)
			{
				// get the remote reference to the session bean
				ServiceManagerHome serviceManagerHome =
					(ServiceManagerHome) EJBHomeFactory.getInstance().lookupHome(
						"ServidorAplicacao.ServiceManager",
						ServiceManagerHome.class);
				System.out.println("Using remote service manager calls.");
				serviceManager = serviceManagerHome.create();
			}

			// store the Business Interface methods for later lookups
			serviceManagerMethodMap = new HashMap();
			Method[] serviceManagerMethods = IServiceManagerWrapper.class.getMethods();
			for (int i = 0; i < serviceManagerMethods.length; i++)
			{
				serviceManagerMethodMap.put(
					serviceManagerMethods[i].getName()
						+ serviceManagerMethods[i].getParameterTypes().length,
					serviceManagerMethods[i]);
			}
		}
		catch (NamingException e)
		{
			throw new RuntimeException(e.getMessage());
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e.getMessage());
		}
		catch (CreateException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method,
	 *      java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		try
		{
			Method serviceManagerMethod =
				(Method) serviceManagerMethodMap.get(method.getName() + args.length);
			if (serviceManagerMethod != null)
			{
				// call the method on the remote interface
				return serviceManagerMethod.invoke(serviceManager, args);
			}
			else
			{
				throw new NoSuchMethodException(
					"The ServiceManager does not implement " + method.getName());
			}
		}
		catch (InvocationTargetException e)
		{
			if (e.getTargetException() instanceof RemoteException) {
//				System.out.println("DynamicServiceManagerEJBDelegate - RemoteException");
//				System.out.println("DynamicServiceManagerEJBDelegate - class= " + e.getTargetException().getClass().getName());
//				System.out.println("DynamicServiceManagerEJBDelegate - cause= " + e.getTargetException().getCause().getClass().getName());
//				System.out.println("DynamicServiceManagerEJBDelegate - cause= " + e.getTargetException().getCause().getCause().getClass().getName());
//				System.out.println("DynamicServiceManagerEJBDelegate - cause= " + e.getTargetException().getCause().getCause().getCause().getClass().getName());
				if (e.getTargetException().getCause() == null)
				{
					throw e.getTargetException();
				}
				else if (e.getTargetException().getCause().getCause() == null)
				{
					throw e.getTargetException().getCause();
				}
				else if (e.getTargetException().getCause().getCause().getCause() == null)
				{
					throw e.getTargetException().getCause().getCause();
				}
				else {
					throw e.getTargetException().getCause().getCause().getCause();
				}
			}
			else if (e.getTargetException().getCause() instanceof FilterChainFailedException)
			{
				throw new NotAuthorizedException();
			}
			else
			{
				throw e.getTargetException();
			}
		}
	}

}