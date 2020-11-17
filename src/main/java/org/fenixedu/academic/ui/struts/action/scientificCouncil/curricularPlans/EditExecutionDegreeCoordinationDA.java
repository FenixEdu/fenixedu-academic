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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.CoordinatorLog;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificBolonhaProcessApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificBolonhaProcessApp.class, path = "edit-degree-coordination",
        titleKey = "navigation.manageCoordinationTeams")
@Mapping(path = "/curricularPlans/editExecutionDegreeCoordination", module = "scientificCouncil")
@Forwards({ @Forward(name = "presentCoordination", path = "/scientificCouncil/curricularPlans/presentCoordination.jsp"),
        @Forward(name = "editCoordination", path = "/scientificCouncil/curricularPlans/editCoordination.jsp"),
        @Forward(name = "selectYearAndDegree", path = "/scientificCouncil/curricularPlans/selectYearAndDegree.jsp") })
public class EditExecutionDegreeCoordinationDA extends FenixDispatchAction {

    public ActionForward prepareEditCoordination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanId");
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

        final Set<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegreesSet();

        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("executionDegreesSet", executionDegrees);

        return mapping.findForward("presentCoordination");

    }

    public ActionForward editCoordination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String executionDegreeId = request.getParameter("executionDegreeId");

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

        String backTo = String.valueOf(request.getParameter("from"));
        String backPath;
        if (backTo.equals("byYears")) {
            backPath = "/curricularPlans/editExecutionDegreeCoordination.do?method=editByYears&executionYearId="
                    + executionDegree.getExecutionYear().getExternalId().toString();
        } else {
            backPath =
                    "/curricularPlans/editExecutionDegreeCoordination.do?method=prepareEditCoordination&degreeCurricularPlanId="
                            + executionDegree.getDegreeCurricularPlan().getExternalId().toString();
        }

        ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);

        coordsBean.setBackPath(backPath);

        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");

        return mapping.findForward("editCoordination");
    }

    public ActionForward addCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final String personId = request.getParameter("personId");
        final Person personAdding = FenixFramework.getDomainObject(personId);

        ExecutionDegreeCoordinatorsBean coordsBean = getRenderedObject("coordsBean");

        createCoordinatorService(coordsBean);

        coordsBean.setNewCoordinator(null);
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");
        request.setAttribute("startVisible", true);

        return mapping.findForward("editCoordination");
    }

    @Atomic
    private void createCoordinatorService(ExecutionDegreeCoordinatorsBean coordsBean) {
        Coordinator.createCoordinator(coordsBean.getExecutionDegree(), coordsBean.getNewCoordinator(), Boolean.valueOf(false));
    }

    public ActionForward switchResponsability(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final String coordinatorId = request.getParameter("coordinatorId");
        Coordinator coordinator = FenixFramework.getDomainObject(coordinatorId);

        final String executionDegreeId = request.getParameter("executionDegreeId");
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

        final String personId = request.getParameter("personId");
        final Person personSwitching = FenixFramework.getDomainObject(personId);

        String backPath = request.getParameter("backPath");

        if (coordinator.isResponsible()) {
//            coordinator.makeAction(OperationType.CHANGERESPONSIBLE_FALSE, personSwitching);
            setAsNotResponsible(coordinator);
        } else {
//            coordinator.makeAction(OperationType.CHANGERESPONSIBLE_TRUE, personSwitching);
            setAsResponsible(coordinator);
        }

        ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
        coordsBean.setEscapedBackPath(backPath);
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");

        return mapping.findForward("editCoordination");
    }

    @Atomic
    private void setAsResponsible(Coordinator coordinator) {
        coordinator.setResponsible(Boolean.TRUE);
    }

    @Atomic
    private void setAsNotResponsible(Coordinator coordinator) {
        coordinator.setResponsible(Boolean.FALSE);
    }

    public ActionForward deleteCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final String coordinatorId = request.getParameter("coordinatorId");
        Coordinator coordinator = FenixFramework.getDomainObject(coordinatorId);

        final String executionDegreeId = request.getParameter("executionDegreeId");
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

        final String personId = request.getParameter("personId");
        final Person personDeleting = FenixFramework.getDomainObject(personId);

        String backPath = request.getParameter("backPath");

        removeCoordinator(coordinator);
