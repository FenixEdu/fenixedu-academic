/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.cms.basic;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:07:57,15/Nov/2005
 * @version $Id$
 */
public class CmsConfigurationForm extends DynaValidatorForm
{
	@Override
	 public void reset(ActionMapping mapping, HttpServletRequest request) {
		 this.set("filterNonTextualAttachments",false);
	 }
}
