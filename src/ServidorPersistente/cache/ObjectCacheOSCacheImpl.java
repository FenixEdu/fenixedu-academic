/*
 * Created on Dec 27, 2003
 *
 */
package ServidorPersistente.cache;

	

import java.util.Properties;

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
public class ObjectCacheOSCacheImpl implements ObjectCache
{
	private static GeneralCacheAdministrator admin = new GeneralCacheAdministrator();
	private static final int NO_REFRESH = CacheEntry.INDEFINITE_EXPIRY;

	public ObjectCacheOSCacheImpl() {
	}

	public ObjectCacheOSCacheImpl(PersistenceBroker broker) {
	}

	public ObjectCacheOSCacheImpl(PersistenceBroker arg0, Properties props)
	{
	}

	public void cache(Identity oid, Object obj) {
	  try {
		this.remove(oid);
		admin.putInCache(oid.toString(), obj);
	  }
	  catch (Exception e) {
		throw new RuntimeCacheException(e.getMessage());
	  }
	}

	public Object lookup(Identity oid) {
	  try {
		return admin.getFromCache(oid.toString(), NO_REFRESH);
	  }
	  catch (Exception e) {
		admin.cancelUpdate(oid.toString());
		return null;
	  }
	}

	public void remove(Identity oid) {
	  try {
		admin.flushEntry(oid.toString());
	  }
	  catch (Exception e) {
		throw new RuntimeCacheException(e.getMessage());
	  }
	}

	public void clear() {
	  if (admin != null) {
		try {
		  admin.flushAll();
		}
		catch (Exception e) {
		  throw new RuntimeCacheException(e);
		}
	  }
	}
}
