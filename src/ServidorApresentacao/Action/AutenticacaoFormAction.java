package ServidorApresentacao.Action;

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
 * @author jorge
 */
public class AutenticacaoFormAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

    DynaActionForm autenticacaoForm = (DynaActionForm) form;
        
    GestorServicos gestor = GestorServicos.manager();
    Object argsAutenticacao[] = {autenticacaoForm.get("utilizador"), 
                                 autenticacaoForm.get("password")};
    IUserView userView = null; 
    userView = (IUserView) gestor.executar(null, "Autenticacao", argsAutenticacao);

    // Invalidate existing session if it exists
    HttpSession sessao = request.getSession(false);
    if(sessao != null) {
      sessao.invalidate();
    }
    
    // Create a new session for this user
    sessao = request.getSession(true);

    // Store the UserView into the session and return
    sessao.setAttribute("UserView", userView);   
 	sessao.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));
 
    return mapping.findForward("Docente");
  }
}
