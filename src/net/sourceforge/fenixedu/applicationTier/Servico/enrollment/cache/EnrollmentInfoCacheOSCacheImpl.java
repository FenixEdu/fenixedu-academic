/*
 * Created on 2004/07/02
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author Luis Cruz
 *  
 */
public class EnrollmentInfoCacheOSCacheImpl {

    private static EnrollmentInfoCacheOSCacheImpl instance = null;

    private static GeneralCacheAdministrator admin = null;

    private int refreshTimeout = 300;

    private int numberOfCachedItems = 0;

    private static int[] objectSynchCreateInstance = new int[0];

    private void init() {
        String propertiesFileName = "/enrollmentOscache.properties";
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

    private EnrollmentInfoCacheOSCacheImpl() {
        super();
        init();
    }

    public static EnrollmentInfoCacheOSCacheImpl getInstance() {
        synchronized (objectSynchCreateInstance) {
            if (instance == null) {
                instance = new EnrollmentInfoCacheOSCacheImpl();
            }
            if (admin == null) {
                instance.init();
            }
        }

        return instance;
    }

    public void cache(String key, Object object) {
        try {
            if (lookup(key) == null) {
                numberOfCachedItems++;
            }
            admin.flushEntry(key);
            admin.putInCache(key, object);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object lookup(String key) {
        try {
            return admin.getFromCache(key, refreshTimeout);
        } catch (Exception e) {
            admin.cancelUpdate(key);
            return null;
        }
    }

    public void remove(String key) {
        try {
            admin.flushEntry(key);
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