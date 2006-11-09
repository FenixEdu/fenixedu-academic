/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SequenceUtil;
import net.sourceforge.fenixedu.stm.Transaction;
import net.sourceforge.fenixedu.stm.VersionedSubject;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.ClassDescriptor;

import pt.utl.ist.fenix.tools.util.StringAppender;

/**
 * @author jpvl
 */
public abstract class DomainObject extends DomainObject_Base implements dml.runtime.FenixDomainObject {

    public class UnableToDetermineIdException extends DomainException {
        public UnableToDetermineIdException(Throwable cause) {
            super("unable.to.determine.idException", cause);
        }
    }

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

    public DomainObject() {
        super();
        // All domain objects become persistent upon there creation.
        // Ensure that this object gets an idInternal
        // The idInternal will be used by both the hashcode and the equals methods
        // jcachopo: This should be changed in the future...
        ensureIdInternal();
        Transaction.storeNewObject(this);
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
            throw new UnableToDetermineIdException(e);
	    }
	}
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    protected final void deleteDomainObject() {
        if (lockMode) {
                PersistenceSupportFactory.getDefaultPersistenceSupport()
                        .getIPersistentObject().deleteByOID(this.getClass(), getIdInternal());
        }
    }

    public void addNewVersion(String attrName, int txNumber) {
	Class myClass = this.getClass();
	while (myClass != Object.class) {
	    try {
		Field f = myClass.getDeclaredField(attrName);
		f.setAccessible(true);
		((VersionedSubject)f.get(this)).addNewVersion(txNumber);
		return;
	    } catch (NoSuchFieldException nsfe) {
		myClass = myClass.getSuperclass();
	    } catch (IllegalAccessException iae) {
		throw new Error("Couldn't addNewVersion to attribute " + attrName + ": " + iae);
	    } catch (SecurityException se) {
		throw new Error("Couldn't addNewVersion to attribute " + attrName + ": " + se);
	    }
	}
    }

    public void addKnownVersionsFromLogs() {
	Class myClass = this.getClass();
	while (myClass != Object.class) {
	    try {
		Method m = myClass.getDeclaredMethod("initKnownVersions");
		m.setAccessible(true);
		m.invoke(this);
	    } catch (NoSuchMethodException nsme) {
		// ok
	    } catch (IllegalAccessException iae) {
		throw new Error("Couldn't addKnownVersions to obj from class " + this.getClass() + ": " + iae);
	    } catch (IllegalArgumentException iae) {
		throw new Error("Couldn't addKnownVersions to obj from class " + this.getClass() + ": " + iae);
	    } catch (InvocationTargetException ite) {
		throw new Error("Couldn't addKnownVersions to obj from class " + this.getClass() + ": " + ite);
	    } catch (SecurityException se) {
		throw new Error("Couldn't addKnownVersions to obj from class " + this.getClass() + ": " + se);
	    }
	    myClass = myClass.getSuperclass();
	}
    }
    
    protected String getCurrentUser() {
    	if(AccessControl.getUserView() != null) {
    		return AccessControl.getUserView().getUtilizador();
    	} else {
    		return System.getProperty("user.name", "FENIX");
    	}
    }

	@Override
	public final String toString() {
		return StringAppender.append(getClass().getName(), "(", getIdInternal().toString(), ")");
	}

}
