/*
 * Created on Oct 17, 2003
 *  
 */
package framework.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import ServidorAplicacao.IServiceManagerWrapper;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import framework.delegate.DynamicServiceManagerEJBDelegate;

/**
 * @author Luis Cruz
 *  
 */
public class ServiceManagerServiceFactory
{

	public ServiceManagerServiceFactory()
	{
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
		IServiceManagerWrapper serviceManager = new ServiceManagerServiceFactory().createService();
		DynamicServiceManagerEJBDelegate delegate = new DynamicServiceManagerEJBDelegate();
		try
		{
			Object[] serviceManagerArgs = { userView, serviceName, serviceArgs };

			Method method = null;
			Method[] methods = IServiceManagerWrapper.class.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++)
			{
				if (methods[i].getName().equals("execute")
					&& (methods[i].getParameterTypes().length == 3))
				{
					method = methods[i];
				}
			}

			return delegate.invoke(serviceManager,
			/* IServiceManagerWrapper.class.getMethod("executar", paramTypes) */
			method, serviceManagerArgs);
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
