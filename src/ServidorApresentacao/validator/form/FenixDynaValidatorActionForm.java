/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.validator.form
 * 
 * Created on 11/Dez/2002
 *
 */
package ServidorApresentacao.validator.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorActionForm;

/**
 * @author jpvl
 *
 *  @deprecated use  @link org.apache.struts.validator.DynaValidatorActionForm instead since struts 1.1 final
 */
public class FenixDynaValidatorActionForm
	extends DynaValidatorActionForm
	{

	/* (non-Javadoc)
	 * @see org.apache.struts.validator.DynaValidatorForm#getPage()
	 */
	public int getPage() {
		try {
			Integer pageInt = (Integer) super.get("page");			
			return pageInt == null ? 0 : pageInt.intValue();			
		} catch (Exception e) {
 			log("Getting the page number! Page property not defined.",e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.validator.DynaValidatorForm#setPage(int)
	 */
	public void setPage(int page) {
		try {
			super.set("page",new Integer(page));								
		} catch (Exception e) {
			log("Page setting ignored! Page property not defined.",e);
		}

	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {
		super.setPage(getPage());
		return super.validate(mapping, request);
	}

}
