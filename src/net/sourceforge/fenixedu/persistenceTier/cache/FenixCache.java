package net.sourceforge.fenixedu.persistenceTier.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.cache.logging.CacheLog;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.cache.ObjectCache;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;



public class FenixCache implements ObjectCache {

    private static int numberOfCachedItems = 0;

    private static final ReferenceQueue refQueue = new ReferenceQueue();

    private DescriptorRepository repository;
    private ConcurrentHashMap<Class,ConcurrentHashMap<Integer,CacheEntry>> cache;


    public FenixCache() {
	this.repository = MetadataManager.getInstance().getRepository();
	this.cache = new ConcurrentHashMap<Class,ConcurrentHashMap<Integer,CacheEntry>>();
    }


    public void cache(Identity oid, Object obj) {
	// ignore OJB's request to cache object
	// we use a different method called immediately after object instantiation
    }


    public Object cache(DomainObject obj) {
	processQueue();
	Class objTopClass = repository.getTopLevelClass(obj.getClass());
	ConcurrentHashMap<Integer,CacheEntry> classCache = getCacheForClass(objTopClass);

	Integer key = obj.getIdInternal();
	CacheEntry newEntry = new CacheEntry(obj, key, refQueue);

	return cacheNewEntry(classCache, newEntry, obj);
    }

    private Object cacheNewEntry(ConcurrentHashMap<Integer,CacheEntry> classCache, CacheEntry newEntry, Object obj) {
	CacheEntry entryInCache = putIfAbsent(classCache, newEntry.key, newEntry);

	if (entryInCache == newEntry) {
	    newEntry.cache = classCache;
            CacheLog.incrementPuts();
            numberOfCachedItems++;
	    return obj;
	} else {
	    Object objInCache = entryInCache.get();
	    if (objInCache != null) {
		return objInCache;
	    } else {
		// the entry in cache was GCed already, so remove it and retry
		removeEntry(classCache, entryInCache);
		return cacheNewEntry(classCache, newEntry, obj);
	    }
	}
    }


    public Object lookup(Identity oid) {
        return lookup(oid.getObjectsTopLevelClass(), (Integer)oid.getPrimaryKeyValues()[0]);
    }

    public Object lookup(Class topLevelClass, Integer id) {
	processQueue();
	ConcurrentHashMap<Integer,CacheEntry> classCache = getCacheForClass(topLevelClass);
	CacheEntry entry = classCache.get(id);

	if (entry != null) {
	    Object result = entry.get();
	    if (result != null) {
		CacheLog.incrementSuccessfulLookUps();
		return result;
	    } else {
		removeEntry(classCache, entry);
		CacheLog.incrementFailedLookUps();
		return null;
	    }
        } else {
            CacheLog.incrementFailedLookUps();
	    return null;
        }
    }

    private static void removeEntry(ConcurrentHashMap<Integer,CacheEntry> cache, CacheEntry entry) {
	if (cache.remove(entry.key, entry)) {
            CacheLog.incrementRemoves();
            numberOfCachedItems--;
	}
    }


    public void remove(Identity oid) {
	// does this method makes any sense in a cache???

	ConcurrentHashMap<Integer,CacheEntry> classCache = getCacheForClass(oid.getObjectsTopLevelClass());
	CacheEntry entry = classCache.get((Integer)oid.getPrimaryKeyValues()[0]);
	
	if (entry != null) {
	    removeEntry(classCache, entry);
        }
    }

    public void clear() {
	// I'm not sure if this is concurrency-safe...

	for (Map classCache : cache.values()) {
	    classCache.clear();
	}
	CacheLog.incrementClears();
	numberOfCachedItems = 0;
    }

    private ConcurrentHashMap<Integer,CacheEntry> getCacheForClass(Class topLevelClass) {
	ConcurrentHashMap<Integer,CacheEntry> classCache = cache.get(topLevelClass);

	if (classCache == null) {
	    classCache = putIfAbsent(cache, topLevelClass, new ConcurrentHashMap<Integer,CacheEntry>());
	}
	
	return classCache;
    }

    private static <K,V> V putIfAbsent(ConcurrentHashMap<K,V> map, K key, V value) {
	// As of the Java(TM) 2 Runtime Environment, Standard Edition (build 1.5.0_03-b07)
	// the ConcurrentHashMap implementation of the putIfAbsent method does not conform to 
	// the specification in the documentation.
	// Instead, it always returns the old-value, which, if the key was not in the map is null
	
	V oldValue = map.putIfAbsent(key, value);
	return ((oldValue == null) ? value : oldValue);
    }


    public static int getNumberOfCachedItems() {
        return numberOfCachedItems;
    }


    private void processQueue() {
        CacheEntry gcedEntry = (CacheEntry)refQueue.poll();
        while (gcedEntry != null) {
	    if (gcedEntry.cache != null) {
		removeEntry(gcedEntry.cache, gcedEntry);
	    }
	    gcedEntry = (CacheEntry)refQueue.poll();
        }
    }


    private static class CacheEntry extends SoftReference {
	private final Integer key;
	private ConcurrentHashMap<Integer,CacheEntry> cache;

        CacheEntry(Object object, Integer key, ReferenceQueue q) {
            super(object, q);
	    this.key = key;
        }
    }
}
