/**
 * 
 */
package net.sourceforge.fenixedu.commons;

/**
 * Defines a functor interface implemented by classes that transform one object
 * into another.
 * 
 * A Transformer converts the input object to the output object. The input
 * object should be left unchanged.
 * 
 * This interface is a Type-Checked version of the Commons Transformer
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public interface Transformer<T, V> {

	public V transform(T input);

}
