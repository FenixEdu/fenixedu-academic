/*
 * Created on 14/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

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
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.SituationName;
import Util.Specialization;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class ListCandidatesDispatchAction extends DispatchAction {

	public ActionForward prepareChoose(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm createCandidateForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Create the Degree Type List
			ArrayList specializations = Specialization.toArrayList();
			session.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
			
			// Get the Degree List
			
			ArrayList degreeList = null; 			
			try {
				degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", null);
			} catch (Exception e) {
				throw new Exception(e);
			}

			session.setAttribute(SessionConstants.DEGREE_LIST, degreeList);
						
			// Create the Candidate Situation List
			session.setAttribute(SessionConstants.CANDIDATE_SITUATION_LIST, SituationName.toArrayList());  
			
			return mapping.findForward("PrepareReady");
		  } else
			throw new Exception();   

	}
		

	public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm createCandidateForm = (DynaActionForm) form;

			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			String degreeTypeTemp = (String) createCandidateForm.get("specialization");
			String degreeName = (String) createCandidateForm.get("degree");
			String candidateSituationTemp = (String) createCandidateForm.get("candidateSituation");
			String candidateNumberTemp = (String) createCandidateForm.get("candidateNumber");
			
			Integer candidateNumber = null;
			Specialization specialization = null;
			SituationName situationName = null;
			
			if (degreeName.length() == 0)
				degreeName = null;
			if (candidateNumberTemp.length() != 0)
				candidateNumber = Integer.valueOf(candidateNumberTemp);
			if (degreeTypeTemp != null && degreeTypeTemp.length() != 0)
				specialization = new Specialization(degreeTypeTemp);
			if (candidateSituationTemp != null && candidateSituationTemp.length() != 0)
				situationName = new SituationName(candidateSituationTemp);

			Object args[] = { degreeName, specialization, situationName, candidateNumber };
	  		List result = null;
	  		
	  		try {
				result = (List) serviceManager.executar(userView, "ReadCandidateList", args);
			} catch (Exception e) {
				throw new Exception(e);
			}

			if (result.size() == 1) {
				InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) result.get(0);
				session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
				return mapping.findForward("ActionReady");
			}
		  // Create find query String
		  String query = new String();
		  if (degreeName == null && specialization == null && situationName == null && candidateNumber == null)
		  	query = "  - Todos os criterios";
		  else if (degreeName != null) query += "\n  - Curso";
		  else if (specialization != null) query += "\n  - Tipo de Especialização: " + specialization.toString();
		  else if (situationName != null) query += "\n  - Situação do Candidato: " + situationName.toString();
		  else if (candidateNumber != null) query += "\n  - Número de Candidato: " + candidateNumber;
		  
		  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST, result);
		  session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_QUERY, query);
		  
		  return mapping.findForward("ChooseCandidate");
		} else
		  throw new Exception();   
	  }
	  
}
