/*
 * Created on 14/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoContributor;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGuide;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingContributorServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.InvalidSituationActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.GuideRequester;
import Util.PaymentType;
import Util.SituationOfGuide;
import Util.Specialization;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Guide
 * 
 */
public class CreateGuideDispatchAction extends DispatchAction {

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
						
						
			List result = null; 			
			try {
				result = (List) serviceManager.executar(userView, "ReadAllContributors", null);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			
			ArrayList contributorList = new ArrayList();
			Iterator iterator = result.iterator();
			while (iterator.hasNext()) {
				InfoContributor infoContributor = (InfoContributor) iterator.next();
				contributorList.add(new LabelValueBean(infoContributor.getContributorName(), infoContributor.getContributorNumber().toString()));
			}

			session.setAttribute(SessionConstants.CONTRIBUTOR_LIST, contributorList);
			
			session.setAttribute(SessionConstants.GUIDE_REQUESTER_LIST, GuideRequester.toArrayList());
			
			return mapping.findForward("PrepareSuccess");
		  } else
			throw new Exception();   

	}
		

	public ActionForward requesterChosen(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm createGuideForm = (DynaActionForm) form;

			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			String graduationType = (String) createGuideForm.get("graduationType");
			String degree = (String) createGuideForm.get("degree");
			String numberString = (String) createGuideForm.get("number");
			
			Integer number = new Integer(numberString);
			String requesterType = (String) createGuideForm.get("requester");
			
			String contributorNumberString = (String) createGuideForm.get("contributorNumber");
			String contributorList = (String) createGuideForm.get("contributorList");
			
			Integer contributorNumber = null;
			Integer contributorNumberFromList = null;
			
			if ((contributorList != null) && (contributorList.length() > 0))
				contributorNumberFromList = new Integer(contributorList);
			
			if ((contributorNumberString != null) && (contributorNumberString.length() > 0))
				contributorNumber = new Integer(contributorNumberString);
			
			ArrayList degrees = (ArrayList) session.getAttribute(SessionConstants.DEGREE_LIST);

			// Verify the chosen degree			
			Iterator iterator = degrees.iterator();
			InfoExecutionDegree infoExecutionDegree = null; 
			while (iterator.hasNext()){
				InfoExecutionDegree infoExecutionDegreeTemp = (InfoExecutionDegree) iterator.next(); 
				if (infoExecutionDegreeTemp.getInfoDegreeCurricularPlan().getInfoDegree().getNome().equals(degree))
					infoExecutionDegree = infoExecutionDegreeTemp;					
			}
			String contributorName = (String) createGuideForm.get("contributorName");
			String contributorAddress = (String) createGuideForm.get("contributorAddress");
			
			Integer contributorNumberToRead = null;
			InfoContributor infoContributor = null;			
			if(contributorNumber != null) contributorNumberToRead = contributorNumber;
			if(contributorNumberFromList != null) contributorNumberToRead = contributorNumberFromList;

			InfoGuide infoGuide = null;

			try {
				Object args[] = {graduationType, infoExecutionDegree, number, requesterType, contributorNumberToRead, contributorName, contributorAddress };
				infoGuide = (InfoGuide) serviceManager.executar(userView, "PrepareCreateGuide", args);
			} catch (NonExistingContributorServiceException e) {
				session.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE);
				return mapping.getInputForward();
			} catch (NonExistingServiceException e) {
				ActionError actionError = new ActionError("error.nonExisting.requester");
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add("Unknown", actionError);
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
			
			session.removeAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR);
			session.setAttribute(SessionConstants.GUIDE, infoGuide);
			
			session.setAttribute(SessionConstants.PAYMENT_TYPE, PaymentType.toArrayList());
			session.setAttribute(SessionConstants.GUIDE_SITUATION_LIST, SituationOfGuide.toArrayList());

			
			if (requesterType.equals(GuideRequester.CANDIDATE_STRING)){
				return mapping.findForward("CreateCandidateGuide");
			}			
			
			if (requesterType.equals(GuideRequester.STUDENT_STRING))
				return mapping.findForward("ChooseItems");

			return null;
		} else throw new Exception();   
	  }
	  
	public ActionForward create(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm createGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
				
			// Get the information

			String othersRemarks = (String) createGuideForm.get("othersRemarks");
			String othersPriceString = (String) createGuideForm.get("othersPrice");
			String remarks = (String) createGuideForm.get("remarks");
			String guideSituationString = (String) createGuideForm.get("guideSituation");
			Integer paymentType = (Integer) createGuideForm.get("paymentType");
			
			Double othersPrice = null;
			if ((othersPriceString != null) && (othersPriceString.length() != 0))
				othersPrice = new Double(othersPriceString);
				
			SituationOfGuide situationOfGuide = new SituationOfGuide(new Integer(guideSituationString));
			
			InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);
	
			InfoGuide newInfoGuide = null;
			
			try {
				Object args[] = {infoGuide, othersRemarks, othersPrice, remarks, situationOfGuide, paymentType};
				newInfoGuide = (InfoGuide) serviceManager.executar(userView, "CreateGuide", args);
			} catch (InvalidSituationServiceException e) {
				Object object = new Object();
				object = "Anulada";
				throw new InvalidSituationActionException(object);
			} catch (NonExistingContributorServiceException e) {
				session.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE);
				return mapping.getInputForward();
			}
						
			session.removeAttribute(SessionConstants.GUIDE);
			session.setAttribute(SessionConstants.GUIDE, newInfoGuide);
			
			return mapping.findForward("CreateSuccess");
			
		} else throw new Exception();
	}
}
