/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ActiveStudentCurricularPlanAlreadyExistsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.GratuityValuesNotDefinedServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.GetBranchListByCandidateID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.GetCandidateRegistrationInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.GetCandidatesByID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.ReadCandidateForRegistration;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.RegisterCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ActiveStudentCurricularPlanAlreadyExistsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.GratuityValuesNotDefinedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidChangeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidStudentNumberActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class CandidateRegistrationDispatchAction extends FenixDispatchAction {

    public ActionForward getCandidateList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm candidateRegistration = (DynaActionForm) form;

        String executionYearString = request.getParameter("executionYear");
        String degreeCode = request.getParameter("degree");

        String executionDegree = request.getParameter("executionDegreeID");
        if ((executionYearString == null) || (executionYearString.length() == 0)) {
            executionYearString = (String) candidateRegistration.get("executionYear");
        }

        if ((degreeCode == null) || (degreeCode.length() == 0)) {
            degreeCode = (String) candidateRegistration.get("degreeCode");
        }

        IUserView userView = getUserView(request);

        List result = null;

        try {

            result = ReadCandidateForRegistration.run(executionDegree);
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("error.candidatesNotFound"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        BeanComparator nameComparator = new BeanComparator("infoPerson.nome");
        Collections.sort(result, nameComparator);

        request.setAttribute("candidateList", result);
        candidateRegistration.set("degreeCode", degreeCode);
        candidateRegistration.set("executionYear", executionYearString);
        candidateRegistration.set("candidateID", null);

        InfoExecutionDegree infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegree);

        request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        return mapping.findForward("ListCandidates");
    }

    public ActionForward prepareCandidateRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm candidateRegistration = (DynaActionForm) form;

        IUserView userView = getUserView(request);
        String candidateID = request.getParameter("candidateID");

        candidateRegistration.set("candidateID", candidateID);
        candidateRegistration.set("studentNumber", null);
        List branchList = null;
        try {

            branchList = GetBranchListByCandidateID.run(candidateID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("branchList", branchList);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
        try {
            infoMasterDegreeCandidate = GetCandidatesByID.run(candidateID);
        } catch (NonExistingServiceException e) {
            throw new FenixActionException(e);
        }
        candidateRegistration.set("branchID", null);
        request.setAttribute("infoMasterDegreeCandidate", infoMasterDegreeCandidate);

        return mapping.findForward("ShowConfirmation");
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm candidateRegistration = (DynaActionForm) form;

        IUserView userView = getUserView(request);
        String candidateID = (String) candidateRegistration.get("candidateID");
        String branchID = (String) candidateRegistration.get("branchID");

        String studentNumberString = (String) candidateRegistration.get("studentNumber");
        Integer studentNumber = null;
        if ((studentNumberString != null) && (studentNumberString.length() > 0)) {
            try {
                studentNumber = new Integer(studentNumberString);
                if (studentNumber.intValue() < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new InvalidInformationInFormActionException("error.exception.invalidInformationInStudentNumber");
            }
        }

        if ((request.getParameter("confirmation") == null) || request.getParameter("confirmation").equals("Confirmar")) {

            InfoCandidateRegistration infoCandidateRegistration = null;
            try {

                infoCandidateRegistration = RegisterCandidate.run(candidateID, branchID, studentNumber, userView);
            } catch (InvalidStudentNumberServiceException e) {
                throw new InvalidStudentNumberActionException(e);
            } catch (ActiveStudentCurricularPlanAlreadyExistsServiceException e) {
                throw new ActiveStudentCurricularPlanAlreadyExistsActionException(e);
            } catch (GratuityValuesNotDefinedServiceException e) {
                throw new GratuityValuesNotDefinedActionException(e);
            } catch (ExistingServiceException e) {

                List branchList = null;
                try {

                    branchList = GetBranchListByCandidateID.run(candidateID);
                } catch (FenixServiceException ex) {
                    throw new FenixActionException(ex);
                }

                request.setAttribute("branchList", branchList);

                try {

                    GetCandidatesByID.run(candidateID);
                } catch (NonExistingServiceException ex) {
                    throw new FenixActionException(ex);
                }
                throw new ExistingActionException("O Aluno", e);
            } catch (InvalidChangeServiceException e) {
                throw new InvalidChangeActionException("error.cantRegisterCandidate", e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            candidateRegistration.set("studentCurricularPlanID", infoCandidateRegistration.getInfoStudentCurricularPlan()
                    .getExternalId());
            request.setAttribute("infoCandidateRegistration", infoCandidateRegistration);

            return mapping.findForward("ShowResult");
        }
        return mapping.findForward("PrepareCandidateList");

    }

    public ActionForward preparePrint(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm candidateRegistration = (DynaActionForm) form;

        IUserView userView = getUserView(request);
        String candidateID = (String) candidateRegistration.get("candidateID");

        InfoCandidateRegistration infoCandidateRegistration = null;
        infoCandidateRegistration = GetCandidateRegistrationInformation.run(candidateID);

        request.setAttribute("infoCandidateRegistration", infoCandidateRegistration);
        request.setAttribute("infoExecutionDegree", infoCandidateRegistration.getInfoMasterDegreeCandidate()
                .getInfoExecutionDegree());

        return mapping.findForward("Print");
    }

}