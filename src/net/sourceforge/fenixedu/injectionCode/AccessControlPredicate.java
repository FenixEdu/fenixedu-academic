/**
 * 
 */
package net.sourceforge.fenixedu.injectionCode;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public interface AccessControlPredicate<E> {

    public boolean evaluate(E c);

}
