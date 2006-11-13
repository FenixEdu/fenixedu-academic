package net.sourceforge.fenixedu.util.domain;

import java.util.Collection;

import dml.runtime.RelationAdapter;

/**
 * This relation adapter is similar to the
 * {@link net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter} but can
 * be used in relations were the order of the elements is &lt;holder, object&gt;
 * instead of &lt;object, holder&gt;.
 * 
 * @author cfgi
 */
public class InverseOrderedRelationAdapter<ObjectType, HolderType> extends RelationAdapter<ObjectType, HolderType> implements OrderedAdapter<HolderType, ObjectType> {
    
    private OrderedRelationAdapter<HolderType, ObjectType> adapter;
    
    /**
     * Creates a new adaptor using the given selector to obtain the relation
     * elements and each element order.
     * 
     * @param selector
     *            the selected used to retrieve the necessary information
     */
    public InverseOrderedRelationAdapter(OrdinalAccessor<HolderType, ObjectType> accessor) {
        super();
        
        this.adapter = new OrderedRelationAdapter<HolderType, ObjectType>(accessor);
    }

    /**
     * A convenience constructor that is requivalent to the previous constructor
     * and using the {@link SlotSelector} selector.
     * 
     * @param slotName
     *            the name of the slot containing the order in type T1
     * @param relationName
     *            the name of the slot containing the relation in type T2
     */
    public InverseOrderedRelationAdapter(String slotName, String relationName) {
        super();
        
        this.adapter = new OrderedRelationAdapter<HolderType, ObjectType>(relationName, slotName);
    }
    
    @Override
    public void beforeAdd(ObjectType object, HolderType holder) {
        super.beforeAdd(object, holder);
        
        this.adapter.beforeAdd(holder, object);
    }

    @Override
    public void afterRemove(ObjectType object, HolderType holder) {
        super.afterRemove(object, holder);
        
        this.adapter.afterRemove(holder, object);
    }

    public void orderChanged(HolderType holder, ObjectType object) {
        this.adapter.orderChanged(holder, object);
    }

    public void updateOrder(HolderType holder, Collection<ObjectType> objects) {
        this.adapter.updateOrder(holder, objects);
    }

    public ObjectType getNext(HolderType holder, ObjectType object) {
        return this.adapter.getNext(holder, object);
    }

    public ObjectType getPrevious(HolderType holder, ObjectType object) {
        return this.adapter.getPrevious(holder, object);
    }

}
