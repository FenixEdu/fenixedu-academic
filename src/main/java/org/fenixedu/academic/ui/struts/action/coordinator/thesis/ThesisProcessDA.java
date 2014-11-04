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
package org.fenixedu.academic.ui.struts.action.coordinator.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/thesisProcess", module = "coordinator", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "showInformation", path = "/coordinator/thesis/showInformation.jsp") })
public class ThesisProcessDA extends FenixDispatchAction {

    public ActionForward showInformation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final ManageThesisContext manageThesisContext = processContext(request);
        return mapping.findForward("showInformation");
    }

    private ManageThesisContext processContext(final HttpServletRequest request) {
        ManageThesisContext manageThesisContext = getRenderedObject();
        if (manageThesisContext == null) {
            final ExecutionDegree executionDegree = guessExecutionDegree(request);
            if (executionDegree != null) {
                manageThesisContext = new ManageThesisContext(executionDegree);
            }
        }
        if (manageThesisContext != null) {
            request.setAttribute("manageThesisContext", manageThesisContext);
        }
        return manageThesisContext;
    }

    private ExecutionDegree guessExecutionDegree(final HttpServletRequest request) {
        final DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "degreeCurricularPlanID");
        ExecutionDegree last = null;
        if (degreeCurricularPlan != null) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                final ExecutionYear executionYear = executionDegree.getExecutionYear();
                if (executionYear.isCurrent()) {
                    return executionDegree;
                }
                if (last == null || last.getExecutionYear().isBefore(executionYear)) {
                    last = executionDegree;
                }
            }
        }
        return last;
    }

}
