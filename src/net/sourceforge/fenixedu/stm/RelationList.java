package net.sourceforge.fenixedu.stm;

import java.lang.ref.SoftReference;
import java.util.AbstractList;
import java.util.Iterator;

import jvstm.PerTxBox;

public abstract class RelationList<E> extends AbstractList<E> implements VersionedSubject {
    private Object listHolder;
    private String attributeName;
    private SoftReference<VBox<FunctionalSet<E>>> elementsRef;
    private PerTxBox<FunctionalSet<E>> elementsToAdd = new PerTxBox<FunctionalSet<E>>(FunctionalSet.EMPTY) {
        public void commit(FunctionalSet<E> toAdd) {
	    consolidateElementsIfLoaded();
        }
    };
    private PerTxBox<FunctionalSet<E>> elementsToRemove = new PerTxBox<FunctionalSet<E>>(FunctionalSet.EMPTY) {
        public void commit(FunctionalSet<E> toRemove) {
	    consolidateElementsIfLoaded();
        }
    };

    public RelationList(Object listHolder, String attributeName, boolean allocateOnly) {
	this.listHolder = listHolder;
	this.attributeName = attributeName;
	VBox elementsBox = null;
	if (allocateOnly) {
	    elementsBox = VBox.makeNew(allocateOnly, true);
	} else {
	    elementsBox = new ReferenceBox<FunctionalSet<E>>(FunctionalSet.EMPTY);
	}
	this.elementsRef = new SoftReference<VBox<FunctionalSet<E>>>(elementsBox);
    }

    // The access to the elementsRef field should be synchronized
    private synchronized VBox<FunctionalSet<E>> getElementsBox() {
	VBox<FunctionalSet<E>> box = elementsRef.get();
	if (box == null) {
	    box = VBox.makeNew(true, true);
	    box.initKnownVersions(listHolder, attributeName);
	    this.elementsRef = new SoftReference<VBox<FunctionalSet<E>>>(box);
	}
	return box;
    }


    // The access to the elementsRef field should be synchronized
    public synchronized void addNewVersion(int txNumber) {
	VBox<FunctionalSet<E>> box = elementsRef.get();
	if (box != null) {
	    box.addNewVersion(txNumber);
	}
    }

    public void initKnownVersions(Object obj, String attr) {
	getElementsBox().initKnownVersions(obj, attr);
    }


    protected abstract void addToRelation(E element);
    protected abstract void removeFromRelation(E element);

    private FunctionalSet<E> elementSet() {
	consolidateElements();
	return getElementsBox().get(listHolder, attributeName);
    }

    protected void consolidateElementsIfLoaded() {
	if (elementsToAdd.get().size() + elementsToRemove.get().size() > 0) {
	    VBox<FunctionalSet<E>> box = getElementsBox();
	    if (box.hasValue()) {
		consolidateElements();
	    } else {
		box.putNotLoadedValue();
	    }
	}
    }

    private void consolidateElements() {
	VBox<FunctionalSet<E>> box = getElementsBox();
	FunctionalSet<E> origSet = box.get(listHolder, attributeName);
	FunctionalSet<E> newSet = origSet;

	if (elementsToRemove.get().size() > 0) {
	    Iterator<E> iter = elementsToRemove.get().iterator();
	    while (iter.hasNext()) {
		newSet = newSet.remove(iter.next());
	    }
	    elementsToRemove.put(FunctionalSet.EMPTY);
	}

	if (elementsToAdd.get().size() > 0) {
	    Iterator<E> iter = elementsToAdd.get().iterator();
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

    public void justAdd(E obj) {
	elementsToAdd.put(elementsToAdd.get().add(obj));
	elementsToRemove.put(elementsToRemove.get().remove(obj));
    }

    public void justRemove(E obj) {
	elementsToRemove.put(elementsToRemove.get().add(obj));
	elementsToAdd.put(elementsToAdd.get().remove(obj));
    }


    public int size() {
	return elementSet().size();
    }

    public E get(int index) {
        return elementSet().get(index);
    }

    public E set(int index, E element) {
        E oldElement = get(index);

        int oldModCount = modCount;
        if (oldElement != element) {
            remove(oldElement);
            add(index, element);
        }
        // After the remove and add the modCount would have been incremented twice
        modCount = oldModCount + 1;
        return oldElement;
    }

    public void add(int index, E element) {
        addToRelation(element);
        modCount++;
    }

    public E remove(int index) {
        E elemToRemove = get(index);
        remove(elemToRemove);
	return elemToRemove;
    }

    public boolean remove(Object o) {
        modCount++;
        removeFromRelation((E) o);
	// HACK!!! What to return here?
	// I wouldn't like to force a load of the list to be able to return the correct boolean value
	return true;
    }

    public Iterator<E> iterator() {
	return new RelationListIterator<E>(this);
    }

    private static class RelationListIterator<X> implements Iterator<X> {
	private RelationList<X> relList;
	private Iterator<X> iter;
	private boolean canRemove = false;
	private X previous = null;

	RelationListIterator(RelationList<X> relList) {
	    this.relList = relList;
	    this.iter = relList.elementSet().iterator();
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
		relList.justRemove(previous);
	    }
	}
    }
}
