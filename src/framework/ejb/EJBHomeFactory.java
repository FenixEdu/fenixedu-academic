/*
 * Created on Oct 17, 2003
 *  
 */
package framework.ejb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * @author Luis Cruz
 * @author José Pedro Pereira
 * 
 * This class implements the EJBHomeFactory pattern. It performs JNDI lookups to
 * locate EJB home or local home and cache the results for subsequent calls.
 */
public class EJBHomeFactory {

    private Map homes;

    private static EJBHomeFactory homeFactory;

    private Context context;

    /**
     *  
     */
    public EJBHomeFactory() throws NamingException {
        homes = Collections.synchronizedMap(new HashMap());
        try {
            // load the properties file from the classpath root
            InputStream inputStream = getClass().getResourceAsStream(
                    "/jndi.properties");
            if (inputStream != null) {
                Properties jndiParams = new Properties();
                jndiParams.load(inputStream);

                Hashtable properties = new Hashtable();
                properties.put(Context.INITIAL_CONTEXT_FACTORY, jndiParams
                        .getProperty(Context.INITIAL_CONTEXT_FACTORY));
                //properties.put(Context.PROVIDER_URL,
                // jndiParams.getProperty(Context.PROVIDER_URL));
                properties.put(Context.PROVIDER_URL, "localhost:1099");
                properties.put(Context.URL_PKG_PREFIXES,
                        "jboss.naming:org.jnp.interfaces");
                context = new InitialContext(properties);
            } else {
                // use default provider
                context = new InitialContext();
            }
        } catch (IOException ex) {
            // use default provider
            context = new InitialContext();
        }
    }

    public static EJBHomeFactory getInstance() throws NamingException {
        if (homeFactory == null) {
            homeFactory = new EJBHomeFactory();
        }

        return homeFactory;
    }

    public EJBHome lookupHome(String jndiName, Class homeClass)
            throws ClassCastException, NamingException {
        EJBHome home = (EJBHome) homes.get(homeClass);
        if (home == null) {
            home = (EJBHome) PortableRemoteObject.narrow(context
                    .lookup(jndiName), homeClass);
            // cache the home for repeated use
            homes.put(homeClass, home);
        }

        return home;
    }

    public EJBLocalHome lookupLocalHome(String jndiName, Class homeClass)
            throws ClassCastException, NamingException {
        EJBLocalHome home = (EJBLocalHome) homes.get(homeClass);
        if (home == null) {
            home = (EJBLocalHome) PortableRemoteObject.narrow(context
                    .lookup(jndiName), homeClass);
            // cache the home for repeated use
            homes.put(homeClass, home);
        }

        return home;
    }

}