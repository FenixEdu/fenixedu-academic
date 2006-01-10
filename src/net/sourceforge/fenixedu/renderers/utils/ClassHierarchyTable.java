package net.sourceforge.fenixedu.renderers.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.TruePredicate;

public class ClassHierarchyTable<T> extends Hashtable<Class, T> {

    private List<Class> classSort;
    
    public ClassHierarchyTable() {
        super();
        
        this.classSort = new ArrayList<Class>();
    }

    @Override
    public synchronized T get(Object key) {
        return get(key, TruePredicate.INSTANCE);
    }
    
    public synchronized T get(Object key, Predicate predicate) {
        Class objectType = (Class) key;
        
        for (Class<? extends Object> type : this.classSort) {
            if (type.isAssignableFrom(objectType) && predicate.evaluate(super.get(type))) {
                return super.get(type); 
            }
        }
        
        return null;
    }
    
    public synchronized T getUnspecific(Class key) {
        return super.get(key);
    }

    @Override
    public synchronized T put(Class key, T value) {
        addType(key);
        
        return super.put(key, value);
    }

    public int addType(Class type) {
        int index = findIndex(type);

        addType(type, index);
        return index;
    }

    public int findIndex(Class type) {
        int index = 0;

        for (Iterator iter = this.classSort.iterator(); iter.hasNext(); index++) {
            Class<? extends Object> element = (Class<? extends Object>) iter.next();

            // read this as "element is after type in list"
            if (element.isAssignableFrom(type)) {
                break;
            }
        }

        return index;
    }

    private void addType(Class type, int index) {
        // no elements? then just insert
        if (this.classSort.size() == 0) {
            this.classSort.add(type);
            return;
        }

        // insert at end is always ok
        if (index == this.classSort.size()) {
            this.classSort.add(index, type);
            return;
        }
        
        // avoid duplicates
        if (!this.classSort.get(index).equals(type)) {
            this.classSort.add(index, type);
        }
    }
}
