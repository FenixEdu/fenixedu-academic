/*
 * Created on 13/Mar/2003
 *
 */
package ServidorApresentacao.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorApresentacao.Action.exceptions.InvalidPasswordActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangePersonPasswordAction extends ServidorApresentacao.Action.base.FenixAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		  throws Exception {

	    SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm changePasswordForm = (DynaActionForm) form;
			IUserView userView = (IUserView) session.getAttribute("UserView");
			String oldPassword = (String) changePasswordForm.get("oldPassword");
			String newPassword = (String) changePasswordForm.get("newPassword");

			// Check the old Password
			
			InfoMasterDegreeCandidate masterDegreeCandidate = null;
		  
			Object args[] = { userView, oldPassword, newPassword };
		  
			try {
				ServiceUtils.executeService(userView, "ChangePasswordService", args);
			} catch (InvalidPasswordServiceException e) {
				throw new InvalidPasswordActionException(e);
			} 
		  return mapping.findForward("Success");
		} else
		  throw new Exception();   
	  }

}
