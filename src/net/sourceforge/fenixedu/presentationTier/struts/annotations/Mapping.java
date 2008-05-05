/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.struts.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.struts.action.ActionForm;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    String path();

    String formBean() default "";

    Class<? extends ActionForm> formBeanClass() default ActionForm.class;

    String input() default "";

    String module();

}
