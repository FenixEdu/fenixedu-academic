/*
 * LogOffAction.java
 *
 * 
 * Created on 09 de Dezembro de 2002, 16:37
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
 
package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogOffAction extends ServidorApresentacao.Action.FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

	HttpSession session = request.getSession(false);
	if (session != null){
	  session.setMaxInactiveInterval(0);
      session.invalidate();
	}
    return mapping.findForward("Success");
  }
}
