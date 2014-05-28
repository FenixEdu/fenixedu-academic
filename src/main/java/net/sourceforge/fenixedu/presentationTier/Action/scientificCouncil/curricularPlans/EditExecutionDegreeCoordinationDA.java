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
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CoordinatorLog;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OperationType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificBolonhaProcessApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
            backPath =
                    "/curricularPlans/editExecutionDegreeCoordination.do?method=editByYears&executionYearId="
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

        Coordinator.makeCreation(personAdding, coordsBean.getExecutionDegree(), coordsBean.getNewCoordinator(),
                Boolean.valueOf(false));

        coordsBean.setNewCoordinator(null);
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");
        request.setAttribute("startVisible", true);

        return mapping.findForward("editCoordination");
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
            coordinator.makeAction(OperationType.CHANGERESPONSIBLE_FALSE, personSwitching);
            // coordinator.setAsNotResponsible();
        } else {
            coordinator.makeAction(OperationType.CHANGERESPONSIBLE_TRUE, personSwitching);
            // coordinator.setAsResponsible();
        }

        ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
        coordsBean.setEscapedBackPath(backPath);
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");

        return mapping.findForward("editCoordination");
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

        coordinator.makeAction(OperationType.REMOVE, personDeleting);
        // coordinator.setCoordinator(null);

        ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
        coordsBean.setEscapedBackPath(backPath);
        request.setAttribute("coordsBean", coordsBean);
        RenderUtils.invalidateViewState("coordsBean");

        return mapping.findForward("editCoordination");
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

        List<ExecutionDegree> bachelors = sessionBean.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_DEGREE);
        request.setAttribute("bachelors", bachelors);

        List<ExecutionDegree> masters = sessionBean.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_MASTER_DEGREE);
        request.setAttribute("masters", masters);

        List<ExecutionDegree> integratedMasters =
                sessionBean.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        request.setAttribute("integratedMasters", integratedMasters);

        List<ExecutionDegree> otherDegrees = new ArrayList<ExecutionDegree>(sessionBean.getExecutionYear().getExecutionDegrees());
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

        finalCoordinatorLogs.addAll(executionDegree.getCoordinatorLog());
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
