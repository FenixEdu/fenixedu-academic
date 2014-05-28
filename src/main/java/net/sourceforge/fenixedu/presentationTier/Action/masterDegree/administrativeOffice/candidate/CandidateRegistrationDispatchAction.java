/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ActiveStudentCurricularPlanAlreadyExistsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.GratuityValuesNotDefinedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidChangeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidStudentNumberActionException;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 *
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *
 *
 */
@Mapping(path = "/candidateRegistration", module = "masterDegreeAdministrativeOffice", input = "/chooseMasterDegree_bd.jsp",
        formBean = "candidateRegistrationForm", functionality = ChooseExecutionYearDispatchAction.class)
@Forwards(value = {
        @Forward(name = "ListCandidates",
                path = "/masterDegreeAdministrativeOffice/candidate/displayCandidateListForRegistration_bd.jsp"),
        @Forward(name = "ShowConfirmation",
                path = "/masterDegreeAdministrativeOffice/candidate/confirmCandidateRegistration_bd.jsp"),
        @Forward(name = "PrepareCandidateList",
                path = "/masterDegreeAdministrativeOffice/candidateRegistration.do?method=getCandidateList"),
        @Forward(name = "ShowResult", path = "/masterDegreeAdministrativeOffice/candidate/candidateRegistered_bd.jsp"),
        @Forward(name = "Print", path = "/masterDegreeAdministrativeOffice/candidate/candidateRegistrationTemplatePrint.jsp") })
@Exceptions({
        @ExceptionHandling(key = "resources.Action.exceptions.InvalidInformationInFormActionException",
                handler = FenixErrorExceptionHandler.class, type = InvalidInformationInFormActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.InvalidChangeActionException",
                handler = FenixErrorExceptionHandler.class, type = InvalidChangeActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException",
                handler = FenixErrorExceptionHandler.class, type = ExistingActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.ActiveStudentCurricularPlanAlreadyExistsActionException",
                handler = FenixErrorExceptionHandler.class, type = ActiveStudentCurricularPlanAlreadyExistsActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.InvalidStudentNumberActionException",
                handler = FenixErrorExceptionHandler.class, type = InvalidStudentNumberActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.GratuityValuesNotDefinedActionException",
                handler = FenixErrorExceptionHandler.class, type = GratuityValuesNotDefinedActionException.class) })
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

        User userView = getUserView(request);

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

        User userView = getUserView(request);
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

        User userView = getUserView(request);
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

        User userView = getUserView(request);
        String candidateID = (String) candidateRegistration.get("candidateID");

        InfoCandidateRegistration infoCandidateRegistration = null;
        infoCandidateRegistration = GetCandidateRegistrationInformation.run(candidateID);

        request.setAttribute("infoCandidateRegistration", infoCandidateRegistration);
        request.setAttribute("infoExecutionDegree", infoCandidateRegistration.getInfoMasterDegreeCandidate()
                .getInfoExecutionDegree());

        PortalLayoutInjector.skipLayoutOn(request);
        return mapping.findForward("Print");
    }

}