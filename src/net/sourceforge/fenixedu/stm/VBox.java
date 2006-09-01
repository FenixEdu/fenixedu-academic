package net.sourceforge.fenixedu.stm;

import jvstm.VBoxBody;
import net.sourceforge.fenixedu.domain.DomainObject;

public class VBox<E> extends jvstm.VBox<E> implements VersionedSubject,dml.runtime.FenixVBox<E> {
    static final Object NOT_LOADED_VALUE = new Object();

    public VBox() {
        super();
    }

    public VBox(E initial) {
	super(initial);
    }

    protected VBox(VBoxBody<E> body) {
	super(body);
    }

    public E get(Object obj, String attrName) {
        return Transaction.currentFenixTransaction().getBodyForRead(this, obj, attrName).value;
    }

    public void put(Object obj, String attrName, E newValue) {
        // TODO: eventually remove this 
        if (! (attrName.equals("idInternal") || attrName.equals("ackOptLock"))) {
            // the set of the idInternal or ackOptLock is performed by OJB and should not be logged
            Transaction.storeObject((DomainObject)obj, attrName);
        }
        put(newValue);
    }
    
    public boolean hasValue() {
	VBoxBody<E> body = Transaction.currentFenixTransaction().getBodyInTx(this);
	if (body == null) {
	    body = this.body;
	}
	return (body.value != NOT_LOADED_VALUE);
    }

    public void putNotLoadedValue() {
	this.put((E)NOT_LOADED_VALUE);
    }

    protected synchronized void persistentLoad(E value) {
	int txNumber = Transaction.current().getNumber();

	// find appropriate body
	VBoxBody<E> body = this.body.getBody(txNumber);
	if (body.value == NOT_LOADED_VALUE) {
	    body.value = value;
	}
    }

    public synchronized void addNewVersion(int txNumber) {
	if (body.version < txNumber) {
	    commit(allocateBody(txNumber));
	} else {
	    //System.out.println("WARNING: adding older version for a box.  This should not happen...");
	}
    }

    // override this method just to make it synchronized, because
    // bodies may be changed in other places besides the commit of a
    // write transaction.  E.g., when reading a non-loaded body
    public synchronized void commit(VBoxBody<E> newBody) {
	super.commit(newBody);
    }

    boolean reload(Object obj, String attr) {
	boolean loading = VBox.setLoading(true);
	try {
	    doReload(obj, attr);
	    return true;
	} catch (Throwable e) {
	    // what to do?
	    System.err.println("Couldn't reload attribute '" + attr + "': " + e.getMessage());
	    //e.printStackTrace();
	    return false;
	} finally {
	    VBox.setLoading(loading);
	}
    }

    protected void doReload(Object obj, String attr) {
	throw new Error("Can't reload a simple VBox.  Use a PrimitiveBox or a ReferenceBox instead.");
    }

    public synchronized void initKnownVersions(Object obj, String attr) {
	this.body = TransactionChangeLogs.allocateBodiesFor(this, obj, attr);
    }


    public VBoxBody<E> allocateBody(int txNumber) {
	VBoxBody<E> body = makeNewBody();
	body.version = txNumber;
	body.value = (E)NOT_LOADED_VALUE;
	return body;
    }

    public static <T> VBox<T> makeNew(boolean allocateOnly, boolean isReference) {
	if (isReference) {
	    if (allocateOnly) {
		return new ReferenceBox<T>((VBoxBody)null);
	    } else {
		return new ReferenceBox<T>();
	    }
	} else {
	    if (allocateOnly) {
		return new PrimitiveBox<T>((VBoxBody)null);
	    } else {
		return new PrimitiveBox<T>();
	    }
	}
    }


    protected static final ThreadLocal<Boolean> isLoading = new ThreadLocal<Boolean>() {
         protected Boolean initialValue() {
             return Boolean.FALSE;
         }
    };

    public static boolean isLoading() {
	return isLoading.get().booleanValue();
    }

    public static boolean setLoading(boolean loading) {
	boolean previous = isLoading();
	isLoading.set(loading ? Boolean.TRUE : Boolean.FALSE);
	return previous;
    }

    

    public void setFromOJB(Object obj, String attr, E value) {
	if (isLoading()) {
	    persistentLoad(value);
	} else {
	    Transaction.storeObject((DomainObject) obj, attr);
	    put(value);
	}
    }
}
