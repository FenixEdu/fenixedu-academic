/*
 * Created on 13/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorApresentacao.Action.exceptions.InvalidPasswordActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangeCandidatePasswordFormAction extends ServidorApresentacao.Action.FenixAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		  throws Exception {

	    SessionUtils.validSessionVerification(request, mapping);
		DynaActionForm changePasswordForm = (DynaActionForm) form;

		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView = (IUserView) session.getAttribute("UserView");
			String oldPassword = (String) changePasswordForm.get("oldPassword");
			String newPassword = (String) changePasswordForm.get("newPassword");

			// Check the old Password
			GestorServicos gestor = GestorServicos.manager();
			InfoMasterDegreeCandidate masterDegreeCandidate = null;
		  
			Object args[] = { userView, oldPassword, newPassword };
		  
			try {
				gestor.executar(userView, "ChangeCandidatePassword", args);
			} catch (InvalidPasswordServiceException e) {
				throw new InvalidPasswordActionException(e);
			} 
			
		  return mapping.findForward("Success");
		} else
		  throw new Exception();   
	  }

}
