package net.sourceforge.fenixedu.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 18:43:06,3/Nov/2005
 * @version $Id$
 */
public class OrderedIterator implements Iterator {

    private Iterator iterator;
    private ArrayList backingList;

    public OrderedIterator(Iterator iterator, Comparator comparator) {
        super();
        this.backingList = new ArrayList();
        while(iterator.hasNext())
        {
        	this.backingList.add(iterator.next());
        }
        Collections.sort(this.backingList,comparator);
        this.iterator = backingList.iterator();
    }
    
    public OrderedIterator(Iterator iterator) {
        super();
        this.backingList = new ArrayList();
        while(iterator.hasNext())
        {
        	this.backingList.add(iterator.next());
        }
        Collections.sort(this.backingList);
        this.iterator = backingList.iterator();
    }


    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    public Object next() {
        return this.iterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
