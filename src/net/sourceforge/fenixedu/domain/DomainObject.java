/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.lang.reflect.Field;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SequenceUtil;
import net.sourceforge.fenixedu.stm.InvalidateSubject;
import net.sourceforge.fenixedu.stm.Transaction;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.ClassDescriptor;

/**
 * @author jpvl
 */
public abstract class DomainObject extends DomainObject_Base {

    // This variable was created so that locking of domain objects can be
    // disabled for writting test cases. Testing domain code can be done
    // without persisting anything.
    private static boolean lockMode = true;

    // This variable was created so that for a predetermined set of data
    // the next generated ID's will always be the same. This is usefull 
    // for example for acceptance tests where the same id's must always
    // be generated.
    private static boolean autoDetermineId = false;

    private static int nextIdInternal =  1; 

    public static void turnOffLockMode() {
        lockMode = false;
    }

    public static void turnOnLockMode() {
        lockMode = true;
    }

    public static int autoDetermineId() {
        autoDetermineId = true;
        return nextIdInternal = SequenceUtil.findMaxID() + 1;
    }

    public static void stopAutoDetermineId() {
        autoDetermineId = false;
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

    protected static void logAttrChange(DomainObject obj, String attrName) {
	Transaction.logAttrChange(obj, attrName);
    }

    public static void noteStore(DomainObject obj, String attrName) {
	Transaction.storeObject(obj, attrName);
    }


    public DomainObject() {
        super();

        // All domain objects become persistent upon there creation.
        // Ensure that this object gets an idInternal
	// The idInternal will be used by both the hashcode and the equals methods
	// jcachopo: This should be changed in the future...

	ensureIdInternal();

	Transaction.storeNewObject(this);
	setAckOptLock(new Integer(0));
    }

    private void ensureIdInternal() {
        if (!lockMode || autoDetermineId) {
            setIdInternal(Integer.valueOf(nextIdInternal++));
	} else {
	    try {
		PersistenceBroker broker = Transaction.getOJBBroker();
		ClassDescriptor cld = broker.getClassDescriptor(this.getClass());
		Integer id = (Integer)broker.serviceSequenceManager().getUniqueValue(cld.getFieldDescriptorByName("idInternal"));
		setIdInternal(id);
	    } catch (Exception e) {
		System.out.println("Something went wrong when initializing the idInternal.  Not setting it...");
	    }
	}
    }

    public final int hashCode() {
	return super.hashCode();

//         if (getIdInternal() != null) {
//             return getIdInternal().intValue();
//         }

//         throw new RuntimeException("Domain object idInternal not set!");
    }

    public final boolean equals(Object obj) {
	return super.equals(obj);

//         if (obj != null && obj instanceof IDomainObject) {
//             IDomainObject domainObject = (IDomainObject) obj;
//             if (domainObject instanceof Proxy) {
//                 domainObject = (IDomainObject) ProxyHelper.getRealObject(domainObject);
//             }

//             if (this.getClass() == domainObject.getClass() && getIdInternal() != null
//                     && getIdInternal().equals(domainObject.getIdInternal())) {
//                 return true;
//             }
//         }
//         return false;
    }

    protected final void deleteDomainObject() {
        if (lockMode) {
            try {
                PersistenceSupportFactory.getDefaultPersistenceSupport()
                        .getIPersistentObject().deleteByOID(this.getClass(), getIdInternal());
            } catch (ExcepcaoPersistencia e) {
                throw new Error("Couldn't delete object", e);
            }
        }
    }

    public void invalidate(String attrName, int txNumber) {
	Class myClass = this.getClass();
	while (myClass != Object.class) {
	    try {
		Field f = myClass.getDeclaredField(attrName);
		f.setAccessible(true);
		((InvalidateSubject)f.get(this)).invalidate(txNumber);
		return;
	    } catch (NoSuchFieldException nsfe) {
		myClass = myClass.getSuperclass();
	    } catch (IllegalAccessException iae) {
		throw new Error("Couldn't invalidate attribute " + attrName + ": " + iae);
	    } catch (SecurityException se) {
		throw new Error("Couldn't invalidate attribute " + attrName + ": " + se);
	    }
	}
    }
}
