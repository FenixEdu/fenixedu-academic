/*
 * 
 * Created on 27 of March de 2003
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
 
package ServidorApresentacao.Action.masterDegree.candidate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class ReadPersonCandidatesAction extends ServidorApresentacao.Action.base.FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

	SessionUtils.validSessionVerification(request, mapping);

	HttpSession session = request.getSession(false);
	if (session != null) {
      IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
      GestorServicos gestor = GestorServicos.manager();
	  
      Object args[] = new Object[1];
	  args[0] = userView;
	  
      List candidates = (List) gestor.executar(userView, "ReadPersonCandidates", args);
	  if (candidates.size() == 1) {
	  	session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, (InfoMasterDegreeCandidate) candidates.get(0));
		return mapping.findForward("Success");
	  }
	  	 
      session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST, candidates);
      return mapping.findForward("ChooseCandidate");

    } else
      throw new Exception();   
  }

}
