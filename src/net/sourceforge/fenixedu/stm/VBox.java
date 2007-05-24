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
        return Transaction.currentFenixTransaction().getBoxValue(this, obj, attrName);
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
	return Transaction.currentFenixTransaction().isBoxValueLoaded(this);
    }

    public void putNotLoadedValue() {
	this.put((E)NOT_LOADED_VALUE);
    }

    protected void persistentLoad(E value) {
	int txNumber = Transaction.current().getNumber();
        persistentLoad(value, txNumber);
    }

    public void persistentLoad(Object value, int txNumber) {
	// find appropriate body
	VBoxBody<E> body = this.body.getBody(txNumber);
	if (body.value == NOT_LOADED_VALUE) {
	    body.value = (E)value;
	}
    }

    public VBoxBody addNewVersion(String attr, int txNumber) {
        return addNewVersion(attr, txNumber, false);
    }

    // This version of the addNewVersion method exists only because the special needs of
    // a RelationList which holds a SoftReference to its VBox.
    // For further explanations see the comment on the class SpecialBody at the end of this file
    VBoxBody addNewVersion(String attr, int txNumber, boolean specialBody) {
	if (body.version < txNumber) {
            return specialBody 
                ? commitSpecial((E)NOT_LOADED_VALUE, txNumber) 
                : commit((E)NOT_LOADED_VALUE, txNumber);
	} else {
            // when adding a new version to a box it may happen that a
	    // version with the same number exists already, if we are
	    // processing the change logs in the same server that
	    // committed those changelogs, between the time when the
	    // changelog were written to the database and the commit
	    // finishes setting the committed tx number
            //
            // so, do nothing and just return null
            
	    //System.out.println("!!! WARNING !!!: adding older version for a box attr " + attr + " -> " + body.version + " not < " + txNumber);
            return null;
	}
    }

    boolean reload(Object obj, String attr) {
	try {
	    doReload(obj, attr);
	    return true;
	} catch (Throwable e) {
	    // what to do?
	    System.err.println("Couldn't reload attribute '" + attr + "': " + e.getMessage());
	    //e.printStackTrace();
	    return false;
	}
    }

    // see comment on the class SpecialBody at the end of this file
    private VBoxBody commitSpecial(E newValue, int txNumber) {
        VBoxBody<E> newBody = new SpecialBody(newValue, txNumber, this.body, this);
	this.body = newBody;
        return newBody;
    }

    protected void doReload(Object obj, String attr) {
	throw new Error("Can't reload a simple VBox.  Use a PrimitiveBox or a ReferenceBox instead.");
    }

    public static <T> VBox<T> makeNew(boolean allocateOnly, boolean isReference) {
	if (isReference) {
	    if (allocateOnly) {
                // when a box is allocated, it is safe 
                // to say that the version number is 0
		return new ReferenceBox<T>(makeNewBody((T)NOT_LOADED_VALUE, 0, null));
	    } else {
		return new ReferenceBox<T>();
	    }
	} else {
	    if (allocateOnly) {
                // when a box is allocated, it is safe 
                // to say that the version number is 0
		return new PrimitiveBox<T>(makeNewBody((T)NOT_LOADED_VALUE, 0, null));
	    } else {
		return new PrimitiveBox<T>();
	    }
	}
    }

    public void setFromOJB(Object obj, String attr, E value) {
        persistentLoad(value);
    }



    /*
     * This SpecialBody class is a hack that will eventually disappear.
     * It is a simple extension of a MultiVersionBoxBody so that it holds a 
     * strong reference to the box which is owning it.  Instances of this class
     * are created only during the processing of an AlientTransaction
     * (see TransactionChangeLogs) when a new version is added to a RelationList.
     * Because RelationLists use a SoftReference to keep the VBox (so that the 
     * bi-directional relations do not prevent the GC from working), it could happen
     * that the VBox of a RelationList got GCed after the processing of an AlientTransaction 
     * but before older running transactions finished.  If meanwhile a more recent transaction
     * accessed the RelationList, it would load its value and associate it with version 0, which 
     * is wrong.  So, until the AlientTransaction gets cleaned up (see the 
     * TransactionChangeLogs.cleanOldAlienTxs method), we must prevent that the VBox be GCed.
     * This class ensures it, because the AlientTransaction keeps strong a reference to an instance of 
     * this class which has also a strong reference to the VBox.
     * Finally, when the cleanOldAlienTxs method runs and calls the freeResources on the AlienTransaction,
     * it calls the clearPrevious method of each body, which, in this case, also removes the reference to
     * the VBox.
     *
     * It's a *little* bit confusing, I know, but...
     */
    private static class SpecialBody<E> extends VBoxBody<E> {
        private VBox owner;

        SpecialBody(E value, int version, VBoxBody<E> next, VBox owner) {
            super(value, version, next);
            this.owner = owner;
        }

        public void clearPrevious() {
            super.clearPrevious();

            // loose the reference to the owner so that it may be GCed, if needed
            owner = null;
        }
    }
}
