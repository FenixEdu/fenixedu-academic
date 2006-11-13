package net.sourceforge.fenixedu.util.domain;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import dml.runtime.RelationAdapter;

/**
 * This adapter when added to a direct relation manages the order of all the
 * elements in the relation
 * of all the elements in the relation so that there is a strict ordering, by
 * that slot's value, between all the elements.
 * 
 * @author cfgi
 */
public class OrderedRelationAdapter<HolderType, ObjectType> extends RelationAdapter<HolderType, ObjectType> {

    private class OrderComparator implements Comparator<ObjectType> {

        private ObjectType element;

        public OrderComparator(ObjectType element) {
            this.element = element;
        }
        
        public int compare(ObjectType o1, ObjectType o2) {
            Integer order1 = getOrder(o1);
            Integer order2 = getOrder(o2);
            
            if (order1 == null) {
                if (order2 == null) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
            else {
                if (order2 == null) {
                    return -1;
                }
                else {
                    int comparison = order1.compareTo(order2);
                    if (comparison != 0) {
                        return comparison;
                    }
                    else {
                        if (o1.equals(this.element)) {
                            return -1;
                        }
                        else {
                            if (o2.equals(this.element)) {
                                return 1;
                            }
                            else {
                                return new Integer(o1.hashCode()).compareTo(o2.hashCode());
                            }
                        }
                    }
                }
            }
        }
        
    }

    private OrdinalAccessor<HolderType, ObjectType> accessor;
    private boolean insertLast;
    
    /**
     * Creates a new adaptor using the given selector to obtain the relation
     * elements and each element order.
     * 
     * @param selector
     *            the selected used to retrieve the necessary information
     */
    public OrderedRelationAdapter(OrdinalAccessor<HolderType, ObjectType> selector) {
        super();
        
        this.insertLast = true;
        this.accessor = selector;
    }

    /**
     * A convenience constructor that is requivalent to the previous constructor
     * and using the {@link SlotSelector} selector.
     * @param relationName
     *            the name of the slot containing the relation in type T2
     * @param slotName
     *            the name of the slot containing the order in type T1
     */
    public OrderedRelationAdapter(String relationName, String slotName) {
        this(new SlotSelector<HolderType, ObjectType>(relationName, slotName));
    }
    
    private Collection<ObjectType> getRelation(HolderType holder) {
        return this.accessor.getObjects(holder);
    }

    private void setOrder(ObjectType object, int order) {
        this.accessor.setOrder(object, order);
    }

    private Integer getOrder(ObjectType object) {
        return this.accessor.getOrder(object);
    }

    @Override
    public void afterRemove(HolderType holder, ObjectType object) {
        super.afterRemove(holder, object);
        
        if (object == null || holder == null) {
            return;
        }
        
        compress(holder, object);
    }

    @Override
    public void beforeAdd(HolderType holder, ObjectType object) {
        super.beforeAdd(holder, object);
        
        if (object == null || holder == null) {
            return;
        }
            
        Integer order = getOrder(object);
        if (order == null) {
            setOrder(object, getDefaultOrder(holder));
            updateDefault(holder, object);
        }
        else {
            expand(holder, object);
        }
    }

    private void updateDefault(HolderType holder, ObjectType object) {
        if (! this.insertLast) {
            expand(holder, object);
        }
    }

    private void pack(HolderType holder, ObjectType object, int offset) {
        Integer objectOrder = getOrder(object);
        
        for (ObjectType other : getRelation(holder)) {
            Integer otherOrder = getOrder(other);
            
            if (otherOrder >= objectOrder) {
                setOrder(other, otherOrder + offset);
            }
        }
    }
    
    private void compress(HolderType holder, ObjectType object) {
        pack(holder, object, -1);
    }

    private void expand(HolderType holder, ObjectType object) {
        pack(holder, object, 1);
    }

    private int getDefaultOrder(HolderType holder) {
        int value = getInfinity();
        
        for (ObjectType object : getRelation(holder)) {
            value = selectGreater(value, getOrderForComparison(object));
        }
        
        return normalizeValue(value);
    }

    private Integer getOrderForComparison(ObjectType object) {
        Integer order = getOrder(object);
        
        if (order == null) {
            order = getInfinity();
        }

        return order;
    }
    
    private int getInfinity() {
        return this.insertLast ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    private int selectGreater(int value, int order) {
        if (this.insertLast) {
            return Math.max(value, order);
        }
        else {
            return Math.min(value, order);
        }
    }

    private int normalizeValue(int value) {
        if (value == getInfinity()) {
            return 0;
        }
        
        return this.insertLast ? value + 1 : value;
    }
    
    public void orderChanged(HolderType holder, ObjectType object) {
        SortedSet<ObjectType> orderedElements = new TreeSet<ObjectType>(new OrderComparator(object));
        orderedElements.addAll(getRelation(holder));
     
        updateOrder(holder, orderedElements);
    }
    
    public void updateOrder(HolderType holder, Collection<ObjectType> objects) {
        if (! getRelation(holder).containsAll(objects)) {
            throw new DomainException("dml.ordered.relation.objects.holder.mismatch");
        }
        
        int index = 0;
        for (ObjectType object : objects) {
            setOrder(object, index++);
        }
    }
    
    public ObjectType getNext(HolderType holder, ObjectType object) {
        SortedSet<ObjectType> orderedElements = new TreeSet<ObjectType>(new OrderComparator(object));
        orderedElements.addAll(getRelation(holder));
        
        Integer ownOrder = getOrder(object);
        for (ObjectType element : orderedElements) {
            Integer order = getOrder(element);
            
            if (order > ownOrder) {
                return element;
            }
        }
        
        return null;
    }
    
    public ObjectType getPrevious(HolderType holder, ObjectType object) {
        SortedSet<ObjectType> orderedElements = new TreeSet<ObjectType>(new OrderComparator(object));
        orderedElements.addAll(getRelation(holder));
        
        Integer ownOrder = getOrder(object);
        for (ObjectType element : orderedElements) {
            Integer order = getOrder(element);
            
            if (order + 1 == ownOrder) {
                return element;
            }
            
            if (order >= ownOrder) {
                break;
            }
        }
        
        return null;
    }
    
}
