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
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
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

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm createCandidateForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute("UserView");
			
			// Create the Degree Type List
			ArrayList specializations = Specialization.toArrayList();
			session.setAttribute("specializations", specializations);
			
			// Create the Degree List
			
			ArrayList degreeList = null; 			
			try {
				degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", null);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}

			ArrayList temp = new ArrayList();
			if (!degreeList.isEmpty()){
				Iterator iterator = degreeList.iterator();
				temp.add(new LabelValueBean("[Escolha um curso]", "-1"));
				while(iterator.hasNext()){
					String degreeName = (String) iterator.next();
					temp.add(new LabelValueBean(degreeName, degreeName)); 
				}
			}
			session.setAttribute("degreeList", temp);
			
			// Create the type of Identification Document
			session.setAttribute("identificationDocumentTypeList", TipoDocumentoIdentificacao.toArrayList());  
			
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
			
			IUserView userView = (IUserView) session.getAttribute("UserView");
			
			// Get the Information
			String degreeType = (String) createCandidateForm.get("specialization");
			String degreeName = (String) createCandidateForm.get("degree");
			String name = (String) createCandidateForm.get("name");
			String identificationDocumentNumber = (String) createCandidateForm.get("identificationDocumentNumber");
			String identificationDocumentType = (String) createCandidateForm.get("identificationDocumentType");

			// Create the new Master Degree Candidate
			InfoMasterDegreeCandidate newMasterDegreeCandidate = new InfoMasterDegreeCandidate();
			newMasterDegreeCandidate.setName(name);
			newMasterDegreeCandidate.setIdentificationDocumentNumber(identificationDocumentNumber);
			newMasterDegreeCandidate.setInfoIdentificationDocumentType(identificationDocumentType);
			newMasterDegreeCandidate.setSpecialization(degreeType);
			
			Object args[] = { newMasterDegreeCandidate , degreeName };
	  
	  		InfoMasterDegreeCandidate createdCandidate = null;
			try {
				createdCandidate = (InfoMasterDegreeCandidate) serviceManager.executar(userView, "CreateMasterDegreeCandidate", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException("O Candidato", e);
			}
		  session.setAttribute("newCandidate", createdCandidate);
		  
		  return mapping.findForward("CreateSuccess");
		} else
		  throw new Exception();   
	  }
	  
}
