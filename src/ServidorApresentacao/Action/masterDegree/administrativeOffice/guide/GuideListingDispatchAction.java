/*
 * Created on 14/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoDocumentoIdentificacao;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to Choose choose, visualize and edit a Guide.
 * 
 */
public class GuideListingDispatchAction extends DispatchAction {

	public ActionForward prepareChooseYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);



		if (session != null) {

			DynaActionForm chooseYearForm = (DynaActionForm) form;
//			session.removeAttribute(SessionConstants.REQUESTER_NUMBER);
//			session.removeAttribute(SessionConstants.GUIDE_LIST);
			chooseYearForm.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));			
			
			return mapping.findForward("PrepareReady");
		  } else
			throw new Exception();   

	}

	public ActionForward chooseYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm chooseGuideForm = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			Integer guideYear = new Integer((String) chooseGuideForm.get("year"));

			Object args[] = { guideYear };
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Guia", e);
			}

			request.setAttribute(SessionConstants.GUIDE_LIST, result);
		  
			return mapping.findForward("ShowGuideList");
		} else
		  throw new Exception();   
	  }


	public ActionForward chooseGuide(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer guideYear = new Integer(request.getParameter("year"));
			Integer guideNumber = new Integer(request.getParameter("number"));
				
			Object args[] = { guideNumber, guideYear };
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Guia", e);
			}

			request.setAttribute(SessionConstants.GUIDE_LIST, result);
			request.setAttribute(SessionConstants.GUIDE_NUMBER, guideNumber);
			request.setAttribute(SessionConstants.GUIDE_YEAR, guideYear);
		  
			return mapping.findForward("ShowVersionList");
		} else
		  throw new Exception();   
	  }


	public ActionForward prepareChoosePerson(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			// Create the type of Identification Document
			request.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST, TipoDocumentoIdentificacao.toArrayList());  
			
			return mapping.findForward("PrepareSuccess");
		  } else
			throw new Exception();   

	}
	
	public ActionForward getPersonGuideList(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			GestorServicos serviceManager = GestorServicos.manager();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			DynaActionForm choosePersonForm = (DynaActionForm) form;
			
			
			String identificationDocumentNumber = (String) choosePersonForm.get("identificationDocumentNumber");
			TipoDocumentoIdentificacao identificationDocumentType = new TipoDocumentoIdentificacao((String) choosePersonForm.get("identificationDocumentType"));
			
			Object args[] = { identificationDocumentNumber, identificationDocumentType };
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Pessoa", e);
			}
			
			if (result == null){
				throw new NonExistingActionException("error.exception.noGuidesForPerson", "Guias para esta pessoa");
			}
			
			request.setAttribute(SessionConstants.GUIDE_LIST, result);

			
			return mapping.findForward("ShowGuideList");
		  } else
			throw new Exception();   

	}


	public ActionForward chooseGuideByPerson(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			GestorServicos serviceManager = GestorServicos.manager();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			Integer personID = Integer.valueOf(request.getParameter("personID"));
			
			Object args[] = { personID};
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ChooseGuideByPersonID", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Pessoa", e);
			}
			
			if (result == null){
				throw new NonExistingActionException("error.exception.noGuidesForPerson", "Guias para esta pessoa");
			}
			
			request.setAttribute(SessionConstants.GUIDE_LIST, result);

			
			return mapping.findForward("ShowGuideList");
		  } else
			throw new Exception();   

	}

	
	  
}
