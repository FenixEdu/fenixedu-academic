/*
 * Created on Aug 8, 2003
 *  
 */
package ServidorPersistente.cache;

import java.util.Properties;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.cache.ObjectCache;
import org.apache.ojb.broker.cache.ObjectCacheDefaultImpl;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class FenixObjectCacheDefaultImpl extends ObjectCacheDefaultImpl implements ObjectCache {

    /**
     * @param arg0
     */
    public FenixObjectCacheDefaultImpl(PersistenceBroker arg0, Properties props) {
        super(arg0, props);
    }

    public Integer getNumberOfCachedObjects() {
        Integer numberCachedObjects = null;

        if (objectTable != null) {
            numberCachedObjects = new Integer(objectTable.size());
        } else {
            numberCachedObjects = new Integer(0);
        }

        return numberCachedObjects;
    }

}