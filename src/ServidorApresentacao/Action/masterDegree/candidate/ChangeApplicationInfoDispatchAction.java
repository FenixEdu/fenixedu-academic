/*
 * ChangeCandidateApplicationFormAction.java
 *
 * 
 * Created on 14 de Dezembro de 2002, 12:31
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.SituationName;

public class ChangeApplicationInfoDispatchAction extends DispatchAction {

  public ActionForward change(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	  throws Exception {


	
	HttpSession session = request.getSession(false);
	
	if (session != null) {
	  DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
	  IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	  GestorServicos gestor = GestorServicos.manager();
 
 	  Object changeArgs[] = new Object[1];
	  InfoMasterDegreeCandidate newMasterDegreeCandidate = (InfoMasterDegreeCandidate) session.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);
      
	  newMasterDegreeCandidate.setAverage(Double.valueOf((String) changeApplicationInfoForm.get("average")));
	  newMasterDegreeCandidate.setMajorDegree((String) changeApplicationInfoForm.get("majorDegree"));
	  newMasterDegreeCandidate.setMajorDegreeSchool((String) changeApplicationInfoForm.get("majorDegreeSchool"));
	  newMasterDegreeCandidate.setMajorDegreeYear((Integer) changeApplicationInfoForm.get("majorDegreeYear"));
	             
	  changeArgs[0] = newMasterDegreeCandidate;

	  gestor.executar(userView, "ChangeApplicationInfo", changeArgs);

	  session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);
	  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, newMasterDegreeCandidate);
 
	  return mapping.findForward("Success");
	} else
	  throw new Exception();  
  }
  
  
  
  public ActionForward prepare(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	throws Exception {

		
		HttpSession session = request.getSession(false);
	  	
		if (session != null) {
		  DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
		  InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) session.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE); 

		  if (!infoMasterDegreeCandidate.getInfoCandidateSituation().getSituation().equals(SituationName.PENDENTE_STRING)){
		  	session.setAttribute(SessionConstants.CANDIDATE_SITUATION, infoMasterDegreeCandidate.getInfoCandidateSituation());
			return mapping.findForward("Unchangeable");
		  }
			

		  changeApplicationInfoForm.set("majorDegree", infoMasterDegreeCandidate.getMajorDegree());
		  changeApplicationInfoForm.set("majorDegreeSchool", infoMasterDegreeCandidate.getMajorDegreeSchool());
		  changeApplicationInfoForm.set("majorDegreeYear", infoMasterDegreeCandidate.getMajorDegreeYear());
		  if (infoMasterDegreeCandidate.getAverage() != null)
		  	changeApplicationInfoForm.set("average", infoMasterDegreeCandidate.getAverage().toString());
		  		  
		  return mapping.findForward("prepareReady");
		} else
		  throw new Exception();  
	}

  
}
