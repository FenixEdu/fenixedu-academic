/*
 * Created on 14/Mar/2003
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

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
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGuide;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NoActiveStudentCurricularPlanServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingContributorServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidInformationInFormActionException;
import ServidorApresentacao.Action.exceptions.InvalidSituationActionException;
import ServidorApresentacao.Action.exceptions.NoActiveStudentCurricularPlanActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.DocumentType;
import Util.GraduationType;
import Util.GuideRequester;
import Util.PaymentType;
import Util.RandomStringGenerator;
import Util.SituationOfGuide;
import Util.Specialization;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Guide
 *  
 */

public class CreateGuideDispatchAction extends DispatchAction
{

    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        DynaActionForm createGuideForm = (DynaActionForm) form;

        if (session != null)
        {

            session.removeAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR);

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            // Create the Degree Type List
            ArrayList specializations = Specialization.toArrayList();
            session.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
            String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

            // Transport chosen Execution Degree
            String executionDegreeIDParam =
                getFromRequest(SessionConstants.EXECUTION_DEGREE_OID, request);
            Integer executionDegreeID = Integer.valueOf(executionDegreeIDParam);
            createGuideForm.set("executionDegreeID", executionDegreeID);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, executionDegreeID);

            InfoExecutionDegree infoExecutionDegree = null;
            try
            {
                Object[] readExecutionDegreeArgs = { executionDegreeID };
                infoExecutionDegree =
                    (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        userView,
                        "ReadExecutionDegreeByOID",
                        readExecutionDegreeArgs);
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);

            }
            
            if (infoExecutionDegree != null)
            {
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
            }

