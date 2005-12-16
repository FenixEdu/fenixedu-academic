/**
 * 
 */
package net.sourceforge.fenixedu.accessControl.injector;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 15:01:06,25/Nov/2005
 * @version $Id$
 */
public interface AccessControlCodeGenerator
{
	public abstract String getCode(String groupAccessorMethodName);
}
