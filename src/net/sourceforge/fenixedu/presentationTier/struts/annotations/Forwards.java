/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.struts.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Forwards {

    Forward[] value();

}
