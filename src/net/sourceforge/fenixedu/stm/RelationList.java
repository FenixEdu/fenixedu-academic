package net.sourceforge.fenixedu.stm;

import java.lang.ref.SoftReference;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.Set;

import jvstm.PerTxBox;
import net.sourceforge.fenixedu.domain.DomainObject;
import dml.runtime.Relation;

public class RelationList<E1,E2> extends AbstractList<E2> implements VersionedSubject,Set<E2>,dml.runtime.RelationBaseSet<E2> {
    private E1 listHolder;
    private Relation<E1,E2> relation;
    private String attributeName;

    private SoftReference<VBox<FunctionalSet<E2>>> elementsRef;

    private PerTxBox<FunctionalSet<E2>> elementsToAdd = new PerTxBox<FunctionalSet<E2>>(FunctionalSet.EMPTY) {
        public void commit(FunctionalSet<E2> toAdd) {
	    consolidateElementsIfLoaded();
        }
    };

    private PerTxBox<FunctionalSet<E2>> elementsToRemove = new PerTxBox<FunctionalSet<E2>>(FunctionalSet.EMPTY) {
        public void commit(FunctionalSet<E2> toRemove) {
	    consolidateElementsIfLoaded();
        }
    };

    public RelationList(E1 listHolder, Relation<E1,E2> relation, String attributeName, boolean allocateOnly) {
	this.listHolder = listHolder;
        this.relation = relation;
	this.attributeName = attributeName;

	VBox elementsBox = null;
	if (allocateOnly) {
	    elementsBox = VBox.makeNew(allocateOnly, true);
	} else {
	    elementsBox = new ReferenceBox<FunctionalSet<E2>>(FunctionalSet.EMPTY);
	}
	this.elementsRef = new SoftReference<VBox<FunctionalSet<E2>>>(elementsBox);
    }

    public void initKnownVersions(Object obj, String attr) {
        // remove this when the DML compiler no longer generates the initKnownVersions calls
    }    


    // The access to the elementsRef field should be synchronized
    private synchronized VBox<FunctionalSet<E2>> getElementsBox() {
	VBox<FunctionalSet<E2>> box = elementsRef.get();
	if (box == null) {
	    box = VBox.makeNew(true, true);
	    this.elementsRef = new SoftReference<VBox<FunctionalSet<E2>>>(box);
	}
	return box;
    }


    public jvstm.VBoxBody addNewVersion(String attr, int txNumber) {
        return getElementsBox().addNewVersion(attr, txNumber, true);
    }

    private FunctionalSet<E2> elementSet() {
	consolidateElements();
	return getElementsBox().get(listHolder, attributeName);
    }

    protected void consolidateElementsIfLoaded() {
	if (elementsToAdd.get().size() + elementsToRemove.get().size() > 0) {
	    VBox<FunctionalSet<E2>> box = getElementsBox();
	    if (box.hasValue()) {
		consolidateElements();
	    } else {
		box.putNotLoadedValue();
	    }
	}
    }

    private void consolidateElements() {
	VBox<FunctionalSet<E2>> box = getElementsBox();
	FunctionalSet<E2> origSet = box.get(listHolder, attributeName);
	FunctionalSet<E2> newSet = origSet;

	if (elementsToRemove.get().size() > 0) {
	    Iterator<E2> iter = elementsToRemove.get().iterator();
	    while (iter.hasNext()) {
		newSet = newSet.remove(iter.next());
	    }
	    elementsToRemove.put(FunctionalSet.EMPTY);
	}

	if (elementsToAdd.get().size() > 0) {
	    Iterator<E2> iter = elementsToAdd.get().iterator();
	    while (iter.hasNext()) {
		newSet = newSet.add(iter.next());
	    }
	    elementsToAdd.put(FunctionalSet.EMPTY);
	}

	if (newSet != origSet) {
	    box.put(newSet);
	}
    }

    public void setFromOJB(Object obj, String attr, OJBFunctionalSetWrapper ojbList) {
	getElementsBox().setFromOJB(obj, attr, ojbList.getElements());
    }

    public void justAdd(E2 obj) {
        Transaction.logAttrChange((DomainObject)listHolder, attributeName);
	elementsToAdd.put(elementsToAdd.get().add(obj));
	elementsToRemove.put(elementsToRemove.get().remove(obj));
    }

    public void justRemove(E2 obj) {
        Transaction.logAttrChange((DomainObject)listHolder, attributeName);
	elementsToRemove.put(elementsToRemove.get().add(obj));
	elementsToAdd.put(elementsToAdd.get().remove(obj));
    }


    public int size() {
	return elementSet().size();
    }

    public E2 get(int index) {
        return elementSet().get(index);
    }

    public E2 set(int index, E2 element) {
        E2 oldElement = get(index);

        int oldModCount = modCount;
        if (oldElement != element) {
            remove(oldElement);
            add(index, element);
        }
        // After the remove and add the modCount would have been incremented twice
        modCount = oldModCount + 1;
        return oldElement;
    }

    public void add(int index, E2 element) {
        relation.add(listHolder, element);
        modCount++;
    }

    public E2 remove(int index) {
        E2 elemToRemove = get(index);
        remove(elemToRemove);
	return elemToRemove;
    }

    public boolean remove(Object o) {
        modCount++;
        relation.remove(listHolder, (E2)o);
	// HACK!!! What to return here?
	// I wouldn't like to force a load of the list to be able to return the correct boolean value
	return true;
    }

    public Iterator<E2> iterator() {
	return new RelationListIterator<E2>();
    }

    private class RelationListIterator<X> implements Iterator<X> {
	private Iterator<X> iter;
	private boolean canRemove = false;
	private X previous = null;

	RelationListIterator() {
	    this.iter = RelationList.this.elementSet().iterator();
	}

	public boolean hasNext() {
	    return iter.hasNext();
	}

	public X next() {
	    X result = iter.next();
	    canRemove = true;
	    previous = result;
	    return result;
	}
	    
	public void remove() {
	    if (! canRemove) {
		throw new IllegalStateException();
	    } else {
		canRemove = false;
		RelationList.this.remove(previous);
	    }
	}
    }
}
