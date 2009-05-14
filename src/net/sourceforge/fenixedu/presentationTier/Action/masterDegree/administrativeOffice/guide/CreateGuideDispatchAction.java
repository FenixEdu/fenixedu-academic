/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoActiveStudentCurricularPlanServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingContributorServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.ChangePersonPassword;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.CreateCandidateSituation;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.PrepareCreateGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate.ReadCertificateList;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateReceiptBean;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSituationActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoActiveStudentCurricularPlanActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.RandomStringGenerator;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *         This is the Action to create a Guide
 * 
 */

public class CreateGuideDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaActionForm createGuideForm = (DynaActionForm) form;

	// Transport chosen Execution Degree
	String executionDegreeIDParam = getFromRequest(PresentationConstants.EXECUTION_DEGREE_OID, request);
	Integer executionDegreeID = Integer.valueOf(executionDegreeIDParam);
	createGuideForm.set("executionDegreeID", executionDegreeID);
	request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, executionDegreeID);

	InfoExecutionDegree infoExecutionDegree = null;
	infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegreeID);
	if (infoExecutionDegree != null) {
	    request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
	}

	// Contributor
	String unexistinngContributor = getFromRequest(PresentationConstants.UNEXISTING_CONTRIBUTOR, request);
	if (unexistinngContributor != null && unexistinngContributor.length() > 0) {
	    request.setAttribute(PresentationConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE.toString());
	}

	request.setAttribute("chooseContributorBean", new CreateReceiptBean());

	return mapping.findForward("PrepareSuccess");

    }

    public ActionForward requesterChosen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	DynaActionForm createGuideForm = (DynaActionForm) form;

	// Get the Information
	Integer executionDegreeID = (Integer) createGuideForm.get("executionDegreeID");

	// requester
	String graduationType = (String) createGuideForm.get("graduationType");
	String numberString = (String) createGuideForm.get("number");
	Integer number = new Integer(numberString);
	String requesterType = (String) createGuideForm.get("requester");

	InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(RootDomainObject.getInstance()
		.readExecutionDegreeByOID(executionDegreeID));

	List types = new ArrayList();
	// types.add(DocumentType.INSURANCE_TYPE);
	types.add(DocumentType.CERTIFICATE);
	types.add(DocumentType.ENROLMENT);
	types.add(DocumentType.EMOLUMENT);
	types.add(DocumentType.FINE);
	types.add(DocumentType.CERTIFICATE_OF_DEGREE);
	types.add(DocumentType.ACADEMIC_PROOF_EMOLUMENT);
	types.add(DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS);
	// types.add(DocumentType.GRATUITY_TYPE);

	List studentGuideList = null;
	try {
	    studentGuideList = ReadCertificateList.run(GraduationType.MASTER_DEGREE, types);

	} catch (NonExistingServiceException e) {
	    e.printStackTrace();
	    throw new NonExistingActionException("A lista de guias para estudantes", e);

	} catch (FenixServiceException e) {
	    e.printStackTrace();
	    throw new FenixActionException(e);
	}
	request.setAttribute(PresentationConstants.CERTIFICATE_LIST, studentGuideList);

	final CreateReceiptBean chooseContributorBean = (CreateReceiptBean) RenderUtils.getViewState("chooseContributorBean")
		.getMetaObject().getObject();
	Party contributorParty = chooseContributorBean.getContributorParty();

	InfoGuide infoGuide = null;
	try {

	    infoGuide = PrepareCreateGuide.run(graduationType, infoExecutionDegree, number, requesterType, contributorParty);
	} catch (ExistingServiceException e) {
	    e.printStackTrace();
	    throw new ExistingActionException("O Contribuinte", e);
	} catch (NoActiveStudentCurricularPlanServiceException e) {
	    e.printStackTrace();
	    throw new NoActiveStudentCurricularPlanActionException(e);
	} catch (NonExistingContributorServiceException e) {
	    request.setAttribute(PresentationConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE.toString());
	    request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, executionDegreeID);

	    return mapping.getInputForward();
	} catch (NonExistingServiceException e) {
	    e.printStackTrace();
	    ActionError actionError = new ActionError("error.nonExisting.requester");
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("Unknown", actionError);
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();
	} catch (FenixServiceException e) {
	    e.printStackTrace();
	    throw new FenixActionException(e);
	}
	request.setAttribute(PresentationConstants.GUIDE, infoGuide);

	request.setAttribute(PresentationConstants.REQUESTER_NUMBER, number);
	request.setAttribute("graduationType", graduationType);
	request.setAttribute(PresentationConstants.REQUESTER_TYPE, requesterType);

	if (requesterType.equals(GuideRequester.CANDIDATE.name())) {
	    request.removeAttribute(PresentationConstants.REQUESTER_TYPE);
	    generateToken(request);
	    saveToken(request);

	    return mapping.findForward("CreateCandidateGuide");
	}

	if (requesterType.equals(GuideRequester.STUDENT.name())) {
	    return mapping.findForward("CreateStudentGuide");
	}

	throw new FenixActionException("Unknown requester type!");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	if (!isTokenValid(request)) {
	    return mapping.findForward("BackError");
	}
	generateToken(request);
	saveToken(request);

	DynaActionForm createGuideForm = (DynaActionForm) form;

	// Get the information
	String othersRemarks = (String) createGuideForm.get("othersRemarks");
	String othersPriceString = (String) createGuideForm.get("othersPrice");
	String remarks = (String) createGuideForm.get("remarks");
	String guideSituationString = (String) createGuideForm.get("guideSituation");
	String paymentType = (String) createGuideForm.get("paymentType");

	String graduationType = (String) request.getAttribute("graduationType");
	if (graduationType == null)
	    graduationType = request.getParameter("graduationType");
	request.setAttribute("graduationType", graduationType);

	Double othersPrice = null;

	try {
	    if ((othersPriceString != null) && (othersPriceString.length() != 0)) {
		othersPrice = new Double(othersPriceString);
		if (othersPrice.floatValue() < 0) {
		    throw new NumberFormatException();
		}
	    }
	} catch (NumberFormatException e) {
	    throw new InvalidInformationInFormActionException(new Throwable());
	}

	// Check if the Guide will have a "Payed" situation and if the payment
	// type has been chosen

	if ((guideSituationString.equals(GuideState.PAYED)) && (paymentType.equals(""))) {
	    ActionError actionError = new ActionError("error.paymentTypeRequired");
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("Unknown", actionError);
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();
	}

	GuideState situationOfGuide = GuideState.valueOf(guideSituationString);
	InfoGuide infoGuide = (InfoGuide) request.getAttribute(PresentationConstants.GUIDE);

	InfoGuide newInfoGuide = null;

	try {

	    newInfoGuide = CreateGuide.run(infoGuide, othersRemarks, othersPrice, remarks, situationOfGuide, paymentType);
	} catch (InvalidSituationServiceException e) {
	    Object object = new Object();
	    object = "Anulada";
	    throw new InvalidSituationActionException(object);
	} catch (NonExistingContributorServiceException e) {
	    request.setAttribute(PresentationConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE.toString());
	    return mapping.getInputForward();
	}

	// Check if it's necessary to create a password for the candidate And to
	// change his situation
	String requesterType = (String) request.getAttribute(PresentationConstants.REQUESTER_TYPE);
	if (requesterType == null)
	    requesterType = request.getParameter(PresentationConstants.REQUESTER_TYPE);
	if (requesterType == null)
	    requesterType = (String) createGuideForm.get("requester");

	// We need to check if the Guide has been payed
	if (requesterType.equals(GuideRequester.CANDIDATE.name())) {

	    if (situationOfGuide.equals(GuideState.PAYED)) {

		// The Candidate will now have a new Situation

		CreateCandidateSituation.run(newInfoGuide.getInfoExecutionDegree().getIdInternal(), newInfoGuide.getInfoPerson()
			.getIdInternal(), new SituationName(SituationName.PENDENTE_STRING));

		if ((newInfoGuide.getInfoPerson().getPassword() == null)
			|| (newInfoGuide.getInfoPerson().getPassword().length() == 0)) {
		    // Generate and write the password

		    try {

			ChangePersonPassword.run(newInfoGuide.getInfoPerson().getIdInternal(), RandomStringGenerator
				.getRandomStringGenerator(8));
		    } catch (FenixServiceException e) {
			throw new FenixActionException();
		    }

		    request.setAttribute(PresentationConstants.PRINT_PASSWORD, Boolean.TRUE);

		} else {
		    request.setAttribute(PresentationConstants.PRINT_INFORMATION, Boolean.TRUE);
		}
	    }
	}
	request.setAttribute(PresentationConstants.GUIDE, newInfoGuide);

	if (request.getParameter(PresentationConstants.REQUESTER_NUMBER) != null) {
	    Integer numberRequester = new Integer(request.getParameter(PresentationConstants.REQUESTER_NUMBER));
	    request.setAttribute(PresentationConstants.REQUESTER_NUMBER, numberRequester);
	}
	return mapping.findForward("CreateSuccess");

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {

	String parameterString = request.getParameter(parameter);
	if (parameterString == null) {
	    parameterString = (String) request.getAttribute(parameter);
	}
	return parameterString;
    }
}