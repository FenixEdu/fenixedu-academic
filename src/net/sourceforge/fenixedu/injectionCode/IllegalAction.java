/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:46:17,10/Fev/2006
 * @version $Id$
 */
public class IllegalAction extends RuntimeException {
    public IllegalAction(String msg) {
	super(msg);
    }
}
