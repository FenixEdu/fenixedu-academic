/*
 * Created on Jan 3, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.util;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.IDomainObject;

import org.apache.ojb.broker.Identity;

/**
 * @author Luis Cruz
 *  
 */
public class InfoObjectCache {

    private static Map infoObjectTable;

    static {
        infoObjectTable = new HashMap();
        //infoObjectTable.setFast(true);
    }

    public static String getKey(Identity identity) {
        String key = identity.getObjectsRealClass().getName() + identity.getPrimaryKeyValues()[0];
        return key;
    }

    public static String getKey(IDomainObject domainObject) {
        String key = domainObject.getClass().getName() + domainObject.getIdInternal();
        return key;
    }

    public static void cache(String key, InfoObject value) {
        remove(key);
        infoObjectTable.put(key, value);
    }

    public static InfoObject lookup(String key) {
        return (InfoObject) infoObjectTable.get(key);
    }

    public static void remove(String key) {
        infoObjectTable.remove(key);
    }

    public static void clear() {
        infoObjectTable.clear();
    }

}