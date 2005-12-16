package net.sourceforge.fenixedu.stm;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.ojb.broker.ManageableCollection;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;


public class OJBFunctionalSetWrapper implements ManageableCollection {
    private static final Iterator EMPTY_ITER = new Iterator() {
	    public boolean hasNext() {
		return false;
	    }
	    
	    public Object next() {
		throw new NoSuchElementException();
	    }
	    
	    public void remove() {
		throw new UnsupportedOperationException();
	    }
	};


    private FunctionalSet elements = FunctionalSet.EMPTY;

    public OJBFunctionalSetWrapper() {
    }

    public FunctionalSet getElements() {
	return elements;
    }

    public void ojbAdd(Object anObject) {
	elements = elements.addUnique(anObject);
    }

    public void ojbAddAll(ManageableCollection otherCollection) {
	Iterator iter = ((OJBFunctionalSetWrapper)otherCollection).getElements().iterator();
	while (iter.hasNext()) {
	    ojbAdd(iter.next());
	}
    }

    public Iterator ojbIterator() {
	return EMPTY_ITER;
    }

    public void afterStore(PersistenceBroker broker) throws PersistenceBrokerException {
	// empty
    }
}
