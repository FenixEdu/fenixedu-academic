package ServidorApresentacao.Action.masterDegree.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class PrepareCandidateApprovalDispatchAction extends DispatchAction {

  public ActionForward chooseExecutionDegree(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	  throws Exception {


	
	  HttpSession session = request.getSession(false);
	
	  InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);
 
	  request.setAttribute("degree", infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
	  request.setAttribute("executionYear", infoExecutionDegree.getInfoExecutionYear().getYear());
 	  
	  
	  return mapping.findForward("ExecutionDegreeChosen");
  }
   
}
