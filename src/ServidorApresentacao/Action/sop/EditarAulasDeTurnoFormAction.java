package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.FenixAction;

/**
 * @author tfc130
 */

public class EditarAulasDeTurnoFormAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
    
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
         /* Obtem o parametro do submit que indica a operacao a realizar */
         /* :FIXME: change this to international form */
        String parameter = request.getParameter(new String("operation"));
        
		return mapping.findForward(parameter);
		
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
 
}

