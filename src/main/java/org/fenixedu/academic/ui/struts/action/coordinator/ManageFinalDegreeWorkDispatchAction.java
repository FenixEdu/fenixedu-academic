/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 2004/03/09
 */
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */

@Mapping(path = "/manageFinalDegreeWork", module = "coordinator", formBean = "finalDegreeWorkCandidacyRequirements",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "show-final-degree-work-info", path = "/coordinator/finalDegreeWork/showFinalDegreeWorkInfo.jsp"),
        @Forward(name = "show-choose-execution-degree-page",
                path = "/coordinator/finalDegreeWork/showFinalDegreeChooseExecutionDegree_bd.jsp") })
public class ManageFinalDegreeWorkDispatchAction extends FenixDispatchAction {

    @Mapping(path = "/finalDegreeWorkProposal", module = "coordinator",
            input = "/finalDegreeWorkProposal.do?method=createNewFinalDegreeWorkProposal", formBean = "finalDegreeWorkProposal",
            functionality = DegreeCoordinatorIndex.class)
    @Forwards(@Forward(name = "show-final-degree-work-list",
            path = "/coordinator/manageFinalDegreeWork.do?method=showProposal&page=0"))
    public static class FinalDegreeWorkProposalDA extends ManageFinalDegreeWorkDispatchAction {
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        final String executionDegreePlanOID = newFindExecutionDegreeID(request);
        if (executionDegreePlanOID != null) {
            request.setAttribute("executionDegreeOID", executionDegreePlanOID);
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreePlanOID);
            request.setAttribute("executionDegree", executionDegree);
        }
        return super.execute(mapping, actionForm, request, response);
    }

    private static String newFindExecutionDegreeID(HttpServletRequest request) {
        String executionDegreePlanOID = request.getParameter("executionDegreeOID");
        if (executionDegreePlanOID == null) {
            executionDegreePlanOID = (String) request.getAttribute("executionDegreeOID");
        }
        return executionDegreePlanOID;
    }

    public ActionForward showChooseExecutionDegreeForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        // keep degreeCurricularPlan in request
        final String degreeCurricularPlanOID = (String) getFromRequest(request, "degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "degreeCurricularPlanID");

        final List<InfoExecutionDegree> infoExecutionDegrees = getExecutionCourses(degreeCurricularPlan);
        Collections.sort(infoExecutionDegrees, new Comparator<InfoExecutionDegree>() {
            @Override
            public int compare(InfoExecutionDegree o1, InfoExecutionDegree o2) {
                return o2.getExecutionDegree().getExecutionYear().compareTo(o1.getExecutionDegree().getExecutionYear());
            }
        });

        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        return mapping.findForward("show-choose-execution-degree-page");
    }

    public static List<InfoExecutionDegree> getExecutionCourses(final DegreeCurricularPlan degreeCurricularPlan) {
        final Collection<ExecutionDegree> executionDegrees =
                degreeCurricularPlan.getExecutionYears().stream()
                        .map(executionYear -> degreeCurricularPlan.getExecutionDegreeByYear(executionYear))
                        .collect(Collectors.toSet());

        final List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            result.add(infoExecutionDegree);
        }

        return result;
    }

    public ActionForward finalDegreeWorkInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExecutionDegree executionDegree = getExecutionDegree(request);
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        ExecutionYear executionYear = executionDegree.getExecutionYear().getNextExecutionYear();
        if (executionYear != null) {
            request.setAttribute("executionYearId", executionYear.getExternalId());
        }
        request.setAttribute("summary", new ThesisSummaryBean(executionDegree, degreeCurricularPlan));
        return mapping.findForward("show-final-degree-work-info");
    }

    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        return getDomainObject(request, "executionDegreeOID");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        return getDomainObject(request, "degreeCurricularPlanID");
    }

}