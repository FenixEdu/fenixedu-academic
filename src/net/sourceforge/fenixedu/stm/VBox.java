package net.sourceforge.fenixedu.stm;

import jvstm.VBoxBody;

public class VBox<E> extends jvstm.VBox<E> implements InvalidateSubject {
    static final Object NOT_LOADED_VALUE = new Object();
    static final VBoxBody NOT_LOADED_BODY = new SingleVersionBoxBody(NOT_LOADED_VALUE, -2);

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
    
    public VBoxBody<E> makeNewBody() {
	return new SingleVersionBoxBody<E>();
    }

    public boolean hasValue() {
	VBoxBody<E> body = Transaction.currentFenixTransaction().getBodyInTx(this);
	if (body == null) {
	    body = this.body;
	}
	return (body.value != NOT_LOADED_VALUE);
    }

    protected synchronized void persistentLoad(Object obj, String attr, E value) {
	int txNumber = Transaction.current().getNumber();
	if ((body.value == NOT_LOADED_VALUE) && (body.version <= txNumber)) {
	    int newerVersion = Transaction.findVersionFor(obj, attr, txNumber);
	    if (newerVersion <= txNumber) {
		VBoxBody<E> body = new SingleVersionBoxBody(value, txNumber);
		this.body = body;
	    } else {
		invalidate(newerVersion);
	    }
	}
    }

    public synchronized void invalidate(int txNumber) {
	if (body.version < txNumber) {
	    VBoxBody<E> body = new SingleVersionBoxBody(NOT_LOADED_VALUE, txNumber);
	    this.body = body;
	}
    }

    boolean reload(Object obj, String attr) {
	boolean loading = VBox.setLoading(true);
	try {
	    doReload(obj, attr);
	    return true;
	} catch (Exception e) {
	    // what to do?
	    System.err.println("Couldn't reload attribute '" + attr + "'");
	    //e.printStackTrace();
	    return false;
	} finally {
	    VBox.setLoading(loading);
	}
    }

    protected void doReload(Object obj, String attr) {
	throw new Error("Can't reload a simple VBox.  Use a PrimitiveBox or a ReferenceBox instead.");
    }

    public static <T> VBox<T> makeNew(boolean allocateOnly, boolean isReference) {
	if (isReference) {
	    if (allocateOnly) {
		return new ReferenceBox<T>(NOT_LOADED_BODY);
	    } else {
		return new ReferenceBox<T>();
	    }
	} else {
	    if (allocateOnly) {
		return new PrimitiveBox<T>(NOT_LOADED_BODY);
	    } else {
		return new PrimitiveBox<T>();
	    }
	}
    }


    protected static final ThreadLocal<Boolean> isLoading = new ThreadLocal<Boolean>() {
         protected synchronized Boolean initialValue() {
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
	    persistentLoad(obj, attr, value);
	} else {
	    put(value);
	}
    }
}
