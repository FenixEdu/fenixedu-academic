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
 
package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class CoordinatedDegreeInfo extends FenixAction {


  public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	  throws Exception {
	

	HttpSession session = request.getSession(false);
	if (session != null) {
	  List degreeList = (List) session.getAttribute(SessionConstants.MASTER_DEGREE_LIST);
	  List candidates = (List) session.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATES_AMMOUNT);
			
	  Integer choosenDegreePosition = Integer.valueOf(request.getParameter("degree"));
			
	  // Put the selected Degree in Session
	  InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) degreeList.get(choosenDegreePosition.intValue());
	  request.setAttribute("infoExecutionDegree",infoExecutionDegree);	
	  session.setAttribute(SessionConstants.MASTER_DEGREE, infoExecutionDegree);
	  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT, candidates.get(choosenDegreePosition.intValue()));
	  return mapping.findForward("Success");
	} else
	  throw new Exception();   
  }
}
