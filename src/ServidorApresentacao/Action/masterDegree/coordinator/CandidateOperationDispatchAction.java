package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class CandidateOperationDispatchAction extends DispatchAction {

  public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	  throws Exception {


	SessionUtils.validSessionVerification(request, mapping);
	HttpSession session = request.getSession(false);
	
	if (session != null) {
	  DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
	  IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	  GestorServicos gestor = GestorServicos.manager();
 
 	  InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);
 
 	  List candidates = null;
 	  Object args[] = {infoExecutionDegree}; 
 
	  try {
		candidates = (List) gestor.executar(userView, "ReadDegreeCandidates", args);
	  } catch (FenixServiceException e) {
		  throw new NonExistingActionException("error.exception.nonExistingCandidates","", e);
	  }	  

	  session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST);
	  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST, candidates);
 
	  return mapping.findForward("ViewList");
	} else
	  throw new Exception();  
  }
  
  
  public ActionForward chooseCandidate(ActionMapping mapping, ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response)
	  throws Exception {

	  SessionUtils.validSessionVerification(request, mapping);
	  HttpSession session = request.getSession(false);

	  if (session != null) {
			
		  DynaActionForm listCandidatesForm = (DynaActionForm) form;


		  GestorServicos serviceManager = GestorServicos.manager();
			
		  IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		  List candidateList = (List) session.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST);
			

		  Integer choosenCandidatePosition = Integer.valueOf(request.getParameter("candidatePosition"));
			
			
		  // Put the selected Candidate in Session
		  InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) candidateList.get(choosenCandidatePosition.intValue());
		
		  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
		  return mapping.findForward("ActionReady");
			
	  } else
		  throw new Exception();  
  }
	   

}
