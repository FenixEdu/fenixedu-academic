/*
 * Created on Oct 17, 2003
 *  
 */
package framework.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import ServidorAplicacao.IServiceManagerWrapper;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.logging.SystemInfo;
import framework.delegate.DynamicServiceManagerEJBDelegate;

/**
 * @author Luis Cruz
 *  
 */
public class ServiceManagerServiceFactory
{

	public ServiceManagerServiceFactory()
	{
		super();
	}

	public IServiceManagerWrapper createService()
	{
		Class[] serviceInterface = new Class[] { IServiceManagerWrapper.class };
		IServiceManagerWrapper proxy =
			(IServiceManagerWrapper) Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				serviceInterface,
				new DynamicServiceManagerEJBDelegate());
		return proxy;
	}

	public static Object executeService(IUserView userView, String serviceName, Object[] serviceArgs)
		throws FenixServiceException
	{
		Object[] methodArgs = { userView, serviceName, serviceArgs };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("execute") && (methods[i].getParameterTypes().length == 3))
			{
				method = methods[i];
			}
		}

		return execute(method, methodArgs);
	}

	public static Map getServicesLogInfo(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("getMapServicesToWatch")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		return (Map) execute(method, methodArgs);
	}

	public static Map getUsersLogInfo(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("getMapUsersToWatch")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		return (Map) execute(method, methodArgs);
	}

	public static Boolean serviceLoggingIsOn(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("serviceLoggingIsOn")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		return (Boolean) execute(method, methodArgs);
	}

	public static Boolean userLoggingIsOn(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("userLoggingIsOn")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		return (Boolean) execute(method, methodArgs);
	}

	public static SystemInfo getSystemInfo(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("getSystemInfo")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		return (SystemInfo) execute(method, methodArgs);
	}

	public static void turnServiceLoggingOn(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("turnServiceLoggingOn")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		execute(method, methodArgs);
	}

	public static void turnUserLoggingOn(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("turnUserLoggingOn")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		execute(method, methodArgs);
	}

	public static void turnServiceLoggingOff(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("turnServiceLoggingOff")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		execute(method, methodArgs);
	}

	public static void turnUserLoggingOff(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("turnUserLoggingOff")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		execute(method, methodArgs);
	}

	public static void clearServiceLogHistory(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("clearServiceLogHistory")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		execute(method, methodArgs);
	}

	public static void clearUserLogHistory(IUserView userView) throws FenixServiceException
	{
		Object[] methodArgs = { userView };
		Method method = null;
		Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equals("clearUserLogHistory")
				&& (methods[i].getParameterTypes().length == 1))
			{
				method = methods[i];
			}
		}

		execute(method, methodArgs);
	}

	private static Object execute(Method method, Object[] methodArgs)
	//private static Object execute(IUserView userView, String serviceName, Object[] serviceArgs)
	throws FenixServiceException
	{
		IServiceManagerWrapper serviceManager = new ServiceManagerServiceFactory().createService();
		DynamicServiceManagerEJBDelegate delegate = new DynamicServiceManagerEJBDelegate();
		try
		{
			return delegate.invoke(serviceManager, method, methodArgs);
		}
		catch (SecurityException e)
		{
			//e.printStackTrace();
			throw new FenixServiceException(e);
		}
		catch (NoSuchMethodException e)
		{
			//e.printStackTrace();
			throw new FenixServiceException(e);
		}
		catch (Throwable e)
		{
			if (e instanceof FenixServiceException)
			{
				throw (FenixServiceException) e;
			}
			else
			{
				throw new FenixServiceException(e);
			}
		}
	}

}
