/*
 * VisualizeApplicationInfoAction.java
 *
 * 
 * Created on 07 de Dezembro de 2002, 11:16
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
 
package ServidorApresentacao.Action.masterDegree.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class VisualizeApplicationInfoAction extends ServidorApresentacao.Action.FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

	SessionUtils.validSessionVerification(request, mapping);

	HttpSession session = request.getSession(false);
	if (session != null) {
      IUserView userView = (IUserView) session.getAttribute("UserView");
      GestorServicos gestor = GestorServicos.manager();
	  
      Object args[] = new Object[1];
	  args[0] = userView;
	  
      InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) gestor.executar(userView, "ReadActiveCandidateSituation", args);
	  
	   
      request.setAttribute("applicationInfo", infoMasterDegreeCandidate);
      return mapping.findForward("Success");
    } else
      throw new Exception();   
  }

}
