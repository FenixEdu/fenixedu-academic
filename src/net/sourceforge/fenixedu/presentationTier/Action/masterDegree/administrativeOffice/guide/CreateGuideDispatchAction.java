/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoActiveStudentCurricularPlanServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingContributorServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSituationActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoActiveStudentCurricularPlanActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.RandomStringGenerator;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Guide
 *  
 */

public class CreateGuideDispatchAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        DynaActionForm createGuideForm = (DynaActionForm) form;

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            // Transport chosen Execution Degree
            String executionDegreeIDParam = getFromRequest(SessionConstants.EXECUTION_DEGREE_OID,
                    request);
            Integer executionDegreeID = Integer.valueOf(executionDegreeIDParam);
            createGuideForm.set("executionDegreeID", executionDegreeID);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, executionDegreeID);

            InfoExecutionDegree infoExecutionDegree = null;
            try {
                Object[] readExecutionDegreeArgs = { executionDegreeID };
                infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        userView, "ReadExecutionDegreeByOID", readExecutionDegreeArgs);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);

            }
            if (infoExecutionDegree != null) {
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
            }

            session.removeAttribute(SessionConstants.PRINT_PASSWORD);
            session.removeAttribute(SessionConstants.PRINT_INFORMATION);

            //Contributor
            String unexistinngContributor = getFromRequest(SessionConstants.UNEXISTING_CONTRIBUTOR,
                    request);
            if (unexistinngContributor != null && unexistinngContributor.length() > 0) {
                request.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE.toString());
            }

            //Although the chosen contributor doen't exists in the Database,
            //all contributors in Database are show
            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadAllContributors", null);
            } catch (ExistingServiceException e) {
                throw new ExistingActionException(e);
            }

            List contributorList = new ArrayList();
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                InfoContributor infoContributor = (InfoContributor) iterator.next();
                contributorList.add(new LabelValueBean("Nr. " + infoContributor.getContributorNumber() + ", " + infoContributor.getContributorName(), infoContributor.getContributorNumber().toString()));
            }
            request.setAttribute(SessionConstants.CONTRIBUTOR_LIST, contributorList);

            return mapping.findForward("PrepareSuccess");
        }
        throw new Exception();

    }

    public ActionForward requesterChosen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            //session.removeAttribute(SessionConstants.CERTIFICATE_LIST);

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            DynaActionForm createGuideForm = (DynaActionForm) form;

            // Get the Information
            Integer executionDegreeID = (Integer) createGuideForm.get("executionDegreeID");

            //requester
            String graduationType = (String) createGuideForm.get("graduationType");
            String numberString = (String) createGuideForm.get("number");
            Integer number = new Integer(numberString);
            String requesterType = (String) createGuideForm.get("requester");

            //contributor
            String contributorNumberString = (String) createGuideForm.get("contributorNumber");
            String contributorList = (String) createGuideForm.get("contributorList");

            Integer contributorNumber = null;
            Integer contributorNumberFromList = null;

            if ((contributorNumberString != null) && (contributorNumberString.length() > 0))
                contributorNumber = new Integer(contributorNumberString);

            if ((contributorList != null) && (contributorList.length() > 0))
                contributorNumberFromList = new Integer(contributorList);

            InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
            try {
                Object args[] = { executionDegreeID };
                infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        userView, "ReadExecutionDegreeByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }

            List types = new ArrayList();
            //types.add(DocumentType.INSURANCE_TYPE);
            types.add(DocumentType.CERTIFICATE);
            types.add(DocumentType.ENROLMENT);
            types.add(DocumentType.EMOLUMENT);
            types.add(DocumentType.FINE);
            types.add(DocumentType.CERTIFICATE_OF_DEGREE);
            types.add(DocumentType.ACADEMIC_PROOF_EMOLUMENT);
            types.add(DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS);
            //types.add(DocumentType.GRATUITY_TYPE);
            Object argsAux[] = { GraduationType.MASTER_DEGREE, types };
            List studentGuideList = null;
            try {
                studentGuideList = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadCertificateList", argsAux);

            } catch (NonExistingServiceException e) {
                e.printStackTrace();
                throw new NonExistingActionException("A lista de guias para estudantes", e);
            } catch (FenixServiceException e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
            session.setAttribute(SessionConstants.CERTIFICATE_LIST, studentGuideList);

            String contributorName = (String) createGuideForm.get("contributorName");
            String contributorAddress = (String) createGuideForm.get("contributorAddress");

            Integer contributorNumberToRead = null;
            if (contributorNumber != null)
                contributorNumberToRead = contributorNumber;
            if (contributorNumberFromList != null)
                contributorNumberToRead = contributorNumberFromList;

            InfoGuide infoGuide = null;
            try {
                Object args[] = { graduationType, infoExecutionDegree, number, requesterType,
                        contributorNumberToRead, contributorName, contributorAddress };

                infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                        "PrepareCreateGuide", args);
            } catch (ExistingServiceException e) {
                e.printStackTrace();
                throw new ExistingActionException("O Contribuinte", e);
            } catch (NoActiveStudentCurricularPlanServiceException e) {
                e.printStackTrace();
                throw new NoActiveStudentCurricularPlanActionException(e);
            } catch (NonExistingContributorServiceException e) {
                request.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE.toString());
                request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, executionDegreeID);

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
            session.setAttribute(SessionConstants.GUIDE, infoGuide);

            request.setAttribute(SessionConstants.REQUESTER_NUMBER, number);
            request.setAttribute("graduationType", graduationType);
            request.setAttribute(SessionConstants.REQUESTER_TYPE, requesterType);

            if (requesterType.equals(GuideRequester.CANDIDATE.name())) {
                session.removeAttribute(SessionConstants.REQUESTER_TYPE);
                generateToken(request);
                saveToken(request);

                return mapping.findForward("CreateCandidateGuide");
            }

            if (requesterType.equals(GuideRequester.STUDENT.name())) {

                session.removeAttribute(SessionConstants.REQUESTER_TYPE);
                return mapping.findForward("CreateStudentGuide");
            }

            throw new FenixActionException("Unknown requester type!");
        }
        throw new FenixActionException();
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (!isTokenValid(request)) {
            return mapping.findForward("BackError");
        }
        generateToken(request);
        saveToken(request);

        DynaActionForm createGuideForm = (DynaActionForm) form;
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String password = null;

        session.removeAttribute(SessionConstants.PRINT_PASSWORD);
        session.removeAttribute(SessionConstants.PRINT_INFORMATION);

        //Get the information
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

        //			session.setAttribute(SessionConstants.PRINT_PASSWORD, Boolean.FALSE);

        // Check if the Guide will have a "Payed" situation and if the payment
        // type has been chosen

        if ((guideSituationString.equals(GuideState.PAYED))
                && (paymentType.equals(""))) {
            ActionError actionError = new ActionError("error.paymentTypeRequired");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("Unknown", actionError);
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        GuideState situationOfGuide = GuideState.valueOf(guideSituationString);
        InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);

        InfoGuide newInfoGuide = null;

        try {
            Object args[] = { infoGuide, othersRemarks, othersPrice, remarks, situationOfGuide,
                    paymentType };
            newInfoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                    "CreateGuide", args);
        } catch (InvalidSituationServiceException e) {
            Object object = new Object();
            object = "Anulada";
            throw new InvalidSituationActionException(object);
        } catch (NonExistingContributorServiceException e) {
            //session.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR,
            // Boolean.TRUE);
            request.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE.toString());
            return mapping.getInputForward();
        }

        // Check if it's necessary to create a password for the candidate And to
        // change his situation
        String requesterType = (String) request.getAttribute(SessionConstants.REQUESTER_TYPE);
        if (requesterType == null)
            requesterType = request.getParameter(SessionConstants.REQUESTER_TYPE);
        if (requesterType == null)
            requesterType = (String) createGuideForm.get("requester");

        // We need to check if the Guide has been payed
        if (requesterType.equals(GuideRequester.CANDIDATE.name())) {

            if (situationOfGuide.equals(GuideState.PAYED)) {

                // The Candidate will now have a new Situation

                try {
                    Object args[] = { newInfoGuide.getInfoExecutionDegree().getIdInternal(),
                            newInfoGuide.getInfoPerson().getIdInternal(), new SituationName(SituationName.PENDENTE_STRING) };
                    ServiceManagerServiceFactory.executeService(userView, "CreateCandidateSituation",
                            args);
                } catch (FenixServiceException e) {
                    throw new FenixActionException();
                }

                if ((newInfoGuide.getInfoPerson().getPassword() == null)
                        || (newInfoGuide.getInfoPerson().getPassword().length() == 0)) {
                    // Generate the password
                    password = RandomStringGenerator.getRandomStringGenerator(8);
                    newInfoGuide.getInfoPerson().setPassword(password);

                    // Write the Person

                    try {
                        Object args[] = { newInfoGuide.getInfoPerson().getIdInternal(), password };
                        ServiceManagerServiceFactory.executeService(userView, "ChangePersonPassword",
                                args);
                    } catch (FenixServiceException e) {
                        throw new FenixActionException();
                    }

                    // Put variable in Session to Inform that it's necessary to
                    // print the password

                    session.setAttribute(SessionConstants.PRINT_PASSWORD, Boolean.TRUE);

                } else {
                    session.setAttribute(SessionConstants.PRINT_INFORMATION, Boolean.TRUE);
                }
            }
        }
        session.removeAttribute(SessionConstants.GUIDE);
        session.setAttribute(SessionConstants.GUIDE, newInfoGuide);

        if (request.getParameter(SessionConstants.REQUESTER_NUMBER) != null) {
            Integer numberRequester = new Integer(request
                    .getParameter(SessionConstants.REQUESTER_NUMBER));
            request.setAttribute(SessionConstants.REQUESTER_NUMBER, numberRequester);
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