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
package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.commons.ReadExecutionDegreesByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadAllMasterDegrees;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadCPlanFromChosenMasterDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeCandidatesApp;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
@StrutsFunctionality(app = MasterDegreeCandidatesApp.class, path = "register-candidate",
        titleKey = "link.masterDegree.administrativeOffice.candidateRegistration")
@Mapping(path = "/chooseExecutionYearToRegisterCandidate", module = "masterDegreeAdministrativeOffice",
        input = "/chooseExecutionYear_bd.jsp")
@Forwards({
        @Forward(name = "DisplayMasterDegreeList",
                path = "/masterDegreeAdministrativeOffice/candidate/displayMasterDegreesToEditCandidates_bd.jsp"),
        @Forward(
                name = "MasterDegreeReady",
                path = "/masterDegreeAdministrativeOffice/candidate/displayCurricularPlanByChosenMasterDegreeToEditCandidates_bd.jsp"),
        @Forward(name = "PrepareSuccess", path = "/masterDegreeAdministrativeOffice/candidate/chooseExecutionYear_bd.jsp"),
        @Forward(name = "ChooseSuccess",
                path = "/masterDegreeAdministrativeOffice/candidateRegistration.do?method=getCandidateList") })
public class ChooseExecutionYearDispatchAction extends FenixDispatchAction {

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
        List<InfoExecutionDegree> executionYearList =
                ReadExecutionDegreesByDegreeCurricularPlanID.runReadExecutionDegreesByDegreeCurricularPlanID(curricularPlanID);

        List executionYearsLabels = transformIntoLabels(executionYearList);
        request.setAttribute(PresentationConstants.EXECUTION_YEAR_LIST, executionYearsLabels);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, curricularPlanID);

        return mapping.findForward("PrepareSuccess");
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

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String curricularPlanID = request.getParameter("degreeCurricularPlanID");
        String executionDegreeID = request.getParameter("executionDegreeID");

        if (curricularPlanID == null) {
            curricularPlanID = (String) request.getAttribute("degreeCurricularPlanID");

        }

        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, request.getParameter("executionDegreeID"));
        request.setAttribute("degreeCurricularPlanID", curricularPlanID);

        if (executionDegreeID != null) {
            ExecutionDegree executionDegree = (ExecutionDegree) FenixFramework.getDomainObject(executionDegreeID);
            ExecutionYear executionYear = executionDegree.getExecutionYear();
            request.setAttribute(PresentationConstants.EXECUTION_YEAR, executionYear.getName());
        }

        return mapping.findForward("ChooseSuccess");
    }

}