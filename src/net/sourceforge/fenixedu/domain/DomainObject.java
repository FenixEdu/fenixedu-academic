/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author jpvl
 */

public class DomainObject extends DomainObject_Base {

    private static boolean lockMode = true;

    public static void turnOffLockMode() {
        lockMode = false;
    }
    public static void turnOnLockMode() {
        lockMode = true;
    }

    protected static void doLockWriteOn(Object obj) {
        if (lockMode) {
            try {
                net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB.getInstance()
                        .lockWrite(obj);
            } catch (Exception e) {
                throw new Error("Couldn't obtain lockwrite on object");
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof IDomainObject) {
            IDomainObject domainObject = (IDomainObject) obj;
            if (getIdInternal() != null && domainObject.getIdInternal() != null
                    && getIdInternal().equals(domainObject.getIdInternal())) {

                Collection thisInterfaces = getInterfaces(getClass());
                Collection objInterfaces = getInterfaces(domainObject.getClass());

                thisInterfaces = CollectionUtils.select(thisInterfaces, new IS_NOT_IDOMAIN_PREDICATE());
                objInterfaces = CollectionUtils.select(objInterfaces, new IS_NOT_IDOMAIN_PREDICATE());

                if (!CollectionUtils.intersection(thisInterfaces, objInterfaces).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Class> getInterfaces(Class thisClass) {

        List<Class> result = new ArrayList<Class>();

        Class innerClass = thisClass;
        while (innerClass != null && !innerClass.equals(DomainObject_Base.class)
                && !innerClass.equals(DomainObject.class)) {
            if (innerClass.getInterfaces().length != 0) {
                result.addAll(Arrays.asList(innerClass.getInterfaces()));
            }
            innerClass = innerClass.getSuperclass();
        }

        return result;
    }

    public int hashCode() {
        if (getIdInternal() != null) {
            return getIdInternal().intValue();
        }
        return super.hashCode();
    }

    public class IS_NOT_IDOMAIN_PREDICATE implements Predicate {

        public boolean evaluate(Object arg0) {
            Class class1 = (Class) arg0;
            if (class1.getName().equals(IDomainObject.class.getName())) {
                return false;
            }
            return true;

        }
    }

}
