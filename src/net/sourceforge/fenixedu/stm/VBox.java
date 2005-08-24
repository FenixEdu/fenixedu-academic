package net.sourceforge.fenixedu.stm;

import jvstm.VBoxBody;

public class VBox<E> extends jvstm.VBox<E> {
    static final VBoxBody NOT_LOADED_YET = new SingleVersionBoxBody(null, -2);

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

    public boolean isLoaded() {
	return (this.body != NOT_LOADED_YET);
    }

    public void persistentLoad(E value) {
	TopLevelTransaction tx = (TopLevelTransaction)Transaction.currentFenixTransaction();
	VBoxBody exists = tx.getBodyWrittenForBox(this);
	if (exists != null) {
	    System.out.println("#### Persistent load for box with existing body written");
// 	    try {
// 		throw new Exception("Bump");
// 	    } catch (Exception e) {
// 		e.printStackTrace();
// 	    }
	}
	exists = tx.getBodyReadForBox(this);
	if (exists != null) {
	    System.out.println("++++ Persistent load for box with existing body read");
// 	    try {
// 		throw new Exception("Bump");
// 	    } catch (Exception e) {
// 		e.printStackTrace();
// 	    }
	}
	VBoxBody<E> body = new SingleVersionBoxBody(value, Transaction.current().getNumber());
	this.body = body;
    }

    public static <T> VBox<T> makeNew(boolean allocateOnly) {
	if (allocateOnly) {
	    return new VBox<T>(NOT_LOADED_YET);
	} else {
	    return new VBox<T>();
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

    

    public void setFromOJB(E value) {
	if (isLoading()) {
	    persistentLoad(value);
	} else {
	    put(value);
	}
    }
}