//        coordinator.makeAction(OperationType.REMOVE, personDeleting);
        // coordinator.setCoordinator(null);

        ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
        coordsBean.setEscapedBackPath(backPath);
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");

        return mapping.findForward("editCoordination");
    }

    @Atomic
    private void removeCoordinator(Coordinator coordinator) {
        coordinator.delete();
    }

    public ActionForward invalidAddCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionDegreeCoordinatorsBean coordsBean = getRenderedObject("coordsBean");
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");
        request.setAttribute("startVisible", true);

        return mapping.findForward("editCoordination");
    }

    @EntryPoint
    public ActionForward editByYears(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionDegreeCoordinatorsBean sessionBean = getRenderedObject("sessionBean");
        if (sessionBean == null) {
            sessionBean = new ExecutionDegreeCoordinatorsBean();
            final String executionYearId = String.valueOf(request.getParameter("executionYearId"));
            if (!executionYearId.equals("null")) {
                ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearId);
                sessionBean.setExecutionYear(executionYear);
            } else {
                request.setAttribute("sessionBean", sessionBean);
                RenderUtils.invalidateViewState("sessionBean");

                return mapping.findForward("selectYearAndDegree");
            }
        }

        List<ExecutionDegree> bachelors = sessionBean.getExecutionYear().getExecutionDegreesSet().stream()
                .filter(ed -> ed.getDegreeCurricularPlan().getDegree().getDegreeType().isDegree()).collect(Collectors.toList());
        request.setAttribute("bachelors", bachelors);

        List<ExecutionDegree> masters = sessionBean.getExecutionYear().getExecutionDegreesSet().stream()
                .filter(ed -> ed.getDegreeCurricularPlan().getDegree().getDegreeType().isMasterDegree())
                .collect(Collectors.toList());
        request.setAttribute("masters", masters);

        List<ExecutionDegree> integratedMasters = sessionBean.getExecutionYear().getExecutionDegreesSet().stream()
                .filter(ed -> ed.getDegreeCurricularPlan().getDegree().getDegreeType().isIntegratedMasterDegree())
                .collect(Collectors.toList());
        request.setAttribute("integratedMasters", integratedMasters);

        List<ExecutionDegree> otherDegrees =
                new ArrayList<ExecutionDegree>(sessionBean.getExecutionYear().getExecutionDegreesSet());
        otherDegrees.removeAll(bachelors);
        otherDegrees.removeAll(masters);
        otherDegrees.removeAll(integratedMasters);
        request.setAttribute("otherDegrees", otherDegrees);

        boolean hasYearSelected = true;
        request.setAttribute("hasYearSelected", hasYearSelected);

        request.setAttribute("sessionBean", sessionBean);
        RenderUtils.invalidateViewState("sessionBean");

        return mapping.findForward("selectYearAndDegree");
    }

    public List<CoordinatorLog> getCoordinatorLogsByExecDegree(ExecutionDegree executionDegree) {
        List<CoordinatorLog> finalCoordinatorLogs = new ArrayList<CoordinatorLog>();

        finalCoordinatorLogs.addAll(executionDegree.getCoordinatorLogSet());
        return finalCoordinatorLogs;
    }

    public ActionForward prepareCoordinatorLog(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String execDegId = request.getParameter("executionYearId");
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(execDegId);
        List<CoordinatorLog> coordinatorLogs = getCoordinatorLogsByExecDegree(executionDegree);
        request.setAttribute("coordinatorLogs", coordinatorLogs);

        String backPath = request.getParameter("backPath");
        ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
        coordsBean.setEscapedBackPath(backPath);
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");
        return mapping.findForward("editCoordination");
    }
}
