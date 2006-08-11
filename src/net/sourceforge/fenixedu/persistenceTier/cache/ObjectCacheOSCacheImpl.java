/*
 * Created on Dec 27, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.cache;

import java.util.Properties;

import net.sourceforge.fenixedu.persistenceTier.cache.logging.CacheLog;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.cache.ObjectCache;
import org.apache.ojb.broker.cache.RuntimeCacheException;

import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author http://db.apache.org/ojb/howto-work-with-clustering.html
 *  
 */
public class ObjectCacheOSCacheImpl implements ObjectCache {
    private static GeneralCacheAdministrator admin = new GeneralCacheAdministrator();

    private static final int NO_REFRESH = CacheEntry.INDEFINITE_EXPIRY;

    private static int numberOfCachedItems = 0;

    public ObjectCacheOSCacheImpl() {
    }

    public ObjectCacheOSCacheImpl(PersistenceBroker broker) {
    }

    public ObjectCacheOSCacheImpl(PersistenceBroker arg0, Properties props) {
    }

    public void cache(Identity oid, Object obj) {
        try {
            this.removeForInsert(oid);
            admin.putInCache(oid.toString(), obj);
            CacheLog.incrementPuts();
            numberOfCachedItems++;
        } catch (Exception e) {
            throw new RuntimeCacheException(e.getMessage());
        }
    }

    public Object lookup(Identity oid) {
        try {
            Object result = admin.getFromCache(oid.toString(), NO_REFRESH);
            CacheLog.incrementSuccessfulLookUps();
            return result;
        } catch (Exception e) {
            CacheLog.incrementFailedLookUps();
            admin.cancelUpdate(oid.toString());
            return null;
        }
    }

    public void remove(Identity oid) {
        try {
            admin.flushEntry(oid.toString());
            CacheLog.incrementRemoves();
            numberOfCachedItems--;
        } catch (Exception e) {
            throw new RuntimeCacheException(e.getMessage());
        }
    }

    private void removeForInsert(Identity oid) {
        try {
            admin.flushEntry(oid.toString());
            CacheLog.incrementRemoves();
        } catch (Exception e) {
            throw new RuntimeCacheException(e.getMessage());
        }
    }

    public void clear() {
        if (admin != null) {
            try {
                admin.flushAll();
                CacheLog.incrementClears();
                numberOfCachedItems = 0;
            } catch (Exception e) {
                throw new RuntimeCacheException(e);
            }
        }
    }

    public static int getNumberOfCachedItems() {
        return numberOfCachedItems;
    }

}