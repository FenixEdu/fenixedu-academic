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
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
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
public class CreateCandidateDispatchAction extends DispatchAction {


	public ActionForward prepareChooseExecutionYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm chooseExecutionYearForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get Execution Year List
			
			ArrayList executionYearList = null; 			
			try {
				executionYearList = (ArrayList) serviceManager.executar(userView, "ReadExecutionYears", null);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}

			request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearList);
						
			return mapping.findForward("PrepareSuccess");
		  } else
			throw new Exception();   

	}



	public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm chooseExecutionYearForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			request.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
						
			return mapping.findForward("CreateReady");
		  } else
			throw new Exception();   
	}


	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
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
			Object args[] = { request.getAttribute(SessionConstants.EXECUTION_YEAR) } ;
			
			try {
				degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}

			session.setAttribute(SessionConstants.DEGREE_LIST, degreeList);

						
			// Create the type of Identification Document
			session.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST, TipoDocumentoIdentificacao.toArrayList());  
			
			return mapping.findForward("PrepareSuccess");
		  } else
			throw new Exception();   

	}
		

	public ActionForward create(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
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
