/**
 * 
 */
package net.sourceforge.fenixedu.accessControl;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public interface AccessControlPredicate<E> {

    public  boolean evaluate(E o);
    
}
