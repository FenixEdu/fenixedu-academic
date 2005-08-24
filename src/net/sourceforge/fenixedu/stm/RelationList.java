package net.sourceforge.fenixedu.stm;

import java.util.Iterator;
import java.util.List;
import java.util.AbstractList;

import jvstm.PerTxBox;

public abstract class RelationList<E> extends AbstractList<E> {
    public static boolean report = false;

    private Object listHolder;
    private String attributeName;
    private VBox<FunctionalSet<E>> elements;
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
	if (allocateOnly) {
	    elements = VBox.makeNew(allocateOnly);
	} else {
	    elements = new VBox<FunctionalSet<E>>(FunctionalSet.EMPTY);
	}
    }

    protected abstract void addToRelation(E element);
    protected abstract boolean removeFromRelation(E element);

    private FunctionalSet<E> elementSet() {
	consolidateElements();
	return elements.get(listHolder, attributeName);
    }

    protected void consolidateElementsIfLoaded() {
	if (elements.isLoaded()) {
	    consolidateElements();
	}
    }

    private void consolidateElements() {
	FunctionalSet<E> origSet = elements.get(listHolder, attributeName);
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
	    elements.put(newSet);
	}
    }

    public void setFromOJB(OJBFunctionalSetWrapper ojbList) {
	elements.setFromOJB(ojbList.getElements());
    }

    public void justAdd(E obj) {
	elementsToAdd.put(elementsToAdd.get().add(obj));
	elementsToRemove.put(elementsToRemove.get().remove(obj));
    }

    public boolean justRemove(E obj) {
// 	FunctionalSet<E> previous = elementSet();
// 	FunctionalSet<E> next = previous.remove(obj);
// 	elements.put(next);
// 	// HACK!!! this assumes that removing a non-existent element from the set always returns the same set...
// 	return previous != next;

	elementsToRemove.put(elementsToRemove.get().add(obj));
	elementsToAdd.put(elementsToAdd.get().remove(obj));
	// HACK!!!! I should look into this 
	return true;
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
        return removeFromRelation((E) o);
    }

    public Iterator<E> iterator() {
	//System.out.println("calling rellist iterator");
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
	    if (report) {
		System.out.println("++++++  next in rellist is " + result);
	    }
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
