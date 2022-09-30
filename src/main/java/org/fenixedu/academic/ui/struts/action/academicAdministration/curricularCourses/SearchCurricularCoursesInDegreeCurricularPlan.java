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
package org.fenixedu.academic.ui.struts.action.academicAdministration.curricularCourses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import com.google.common.collect.Lists;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/searchCurricularCourses", module = "academicAdministration")
@Forwards({ @Forward(name = "searchCurricularCourses",
        path = "/academicAdministration/bolonha/curricularCourses/search/searchCurricularCourses.jsp") })
public class SearchCurricularCoursesInDegreeCurricularPlan extends FenixDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "dcpId");

        SearchCurricularCourseBean searchBean = new SearchCurricularCourseBean(degreeCurricularPlan);

        request.setAttribute("results", new ArrayList<Context>());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("currentExecutionYear", ExecutionYear.findCurrent(degreeCurricularPlan.getDegree().getCalendar()));

        return mapping.findForward("searchCurricularCourses");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "dcpId");
        SearchCurricularCourseBean searchBean = getRenderedObject("searchBean");

        List<Context> results = Lists.newArrayList(searchBean.search());
        Collections.sort(results);
        request.setAttribute("results", results);
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("currentExecutionYear", ExecutionYear.findCurrent(degreeCurricularPlan.getDegree().getCalendar()));

        RenderUtils.invalidateViewState();
        return mapping.findForward("searchCurricularCourses");
    }

    public ActionForward searchInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "dcpId");
        SearchCurricularCourseBean searchBean = getRenderedObject("searchBean");

        request.setAttribute("results", new ArrayList<Context>());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("currentExecutionYear", ExecutionYear.findCurrent(degreeCurricularPlan.getDegree().getCalendar()));

        return mapping.findForward("searchCurricularCourses");
    }

}
