package framework.delegate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;

import pt.utl.ist.berserk.logic.serviceManager.exceptions.FilterChainFailedException;
import ServidorAplicacao.IServiceManagerWrapper;
import ServidorAplicacao.ServiceManagerHome;
import ServidorAplicacao.ServiceManagerLocalHome;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import framework.ejb.EJBHomeFactory;

/**
 * This class is a dynamic proxy implementation of the IServiceManagerWrapper
 * interface.
 * 
 * @author Luis Cruz
 * @deprecated Esta classe deixou de ser necessária... Não trazia nada de novo e
 *             implicava grandes perdas de performance
 * @version
 */

public class DynamicServiceManagerEJBDelegate implements InvocationHandler {

    private static IServiceManagerWrapper serviceManager;

    private static Map serviceManagerMethodMap;

    public DynamicServiceManagerEJBDelegate() {
        System.out.println("*********************************************");
        System.out.println("Initializing DynamicServiceManagerEJBDelegate");
        System.out.println("*********************************************");
        System.out.println("*********************************************");
        init();
    }

    private void init() {
        if (serviceManager != null) {
            System.out.println("*********************************************");
            System.out
                    .println("ServiceManager instance allready resolved... No need to resolv it again");
            System.out.println("*********************************************");
            System.out.println("*********************************************");
            return;
        }

        try {
            //get the local reference to the session bean
            System.out.println("*********************************************");
            System.out.println("Resolving ServiceManagerLocalHome");
            System.out.println("*********************************************");
            System.out.println("*********************************************");
            ServiceManagerLocalHome serviceManagerLocalHome = (ServiceManagerLocalHome) EJBHomeFactory
                    .getInstance().lookupLocalHome(
                            "Fenix/ServidorAplicacao/ServiceManagerLocal",
                            ServiceManagerLocalHome.class);
            System.out.println("*********************************************");
            System.out.println("Using local ejb service manager calls.");
            System.out.println("*********************************************");
            System.out.println("*********************************************");
            serviceManager = serviceManagerLocalHome.create();
        } catch (Exception e) {

            System.out.println("*********************************************");
            e.printStackTrace();
            System.out.println("*********************************************");
            System.out.println("*********************************************");

            try {
                //get the remote reference to the session bean
                System.out
                        .println("*********************************************");
                System.out
                        .println("Couldn't find Local EJB... Resolving remote ServiceManagerHome");
                System.out
                        .println("*********************************************");
                System.out
                        .println("*********************************************");
                ServiceManagerHome serviceManagerHome = (ServiceManagerHome) EJBHomeFactory
                        .getInstance().lookupHome(
                                "Fenix/ServidorAplicacao/ServiceManager",
                                ServiceManagerHome.class);
                System.out
                        .println("*********************************************");
                System.out.println("Using remote ejb service manager calls.");
                System.out
                        .println("*********************************************");
                System.out
                        .println("*********************************************");
                serviceManager = serviceManagerHome.create();
            } catch (Exception e2) {
                System.out
                        .println("*********************************************");
                e2.printStackTrace();
                System.out
                        .println("*********************************************");
                System.out
                        .println("*********************************************");
            }
        }

        if (serviceManager == null)
                try {
                    System.out
                            .println("*********************************************");
                    System.out.println("No ejb's found - trying directly...");
                    System.out
                            .println("*********************************************");
                    System.out
                            .println("*********************************************");
                    Class serviceMagagerBeanClass = Class
                            .forName("ServidorAplicacao.ServiceManagerBean");
                    serviceManager = (IServiceManagerWrapper) serviceMagagerBeanClass
                            .getConstructor(null).newInstance(null);
                    System.out
                            .println("*********************************************");
                    System.out.println("Using local service manager calls.");
                    System.out
                            .println("*********************************************");
                    System.out
                            .println("*********************************************");
                } catch (Exception e1) {
                    serviceManager = null;
                }

        // store the Business Interface methods for later lookups
        serviceManagerMethodMap = new HashMap();
        Method[] serviceManagerMethods = IServiceManagerWrapper.class
                .getMethods();
        for (int i = 0; i < serviceManagerMethods.length; i++) {
            serviceManagerMethodMap.put(serviceManagerMethods[i].getName()
                    + serviceManagerMethods[i].getParameterTypes().length,
                    serviceManagerMethods[i]);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        try {
            Method serviceManagerMethod = (Method) serviceManagerMethodMap
                    .get(method.getName() + args.length);
            if (serviceManagerMethod != null) {
                // call the method on the remote interface
                return serviceManagerMethod.invoke(serviceManager, args);
            } else {
                throw new NoSuchMethodException(
                        "The ServiceManager does not implement "
                                + method.getName());
            }
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof EJBException) {
                // This case is when calls to the ServiceManager are.
                throw e.getTargetException().getCause();
            } else if (e.getTargetException() instanceof RemoteException) {
                // This case is when calls to the ServiceManager are remote.
                if (e.getTargetException().getCause() == null) {
                    throw e.getTargetException();
                } else if (e.getTargetException().getCause().getCause() == null) {
                    throw e.getTargetException().getCause();
                } else if (e.getTargetException().getCause().getCause()
                        .getCause() == null) {
                    throw e.getTargetException().getCause().getCause();
                } else {
                    throw e.getTargetException().getCause().getCause()
                            .getCause();
                }
            } else if (e.getTargetException().getCause() instanceof FilterChainFailedException) {
                throw new NotAuthorizedException();
            } else {
                throw e.getTargetException();
            }
        }
    }

}