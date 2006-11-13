package net.sourceforge.fenixedu.util.domain;

import java.util.Collection;

/**
 * Provides an interface to obtain all the information required by the
 * {@link net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter OrderedRelationAdapter}.
 * 
 * @author cfgi
 */
public interface OrdinalAccessor<HolderType, ObjectType> {
    
    /**
     * @param holder
     *            the holder of the ordered relation
     * @return the elements having an order in the relation
     */
    public Collection<ObjectType> getObjects(HolderType holder);

    /**
     * @param target
     *            one of the objects being ordered in the relation
     * @return the current order for the object
     */
    public Integer getOrder(ObjectType target);

    /**
     * Changes the order of the target.
     * 
     * @param target
     *            one of the objects being ordered in the relation
     * @param order
     *            the new order for the target
     */
    public void setOrder(ObjectType target, Integer order);
}