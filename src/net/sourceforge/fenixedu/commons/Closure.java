/**
 * 
 */
package net.sourceforge.fenixedu.commons;

/**
 * Defines a functor interface implemented by classes that do something.
 * <p>
 * A <code>Closure</code> represents a block of code which is executed from inside some block, function or iteration. It operates
 * an input object.
 * 
 * This interface is a Type-Checked version of the Commons Transformer
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public interface Closure<T> {

    public void execute(T object);

}
