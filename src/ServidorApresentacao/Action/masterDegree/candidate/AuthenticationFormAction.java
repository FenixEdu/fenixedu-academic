package ServidorApresentacao.Action.masterDegree.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Nuno Nunes & Joana Mota
 */


public class AuthenticationFormAction extends ServidorApresentacao.Action.FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

    DynaActionForm authenticationForm = (DynaActionForm) form;
        
    GestorServicos gestor = GestorServicos.manager();
    Object authenticationArgs[] = {authenticationForm.get("username"), 
                             	   authenticationForm.get("password")};

    IUserView userView = null; 
    userView = (IUserView) gestor.executar(null, "CandidateAuthentication", authenticationArgs);


    // Invalidate existing session if it exists
    HttpSession sessao = request.getSession(false);
    if(sessao != null) {
      sessao.invalidate();
    }
 
    // Create a new session for this user
    sessao = request.getSession(true);

    // Store the UserView into the session and return
	sessao.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));
    sessao.setAttribute("UserView", userView);   
 
    return mapping.findForward("Candidate");
  }
}
