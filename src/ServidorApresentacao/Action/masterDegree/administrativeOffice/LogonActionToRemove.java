/*
 * Created on 15/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Nuno Nunes
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LogonActionToRemove extends ServidorApresentacao.Action.FenixAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response)
		throws Exception {
			HttpSession sessao = request.getSession(true);

			sessao.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));
   
 System.out.println("ACTION TO REMOVE !!! REMOVE ME !!!");
 
			return mapping.findForward("Success");

		}

}
