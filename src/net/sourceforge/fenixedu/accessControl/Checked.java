/**
 * 
 */
package net.sourceforge.fenixedu.accessControl;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 9:11:59,24/Nov/2005
 * @version $Id$
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface Checked
{
	String value(); 
}
