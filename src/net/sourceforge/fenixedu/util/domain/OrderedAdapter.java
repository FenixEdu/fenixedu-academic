package net.sourceforge.fenixedu.util.domain;

import java.util.Collection;

public interface OrderedAdapter<HolderType, ObjectType> {
    
    /**
     * Updates the order of all elements in the relation ensuring that all
     * elements will be given a sequencial numbering starting from 0. This is
     * usefull when the order of one of the elements is changed directly.
     * 
     * <p>
     * If and element is given the same order as other element then all the
     * elements with an order equal or greater than the order of that element
     * will be moved forward, that is, will have their order increased.
     * 
     * @param holder
     *            the holder of the relation
     * @param object
     *            the object with it's order changed
     */
    public void orderChanged(HolderType holder, ObjectType object);
    
    /**
     * Updates the order of all elements of the relation. <b>NOTE</b>: currently you
     * must provide all objects in the relation and the holder is not used.
     * 
     * @param holder
     *            the holder of the objects
     * @param objects
     *            the objects to be reordered
     */
    public void updateOrder(HolderType holder, Collection<ObjectType> objects);
    
    /**
     * Obtains the object that appears after the given object according to the
     * ordinal attribute used.
     * 
     * @param holder
     *            the holder of the relation
     * @param object
     *            the object in question
     *
     * @return the object that appears after the given object or
     *         <code>null</code> if the given object is the last
     */
    public ObjectType getNext(HolderType holder, ObjectType object);

    /**
     * Obtains the object that appears before the given object according to the
     * ordinal attribute used.
     * 
     * @param holder
     *            the holder of the relation
     * @param object
     *            the object in question
     * 
     * @return the object that appears before the given object or
     *         <code>null</code> if the given object is the first
     */
    public ObjectType getPrevious(HolderType holder, ObjectType object);
}
