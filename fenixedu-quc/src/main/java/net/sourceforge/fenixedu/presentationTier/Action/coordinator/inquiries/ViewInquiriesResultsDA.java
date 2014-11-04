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
package org.fenixedu.academic.ui.struts.action.coordinator.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.inquiries.ViewInquiriesResultPageDTO;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.FenixFramework;

abstract public class ViewInquiriesResultsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("coursesResultResumeMap", "");
        request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
        ((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(getStringFromRequest(request,
                "degreeCurricularPlanID"));

        return actionMapping.findForward("curricularUnitSelection");
    }

    public List<ExecutionSemester> getExecutionSemesters(HttpServletRequest request, ActionForm actionForm) {
        String degreeCurricularPlanID = getStringFromRequest(request, "degreeCurricularPlanID");
        if (degreeCurricularPlanID == null || degreeCurricularPlanID.equals("")) {
            degreeCurricularPlanID = ((ViewInquiriesResultPageDTO) actionForm).getDegreeCurricularPlanID();
        }
        final DegreeCurricularPlan degreeCurricularPlan =
                FenixFramework.getDomainObject(getStringFromRequest(request, "degreeCurricularPlanID"));
        final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            executionSemesters.addAll(executionYear.getExecutionPeriodsSet());
        }
        Collections.sort(executionSemesters);
        Collections.reverse(executionSemesters);
        return executionSemesters;
    }

    public ActionForward selectexecutionSemester(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ViewInquiriesResultPageDTO resultPageDTO = (ViewInquiriesResultPageDTO) actionForm;
        final ExecutionSemester executionSemester = resultPageDTO.getExecutionSemester();
        if (executionSemester == null) {
            return prepare(actionMapping, actionForm, request, response);
        }
        final ExecutionYear executionYear = executionSemester.getExecutionYear();
        final DegreeCurricularPlan degreeCurricularPlan = resultPageDTO.getDegreeCurricularPlan();
        final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
        if (executionDegree == null) {
            return prepare(actionMapping, actionForm, request, response);
        }

        resultPageDTO.setExecutionDegreeID(executionDegree.getExternalId());

        request.setAttribute("executionSemester", executionSemester);
        request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
        request.setAttribute("executionDegreeID", resultPageDTO.getExecutionDegreeID());

        return actionMapping.findForward("curricularUnitSelection");
    }
}
