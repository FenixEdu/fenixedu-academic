package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class CandidateOperationDispatchAction extends DispatchAction {

  public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	  throws Exception {


	
	HttpSession session = request.getSession(false);
	
	if (session != null) {
	  IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	  GestorServicos gestor = GestorServicos.manager();
 
 	  InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);
 
 	  List candidates = null;
 	  Object args[] = {infoExecutionDegree}; 
 
	  try {
		candidates = (List) gestor.executar(userView, "ReadDegreeCandidates", args);
	  } catch (FenixServiceException e) {
		  throw new FenixActionException(e);
	  }	  
	  
	  if (candidates.size() == 0)
	  	throw new NonExistingActionException("error.exception.nonExistingCandidates","", null);

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

	  
	  HttpSession session = request.getSession(false);

	  if (session != null) {
			
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
