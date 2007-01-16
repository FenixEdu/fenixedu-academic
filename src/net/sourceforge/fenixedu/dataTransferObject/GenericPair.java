/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class GenericPair<T, V> implements Serializable {

    private T left;

    private V right;

    public GenericPair(T left, V right) {
	super();
	this.left = left;
	this.right = right;
    }

    public T getLeft() {
	return left;
    }

    public void setLeft(T left) {
	this.left = left;
    }

    public V getRight() {
	return right;
    }

    public void setRight(V right) {
	this.right = right;
    }

    @Override
    public boolean equals(Object o) {
	return o instanceof GenericPair && getLeft().equals(((GenericPair) o).getLeft()) && getRight().equals(((GenericPair) o).getRight());
    }

    @Override
    public int hashCode() {
	final String hashBase = getLeft().toString() + getRight().toString();
	return hashBase.hashCode();
    }

}
