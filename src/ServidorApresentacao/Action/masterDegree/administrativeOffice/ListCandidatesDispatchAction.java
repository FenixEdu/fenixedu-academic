/*
 * Created on 14/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;

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
import DataBeans.InfoPerson;
import Dominio.CandidateSituation;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.SituationName;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Master Degree Candidate
 * 
 */
public class ListCandidatesDispatchAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
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
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}

			session.setAttribute(SessionConstants.DEGREE_LIST, degreeList);
						
			// Create the Candidate Situation List
			session.setAttribute(SessionConstants.CANDIDATE_SITUATION_LIST, SituationName.toArrayList());  
			
			return mapping.findForward("PrepareSuccess");
		  } else
			throw new Exception();   

	}
		

	public ActionForward create(ActionMapping mapping, ActionForm form,
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
			String degreeType = (String) createCandidateForm.get("specialization");
			String degreeName = (String) createCandidateForm.get("degree");
			String name = (String) createCandidateForm.get("name");
			String identificationDocumentNumber = (String) createCandidateForm.get("identificationDocumentNumber");
			String identificationDocumentType = (String) createCandidateForm.get("identificationDocumentType");

			ArrayList degrees = (ArrayList) session.getAttribute(SessionConstants.DEGREE_LIST);
			
			Iterator iterator = degrees.iterator();
			InfoExecutionDegree infoExecutionDegree = null; 
			while (iterator.hasNext()){
				InfoExecutionDegree infoExecutionDegreeTemp = (InfoExecutionDegree) iterator.next(); 
				if (infoExecutionDegreeTemp.getInfoDegreeCurricularPlan().getInfoDegree().getNome().equals(degreeName))
					infoExecutionDegree = infoExecutionDegreeTemp;					
			}


			// Create the new Master Degree Candidate
			InfoMasterDegreeCandidate newMasterDegreeCandidate = new InfoMasterDegreeCandidate();
			newMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

			InfoPerson infoPerson = new InfoPerson();
			infoPerson.setNome(name);
			infoPerson.setNumeroDocumentoIdentificacao(identificationDocumentNumber);
			infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(identificationDocumentType));

			newMasterDegreeCandidate.setSpecialization(degreeType);
			newMasterDegreeCandidate.setInfoPerson(infoPerson);
			
			Object args[] = { newMasterDegreeCandidate };
	  
	  		InfoMasterDegreeCandidate createdCandidate = null;
			try {
				createdCandidate = (InfoMasterDegreeCandidate) serviceManager.executar(userView, "CreateMasterDegreeCandidate", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException("O Candidato", e);
			}
		  session.setAttribute(SessionConstants.NEW_MASTER_DEGREE_CANDIDATE, createdCandidate);
		  
		  return mapping.findForward("CreateSuccess");
		} else
		  throw new Exception();   
	  }
	  
}
