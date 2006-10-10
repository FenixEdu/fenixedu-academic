/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 18, 2006, 2:54:00 PM
 * 
 */
public class AlternativeSiteForm extends DynaValidatorForm {

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.set("dynamicMailDistribution", false);
    }
}
