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
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.VariantBean;
import org.fenixedu.academic.dto.administrativeOffice.ExecutionDegreeBean;
import org.fenixedu.academic.service.services.coordinator.AddScientificCommission;
import org.fenixedu.academic.service.services.coordinator.DeleteScientificCommission;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "coordinator", path = "/scientificCommissionTeamDA", functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "viewScientificCommission", path = "/coordinator/scientificCommission/manageScientificCommission.jsp"))
public class ScientificCommissionTeamDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));

        ExecutionDegree executionDegree = getExecutionDegree(request);
        if (executionDegree != null) {
            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("executionDegreeId", executionDegree.getExternalId());
        } else {
            request.setAttribute("executionDegreeId", "");
        }

        return super.execute(mapping, actionForm, request, response);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        String id = request.getParameter("degreeCurricularPlanID");
        if (id == null) {
            return null;
        } else {
            return FenixFramework.getDomainObject(id);
        }
    }

    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        ExecutionDegreeBean bean = getRenderedObject("executionDegreeChoice");
        if (bean != null) {
            return bean.getExecutionDegree();
        } else {
            String id = request.getParameter("executionDegreeID");
            if (id == null) {
                return getDefaultExecutionDegree(request);
            } else {
                return FenixFramework.getDomainObject(id);
            }
        }
    }

    private ExecutionDegree getDefaultExecutionDegree(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        TreeSet<ExecutionDegree> executionDegrees =
                new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        executionDegrees.addAll(degreeCurricularPlan.getExecutionDegreesSet());

        return executionDegrees.isEmpty() ? null : executionDegrees.last();
    }

    public ActionForward manage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        ExecutionDegree executionDegree = getExecutionDegree(request);

        ExecutionDegreeBean bean = getRenderedObject("executionDegreeChoice");
        if (bean == null) {
            bean = new ExecutionDegreeBean(degreeCurricularPlan);
            bean.setExecutionDegree(executionDegree);
        }

        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("executionDegreeBean", bean);
        request.setAttribute("usernameBean", new VariantBean());
        request.setAttribute("members", executionDegree == null ? null : executionDegree.getScientificCommissionMembersSet());

        if (isResponsible(request, executionDegree)) {
            request.setAttribute("responsible", true);
        }

        if (hasUpdatedContactFlag(request)) {
            request.setAttribute("updateContactConfirmation", true);
        }

        return mapping.findForward("viewScientificCommission");
    }

    private boolean hasUpdatedContactFlag(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState("membersContacts");

        if (viewState == null) {
            return false;
        }

        if (!viewState.isValid()) {
            return false;
        }

        if (viewState.skipUpdate()) {
            return false;
        }

        return true;
    }

    private boolean isResponsible(HttpServletRequest request, ExecutionDegree executionDegree) {
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(getLoggedPerson(request));

        return coordinator != null && coordinator.isResponsible();
    }

    public ActionForward addMember(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        VariantBean bean = getRenderedObject("usernameChoice");
        if (bean != null) {
            String username = bean.getString();

            Person person = Person.readPersonByUsername(username);
            if (person == null) {
                addActionMessage("addError", request, "error.coordinator.scientificComission.person.doesNotExist");
            } else {
                ExecutionDegree executionDegree = getExecutionDegree(request);

                try {
                    AddScientificCommission.runAddScientificCommission(executionDegree.getExternalId(), person);
                    RenderUtils.invalidateViewState("usernameChoice");
                } catch (DomainException e) {
                    addActionMessage("addError", request, e.getKey(), e.getArgs());
                }
            }
        }

        return manage(mapping, actionForm, request, response);
    }

    public ActionForward removeMember(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String memberId = request.getParameter("memberID");

        if (memberId != null) {
            ExecutionDegree executionDegree = getExecutionDegree(request);

            for (ScientificCommission commission : executionDegree.getScientificCommissionMembersSet()) {
                if (commission.getExternalId().equals(memberId)) {
                    try {
                        DeleteScientificCommission.runDeleteScientificCommission(executionDegree.getExternalId(), commission);
                    } catch (DomainException e) {
                        addActionMessage("addError", request, e.getKey(), e.getArgs());
                    }
                }
            }
        }

        return manage(mapping, actionForm, request, response);
    }

}
