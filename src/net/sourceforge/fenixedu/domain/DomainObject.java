/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.lang.reflect.Proxy;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.ojb.broker.core.proxy.ProxyHelper;

/**
 * @author jpvl
 */
public abstract class DomainObject extends DomainObject_Base {

    // This variable was created so that locking of domain objects can be
    // disabled for writting test cases. Testing domain code can be done
    // without persisting anything.
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
                PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentObject().lockWrite(obj);
            } catch (Exception e) {
                throw new Error("Couldn't obtain lockwrite on object", e);
            }
        }
    }

    public DomainObject() {
        super();

        // All domain objects become persistent upon there creation.
        // Locking the object will also set the idInternal which will be
        // used by both the hashcode and the equals methods
        doLockWriteOn(this);
    }

    public final int hashCode() {
        if (getIdInternal() != null) {
            return getIdInternal().intValue();
        }

        throw new RuntimeException("Domain object idInternal not set!");
    }

    public final boolean equals(Object obj) {
        if (obj != null && obj instanceof IDomainObject) {
            IDomainObject domainObject = (IDomainObject) obj;
            if (domainObject instanceof Proxy) {
                domainObject = (IDomainObject) ProxyHelper.getRealObject(domainObject);
            }

            if (this.getClass() == domainObject.getClass() && getIdInternal() != null
                    && getIdInternal().equals(domainObject.getIdInternal())) {
                return true;
            }
        }
        return false;
    }

    protected void delete() {
        try {
            PersistenceSupportFactory.getDefaultPersistenceSupport()
                    .getIPersistentObject().deleteByOID(this.getClass(), getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            throw new Error("Couldn't delete object", e);
        }
    }

}