//            // Get the Degree List
//
//            Object args[] = { executionYear };
//
//            ArrayList degreeList = null;
//            try
//            {
//                degreeList =
//                    (ArrayList) ServiceManagerServiceFactory.executeService(
//                        userView,
//                        "ReadMasterDegrees",
//                        args);
//            }
//            catch (ExistingServiceException e)
//            {
//                throw new ExistingActionException(e);
//            }
//
//            //BeanComparator nameComparator = new
//            // BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
//            //Collections.sort(degreeList, nameComparator);
//            Collections.sort(degreeList, new ComparatorByNameForInfoExecutionDegree());
//            List newDegreeList = degreeList;
//            List executionDegreeLabels = buildExecutionDegreeLabelValueBean(newDegreeList);
//
//            session.setAttribute(SessionConstants.DEGREE_LIST, executionDegreeLabels);
            session.removeAttribute(SessionConstants.PRINT_PASSWORD);
            session.removeAttribute(SessionConstants.PRINT_INFORMATION);

            List result = null;
            try
            {
                result =
                    (List) ServiceManagerServiceFactory.executeService(
                        userView,
                        "ReadAllContributors",
                        null);
            }
            catch (ExistingServiceException e)
            {
                throw new ExistingActionException(e);
            }

            ArrayList contributorList = new ArrayList();
            Iterator iterator = result.iterator();
            while (iterator.hasNext())
            {
                InfoContributor infoContributor = (InfoContributor) iterator.next();
                contributorList.add(
                    new LabelValueBean(
                        infoContributor.getContributorName(),
                        infoContributor.getContributorNumber().toString()));
            }

            session.setAttribute(SessionConstants.CONTRIBUTOR_LIST, contributorList);

            session.setAttribute(SessionConstants.GUIDE_REQUESTER_LIST, GuideRequester.toArrayList());
            session.setAttribute(SessionConstants.EXECUTION_YEAR, executionYear);

            return mapping.findForward("PrepareSuccess");
        }
        else
            throw new Exception();

    }

    public ActionForward requesterChosen(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {

            DynaActionForm createGuideForm = (DynaActionForm) form;
            session.removeAttribute(SessionConstants.CERTIFICATE_LIST);

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            // Get the Information
            String graduationType = (String) createGuideForm.get("graduationType");
            //String degree = (String) createGuideForm.get("degree");
            String numberString = (String) createGuideForm.get("number");

            Integer executionDegreeID = (Integer) createGuideForm.get("executionDegreeID");
            String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

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

            List types = new ArrayList();
            types.add(DocumentType.INSURANCE_TYPE);
            types.add(DocumentType.CERTIFICATE_TYPE);
            types.add(DocumentType.ENROLMENT_TYPE);
            types.add(DocumentType.EMOLUMENT_TYPE);
            types.add(DocumentType.FINE_TYPE);
            types.add(DocumentType.CERTIFICATE_OF_DEGREE_TYPE);
            types.add(DocumentType.ACADEMIC_PROOF_EMOLUMENT_TYPE);
            types.add(DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS_TYPE);
            types.add(DocumentType.GRATUITY_TYPE);

            InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
            try
            {
                Object args[] = { executionDegreeID };
                infoExecutionDegree =
                    (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        userView,
                        "ReadExecutionDegreeByOID",
                        args);
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

            //            try
            //            {
            //                Object args[] = { executionYear, degree };
            //                infoExecutionDegree =
            //                    (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
            //                        userView,
            //                        "ReadDegreeByYearAndCode",
            //                        args);
            //
            //            }
            //            catch (NonExistingServiceException e)
            //            {
            //                throw new NonExistingActionException("A lista de guias para estudantes", e);
            //            }
            //            catch (FenixServiceException e)
            //            {
            //                throw new FenixActionException(e);
            //            }

            Object argsAux[] = { GraduationType.MASTER_DEGREE_TYPE, types };

            List studentGuideList = null;

            try
            {
                studentGuideList =
                    (List) ServiceManagerServiceFactory.executeService(
                        userView,
                        "ReadCertificateList",
                        argsAux);

            }
            catch (NonExistingServiceException e)
            {
                throw new NonExistingActionException("A lista de guias para estudantes", e);
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

            session.setAttribute(SessionConstants.CERTIFICATE_LIST, studentGuideList);

            // Verify the chosen degree
            //            Iterator iterator = degrees.iterator();
            //            InfoExecutionDegree infoExecutionDegree = null;
            //            while (iterator.hasNext())
            //            {
            //			
            //                InfoExecutionDegree infoExecutionDegreeTemp = (InfoExecutionDegree) iterator.next();
            //                if (infoExecutionDegreeTemp
            //                    .getInfoDegreeCurricularPlan()
            //                    .getInfoDegree()
            //                    .getNome()
            //                    .equals(degree))
            //                    infoExecutionDegree = infoExecutionDegreeTemp;
            //            }
            String contributorName = (String) createGuideForm.get("contributorName");
            String contributorAddress = (String) createGuideForm.get("contributorAddress");

            Integer contributorNumberToRead = null;
            if (contributorNumber != null)
                contributorNumberToRead = contributorNumber;
            if (contributorNumberFromList != null)
                contributorNumberToRead = contributorNumberFromList;

            InfoGuide infoGuide = null;

            try
            {
                Object args[] =
                    {
                        graduationType,
                        infoExecutionDegree,
                        number,
                        requesterType,
                        contributorNumberToRead,
                        contributorName,
                        contributorAddress };
                infoGuide =
                    (InfoGuide) ServiceManagerServiceFactory.executeService(
                        userView,
                        "PrepareCreateGuide",
                        args);
            }
            catch (ExistingServiceException e)
            {
                throw new ExistingActionException("O Contribuinte", e);
            }
            catch (NoActiveStudentCurricularPlanServiceException e)
            {
                throw new NoActiveStudentCurricularPlanActionException(e);
            }
            catch (NonExistingContributorServiceException e)
            {
                session.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE);
                return mapping.getInputForward();
            }
            catch (NonExistingServiceException e)
            {
                ActionError actionError = new ActionError("error.nonExisting.requester");
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("Unknown", actionError);
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

            session.removeAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR);
            session.setAttribute(SessionConstants.GUIDE, infoGuide);

            session.setAttribute(SessionConstants.PAYMENT_TYPE, PaymentType.toArrayList());

            ArrayList guideSituations = new ArrayList();
            guideSituations.add(
                new LabelValueBean(
                    SituationOfGuide.NON_PAYED_STRING,
                    SituationOfGuide.NON_PAYED_STRING));
            guideSituations.add(
                new LabelValueBean(SituationOfGuide.PAYED_STRING, SituationOfGuide.PAYED_STRING));

            session.setAttribute(SessionConstants.GUIDE_SITUATION_LIST, guideSituations);

            session.setAttribute(SessionConstants.REQUESTER_NUMBER, number);
            request.setAttribute("graduationType", graduationType);

            if (requesterType.equals(GuideRequester.CANDIDATE_STRING))
            {
                session.removeAttribute(SessionConstants.REQUESTER_TYPE);
                session.setAttribute(SessionConstants.REQUESTER_TYPE, requesterType);
                generateToken(request);
                saveToken(request);

                return mapping.findForward("CreateCandidateGuide");
            }

            if (requesterType.equals(GuideRequester.STUDENT_STRING))
            {
                session.removeAttribute(SessionConstants.REQUESTER_TYPE);
                session.setAttribute(SessionConstants.REQUESTER_TYPE, requesterType);
                return mapping.findForward("CreateStudentGuide");
            }
            throw new FenixActionException("Unknown requester type!");
        }
        else
            throw new FenixActionException();
    }

    public ActionForward create(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (!isTokenValid(request))
        {
            return mapping.findForward("BackError");
        }
        else
        {
            generateToken(request);
            saveToken(request);
        }

        DynaActionForm createGuideForm = (DynaActionForm) form;
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String password = null;
        // Get the information

        session.removeAttribute(SessionConstants.PRINT_PASSWORD);
        session.removeAttribute(SessionConstants.PRINT_INFORMATION);

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

        try
        {
            if ((othersPriceString != null) && (othersPriceString.length() != 0))
            {
                othersPrice = new Double(othersPriceString);
                if (othersPrice.floatValue() < 0)
                {
                    throw new NumberFormatException();
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new InvalidInformationInFormActionException(new Throwable());
        }

        //			session.setAttribute(SessionConstants.PRINT_PASSWORD, Boolean.FALSE);

        // Check if the Guide will have a "Payed" situation and if the payment type has been chosen

        if ((guideSituationString.equals(SituationOfGuide.PAYED_STRING))
            && (paymentType.equals(PaymentType.DEFAULT_STRING)))
        {
            ActionError actionError = new ActionError("error.paymentTypeRequired");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("Unknown", actionError);
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        SituationOfGuide situationOfGuide = new SituationOfGuide(guideSituationString);
        InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);

        InfoGuide newInfoGuide = null;

        try
        {
            Object args[] =
                { infoGuide, othersRemarks, othersPrice, remarks, situationOfGuide, paymentType };
            newInfoGuide =
                (InfoGuide) ServiceManagerServiceFactory.executeService(userView, "CreateGuide", args);
        }
        catch (InvalidSituationServiceException e)
        {
            Object object = new Object();
            object = "Anulada";
            throw new InvalidSituationActionException(object);
        }
        catch (NonExistingContributorServiceException e)
        {
            session.setAttribute(SessionConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE);
            return mapping.getInputForward();
        }

        // Check if it's necessary to create a password for the candidate And to change his situation
        String requesterType = (String) session.getAttribute(SessionConstants.REQUESTER_TYPE);
        session.removeAttribute(SessionConstants.REQUESTER_TYPE);

        // We need to check if the Guide has been payed
        if (requesterType.equals(GuideRequester.CANDIDATE_STRING))
        {

            if (situationOfGuide.equals(SituationOfGuide.PAYED_TYPE))
            {

                // The Candidate will now have a new Situation

                try
                {
                    Object args[] =
                        { newInfoGuide.getInfoExecutionDegree(), newInfoGuide.getInfoPerson()};
                    ServiceManagerServiceFactory.executeService(
                        userView,
                        "CreateCandidateSituation",
                        args);
                }
                catch (FenixServiceException e)
                {
                    throw new FenixActionException();
                }

                if ((newInfoGuide.getInfoPerson().getPassword() == null)
                    || (newInfoGuide.getInfoPerson().getPassword().length() == 0))
                {
                    // Generate the password
                    password = RandomStringGenerator.getRandomStringGenerator(8);
                    newInfoGuide.getInfoPerson().setPassword(password);

                    // Write the Person
                    try
                    {
                        Object args[] = { newInfoGuide.getInfoPerson().getIdInternal(), password };
                        ServiceManagerServiceFactory.executeService(
                            userView,
                            "CreateCandidateSituation",
                            args);
                    }
                    catch (FenixServiceException e)
                    {
                        throw new FenixActionException();
                    }

                    // Put variable in Session to Inform that it's necessary to print the password

                    session.setAttribute(SessionConstants.PRINT_PASSWORD, Boolean.TRUE);

                }
                else
                {
                    session.setAttribute(SessionConstants.PRINT_INFORMATION, Boolean.TRUE);
                }
            }
        }
        session.removeAttribute(SessionConstants.GUIDE);
        session.setAttribute(SessionConstants.GUIDE, newInfoGuide);

        return mapping.findForward("CreateSuccess");

    }
    private List buildExecutionDegreeLabelValueBean(List executionDegreeList)
    {
        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name =
                infoExecutionDegree
                    .getInfoDegreeCurricularPlan()
                    .getInfoDegree()
                    .getTipoCurso()
                    .toString()
                    + " em "
                    + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
                ? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
                : "";

            executionDegreeLabels.add(
                new LabelValueBean(
                    name,
                    infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla()));
        }
        return executionDegreeLabels;
    }
    private boolean duplicateInfoDegree(
        List executionDegreeList,
        InfoExecutionDegree infoExecutionDegree)
    {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

    private String getFromRequest(String parameter, HttpServletRequest request)
    {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null)
        {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}
