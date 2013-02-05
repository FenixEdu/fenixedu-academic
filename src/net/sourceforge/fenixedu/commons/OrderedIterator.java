package net.sourceforge.fenixedu.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 18:43:06,3/Nov/2005
 * @version $Id: OrderedIterator.java 45653 2010-01-19 18:39:54Z
 *          ist148357@IST.UTL.PT $
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

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public T next() {
        return this.iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
