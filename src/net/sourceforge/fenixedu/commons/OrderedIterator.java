package net.sourceforge.fenixedu.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 18:43:06,3/Nov/2005
 * @version $Id$
 */
public class OrderedIterator<T> implements Iterator {

    private Iterator<T> iterator;

    private List<T> buildBackingList(Iterator<T> iterator) {
	List<T> backingList = new ArrayList<T>();
	while (iterator.hasNext()) {
	    backingList.add(iterator.next());
	}

	return backingList;
    }

    public OrderedIterator(Iterator<T> iterator, Comparator<T> comparator) {
	super();
	List<T> backingList = this.buildBackingList(iterator);
	Collections.sort(backingList, comparator);
	this.iterator = backingList.iterator();
    }

    public OrderedIterator(Iterator<T> iterator) {
	super();
	List backingList = this.buildBackingList(iterator);
	Collections.sort(backingList);
	this.iterator = backingList.iterator();
    }

    public boolean hasNext() {
	return this.iterator.hasNext();
    }

    public T next() {
	return this.iterator.next();
    }

    public void remove() {
	throw new UnsupportedOperationException();
    }
}
