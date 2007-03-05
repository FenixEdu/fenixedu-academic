package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class DomainListReference<T extends DomainObject> extends AbstractList<T> implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private List<DomainReference<T>> list;
    
    public DomainListReference() {
        super();
        
        this.list = new ArrayList<DomainReference<T>>();
    }

    public DomainListReference(List<T> list) {
        this();
        
        for (T object : list) {
            add(object);
        }
    }

    @Override
    public T get(int index) {
        return this.list.get(index).getObject();
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public T set(int index, T element) {
        return convert(this.list.set(index, new DomainReference<T>(element)));
    }
    
    @Override
    public void add(int index, T element) {
        this.list.add(index, new DomainReference<T>(element));
    }

    @Override
    public T remove(int index) {
        return convert(this.list.remove(index));
    }

    private T convert(DomainReference<T> reference) {
        if (reference == null) {
            return null;
        }
        else {
            return reference.getObject();
        }
    }

}
