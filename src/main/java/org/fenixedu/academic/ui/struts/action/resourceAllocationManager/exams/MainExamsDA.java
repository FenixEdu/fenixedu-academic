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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.resourceAllocationManager.ContextSelectionBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMEvaluationsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = RAMEvaluationsApp.class, path = "written-evaluations-calendar",
        titleKey = "link.writtenEvaluation.map")
@Mapping(path = "/mainExams", module = "resourceAllocationManager")
@Forwards(@Forward(name = "showForm", path = "/resourceAllocationManager/exams/mainExams_bd.jsp"))
public class MainExamsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ContextSelectionBean contextSelectionBean = getRenderedObject();
        if (contextSelectionBean == null) {
            contextSelectionBean = new ContextSelectionBean();
            AcademicInterval fromAttribute = (AcademicInterval) request.getAttribute("academicInterval");
            if (fromAttribute != null) {
                contextSelectionBean.setAcademicInterval(fromAttribute);
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", contextSelectionBean);
        final List<ExecutionDegree> executionDegrees =
                new ArrayList<ExecutionDegree>(ExecutionDegree.filterByAcademicInterval(contextSelectionBean
                        .getAcademicInterval()));
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        request.setAttribute("executionDegrees", executionDegrees);
        request.setAttribute("executionInterval",
                ExecutionInterval.getExecutionInterval(contextSelectionBean.getAcademicInterval()));
        return mapping.findForward("showForm");
    }
}
