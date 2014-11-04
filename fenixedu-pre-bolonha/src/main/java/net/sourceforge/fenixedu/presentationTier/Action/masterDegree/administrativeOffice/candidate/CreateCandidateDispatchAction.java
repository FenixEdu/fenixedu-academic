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
package org.fenixedu.academic.ui.struts.action.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.candidate.CreateMasterDegreeCandidate;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.commons.ReadExecutionDegreesByDegreeCurricularPlanID;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.student.listings.ReadAllMasterDegrees;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.student.listings.ReadCPlanFromChosenMasterDegree;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidate;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.ExistingActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.NonExistingActionException;
import org.fenixedu.academic.ui.struts.action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeCandidatesApp;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *         This is the Action to create a Master Degree Candidate
 */
@StrutsFunctionality(app = MasterDegreeCandidatesApp.class, path = "create-candidate",
        titleKey = "link.masterDegree.administrativeOffice.createCandidate")
@Mapping(path = "/chooseExecutionYear", module = "masterDegreeAdministrativeOffice", input = "/chooseExecutionYear_bd.jsp",
        formBean = "chooseExecutionYearForm")
@Forwards(value = {
        @Forward(name = "DisplayMasterDegreeList",
                path = "/masterDegreeAdministrativeOffice/candidate/displayMasterDegrees_bd.jsp"),
        @Forward(name = "MasterDegreeReady",
                path = "/masterDegreeAdministrativeOffice/candidate/displayCurricularPlanByChosenMasterDegree_bd.jsp"),
        @Forward(name = "CreateReady",
                path = "/masterDegreeAdministrativeOffice/createCandidateDispatchAction.do?method=prepare&page=0"),
        @Forward(name = "PrepareSuccess", path = "/masterDegreeAdministrativeOffice/chooseExecutionYear_bd.jsp"),
        @Forward(name = "CreateSuccess", path = "/masterDegreeAdministrativeOffice/createCandidateSuccess_bd.jsp") })
@Exceptions({ @ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = FenixErrorExceptionHandler.class, type = NonExistingActionException.class) })
public class CreateCandidateDispatchAction extends FenixDispatchAction {

    @Mapping(path = "/createCandidateDispatchAction", module = "masterDegreeAdministrativeOffice",
            formBean = "createCandidateForm", functionality = CreateCandidateDispatchAction.class)
    @Forwards(@Forward(name = "PrepareSuccess", path = "/masterDegreeAdministrativeOffice/createCandidate_bd.jsp"))
    public static class CreateCandidateAction extends CreateCandidateDispatchAction {
    }

    @EntryPoint
    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DegreeType degreeType = DegreeType.MASTER_DEGREE;

        List result = null;
        try {
            result = ReadAllMasterDegrees.run(degreeType);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O Degree de Mestrado", e);
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_LIST, result);

        return mapping.findForward("DisplayMasterDegreeList");
    }

    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Get the Chosen Master Degree
        String masterDegreeID = request.getParameter("degreeID");
        if (masterDegreeID == null) {
            masterDegreeID = (String) request.getAttribute("degreeID");
        }

        List result = null;

        try {

            result = ReadCPlanFromChosenMasterDegree.run(masterDegreeID);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O plano curricular ", e);
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);

        return mapping.findForward("MasterDegreeReady");
    }

    public ActionForward prepareChooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Get the Chosen Master Degree
        String curricularPlanID = request.getParameter("curricularPlanID");
        if (curricularPlanID == null) {
            curricularPlanID = (String) request.getAttribute("curricularPlanID");
        }
        request.setAttribute("curricularPlanID", curricularPlanID);

        List executionYearList =
                ReadExecutionDegreesByDegreeCurricularPlanID.runReadExecutionDegreesByDegreeCurricularPlanID(curricularPlanID);
        List executionYearsLabels = transformIntoLabels(executionYearList);
        request.setAttribute(PresentationConstants.EXECUTION_YEAR_LIST, executionYearsLabels);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, curricularPlanID);

        return mapping.findForward("PrepareSuccess");

    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute(PresentationConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
        String curricularPlanID = request.getParameter("degreeCurricularPlanID");

        if (curricularPlanID == null) {
            curricularPlanID = (String) request.getAttribute("curricularPlanID");

        }
        request.setAttribute("degreeCurricularPlanID", curricularPlanID);

        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, request.getParameter("executionDegreeID"));
        return mapping.findForward("CreateReady");

    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Create the Degree Type List

        String executionDegreeId = (String) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        if (executionDegreeId == null) {
            executionDegreeId = request.getParameter(PresentationConstants.EXECUTION_DEGREE);
        }
        request.setAttribute(PresentationConstants.EXECUTION_YEAR, request.getAttribute(PresentationConstants.EXECUTION_YEAR));

        String curricularPlanID = null;
        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        if (degreeCurricularPlanID != null) {
            curricularPlanID = degreeCurricularPlanID;
        } else {
            curricularPlanID = (String) request.getAttribute("curricularPlanID");
        }

        request.setAttribute("curricularPlanID", curricularPlanID);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, executionDegreeId);

        return mapping.findForward("PrepareSuccess");

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Get the Information
        DynaActionForm createCandidateForm = (DynaActionForm) form;
        String degreeType = (String) createCandidateForm.get("specialization");
        String executionDegreeOID = (String) createCandidateForm.get("executionDegreeOID");
        String name = (String) createCandidateForm.get("name");
        String identificationDocumentNumber = (String) createCandidateForm.get("identificationDocumentNumber");
        String identificationDocumentType = (String) createCandidateForm.get("identificationDocumentType");

        InfoMasterDegreeCandidate createdCandidate = null;
        try {
            createdCandidate =
                    CreateMasterDegreeCandidate.run(Specialization.valueOf(degreeType), executionDegreeOID, name,
                            identificationDocumentNumber, IDDocumentType.valueOf(identificationDocumentType));
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("O Candidato", e);
        }

        request.setAttribute(PresentationConstants.NEW_MASTER_DEGREE_CANDIDATE, createdCandidate);
        return mapping.findForward("CreateSuccess");
    }

    private List transformIntoLabels(List executionYearList) {
        List executionYearsLabels = new ArrayList();
        CollectionUtils.collect(executionYearList, new Transformer() {
            @Override
            public Object transform(Object input) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
                LabelValueBean labelValueBean =
                        new LabelValueBean(infoExecutionDegree.getInfoExecutionYear().getYear(), infoExecutionDegree
                                .getExternalId().toString());
                return labelValueBean;
            }
        }, executionYearsLabels);
        Collections.sort(executionYearsLabels, new BeanComparator("label"));
        Collections.reverse(executionYearsLabels);

        return executionYearsLabels;
    }

}