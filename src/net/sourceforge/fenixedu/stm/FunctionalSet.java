package net.sourceforge.fenixedu.stm;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FunctionalSet<E> {
    public static final FunctionalSet EMPTY = new FunctionalSet();

    private int size = 0;
    private E element = null;
    private FunctionalSet<E> next = null;
    
    private FunctionalSet() {
    }

    public synchronized int size() {
	return size;
    }

    public E get(int index) {
	if (index > size) {
	    throw new NoSuchElementException();
	} else {
	    FunctionalSet<E> iter = this;
	    while (index-- > 0) {
		iter = iter.next;
	    }
	    return iter.element;
	}
    }
   
    public FunctionalSet<E> addUnique(E obj) {
	FunctionalSet<E> newSet = new FunctionalSet<E>();
	newSet.element = obj;
	newSet.next = this;
	newSet.size = this.size() + 1;
	return newSet;
    }

    public FunctionalSet<E> add(E obj) {
	if (this.contains(obj)) {
	    return this;
	} else {
	    return addUnique(obj);
	}
    }

    public FunctionalSet<E> remove(E obj) {
	if (! this.contains(obj)) {
	    return this;
	} else {
	    return removeExisting(obj);
	}
    }
    
    private FunctionalSet<E> removeExisting(E obj) {
	FunctionalSet<E> result = null;
	FunctionalSet<E> prev = null;
	FunctionalSet<E> iter = this;

	while (! ((obj == iter.element) || ((obj != null) && obj.equals(iter.element)))) {
	    FunctionalSet<E> newEntry = new FunctionalSet<E>();
	    newEntry.element = iter.element;
	    newEntry.size = iter.size() - 1;
	    if (result == null) {
		result = newEntry;
	    } else {
		prev.next = newEntry;
	    }
	    prev = newEntry;
	    iter = iter.next;
	}
	if (result == null) {
	    result = iter.next;
	} else {
	    prev.next = iter.next;
	}

	return result;
    }

    public boolean contains(E obj) {
	FunctionalSet iter = this;
	if (obj == null) {
	    while (iter.size() != 0) {
		if (iter.element == null) {
		    return true;
		}
		iter = iter.next;
	    }
	} else {
	    while (iter.size() != 0) {
		if (obj.equals(iter.element)) {
		    return true;
		}
		iter = iter.next;
	    }
	}
	return false;
    }


    public Iterator iterator() {
	return new FunctionalSetIterator<E>(this);
    }

    private static class FunctionalSetIterator<E> implements Iterator<E> {
	private FunctionalSet<E> current;

	FunctionalSetIterator(FunctionalSet<E> funcSet) {
	    this.current = funcSet;
	}


	public boolean hasNext() {
	    return current.size() > 0;
	}

	public E next() {
	    if (current.size() == 0) {
		throw new NoSuchElementException();
	    } else {
		E res = current.element;
		current = current.next;
		return res;
	    }
	}
	    
	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }
}
