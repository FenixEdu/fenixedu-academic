/*
 * Created on 2004/07/02
 *  
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.web.filter.ResponseContent;

/**
 * @author Luis Cruz
 *  
 */
public class ResponseCacheOSCacheImpl {
    private static ResponseCacheOSCacheImpl instance = null;

    private static GeneralCacheAdministrator admin = null;

    private int refreshTimeout = 300;

    private int numberOfCachedItems = 0;

    // The purpose of this variable is to synchronize access to the constructor
    // of this class, in the getInstance method.
    // Creating an int[0] is usually faster than creating an Object.
    private static int[] objectSynchCreateInstance = new int[0];

    private void init() {
        String propertiesFileName = "/responseOscache.properties";
        InputStream inputStream = getClass().getResourceAsStream(propertiesFileName);
        if (inputStream != null) {
            Properties properties = new Properties();
            try {
                properties.load(inputStream);
                admin = new GeneralCacheAdministrator(properties);
            } catch (IOException e) {
                throw new RuntimeException("Failled to load properties for response cache.", e);
            }
        }
    }

    private ResponseCacheOSCacheImpl() {
        super();
        init();
    }

    public static ResponseCacheOSCacheImpl getInstance() {
        synchronized (objectSynchCreateInstance) {
            if (instance == null) {
                instance = new ResponseCacheOSCacheImpl();
            }
            if (admin == null) {
                instance.init();
            }
        }

        return instance;
    }

    public void cache(String id, ResponseContent response) {
        try {
            if (lookup(id) == null) {
                numberOfCachedItems++;
            }
            admin.flushEntry(id);
            admin.putInCache(id, response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseContent lookup(String id) {
        try {
            return (ResponseContent) admin.getFromCache(id, refreshTimeout);
        } catch (Exception e) {
            admin.cancelUpdate(id);
            return null;
        }
    }

    public void remove(String id) {
        try {
            admin.flushEntry(id);
            numberOfCachedItems--;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {
        if (admin != null) {
            try {
                admin.flushAll();
                numberOfCachedItems = 0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getNumberOfCachedItems() {
        return instance.numberOfCachedItems;
    }

    public int getRefreshTimeout() {
        return refreshTimeout;
    }

    public void setRefreshTimeout(int refreshTimeout) {
        this.refreshTimeout = refreshTimeout;
    }

}