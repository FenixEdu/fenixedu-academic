package framework.delegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
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

			if (e.getTargetException() instanceof FenixRemoteServiceException)
			{
				FenixRemoteServiceException fenixRemoteServiceException =
					(FenixRemoteServiceException) e.getTargetException();

				Throwable originalException = recreateOriginalException(fenixRemoteServiceException);
				if (originalException != null)
				{
					throw originalException;
				}
				else
				{
					throw new RuntimeException(
						"Unable to recreate original exception: "
							+ fenixRemoteServiceException.getCauseClassName()
							+ " on the client side.",
						fenixRemoteServiceException);
				}
			}
			else if (e.getTargetException().getCause() instanceof FilterChainFailedException)
			{
				throw new NotAuthorizedException();
			}
			else if (e.getTargetException() instanceof RemoteException)
			{
				// Remote exception isn't declared by the
				// IServiceManagerWrapper method that was called,
				// so we have to catch it and throw something that is
				System.out.println("########!!!!!!!!!!!!! N DEVIA ESTAR AQUI !!!!!!!!!!##########");
				throw new FenixServiceException(e.getTargetException());
			}
			else
			{
				throw e.getTargetException();
			}
		}
	}

	/**
	 * @param exception
	 * @return
	 */
	private Throwable recreateOriginalException(FenixRemoteServiceException exception)
	{
		Throwable originalException = null;
		try
		{
			String[] classNames = StringUtils.split(exception.getCauseClassName(), "$");
			Class originalHostExceptionClass = null;
			Object originalHostException = null;
			if (classNames.length > 2)
			{
				System.out.println("Unable to recreated orignial exception. :(");
				System.out.println("The original exception is nested inside a nested object.");
				System.out.println(
					"Inorder for this exception to be recreated this code must be changed.");
				/*
				 * To do so, iterate this if/else block This solution was not yet implemented because it
				 * is very unlikely and doing so might bring about a bug or two, so for we decided to
				 * just keep it simple.
				 *  
				 */
				return null;
			}
			else if (classNames.length == 2)
			{
				originalHostExceptionClass = Class.forName(classNames[0]);
				originalHostException =
					originalHostExceptionClass.getConstructor(null).newInstance(null);
				//System.out.println("Sucessfully recreated orignial host of exception. :)");

				Class originalExceptionClass = Class.forName(exception.getCauseClassName());
				Constructor[] constructors = originalExceptionClass.getConstructors();
				originalException =
					(Throwable) originalExceptionClass.getConstructor(
						constructors[0].getParameterTypes()).newInstance(
						new Object[] { originalHostException });
			}
			else
			{
				Class originalExceptionClass = Class.forName(exception.getCauseClassName());
				originalException =
					(Throwable) originalExceptionClass.getConstructor(null).newInstance(null);
			}

			//System.out.println("Sucessfully recreated orignial exception. :)");
		}
		catch (Exception e1)
		{
			System.out.println("Unable to recreated orignial exception. :(");
			originalException = null;
		}

		return originalException;
	}
}