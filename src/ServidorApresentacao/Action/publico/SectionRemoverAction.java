/*
 * Created on 9/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SectionRemoverAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,  
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			HttpSession session = getSession(request);
			session.removeAttribute(SessionConstants.INFO_SECTION);
			return mapping.findForward("sucess");
		}

}
