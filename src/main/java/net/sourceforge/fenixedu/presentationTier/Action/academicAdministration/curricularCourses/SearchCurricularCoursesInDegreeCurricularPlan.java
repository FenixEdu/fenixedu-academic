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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.curricularCourses;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.CurricularPlansManagement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/searchCurricularCourses", module = "academicAdministration", functionality = CurricularPlansManagement.class)
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
        request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

        return mapping.findForward("searchCurricularCourses");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "dcpId");
        SearchCurricularCourseBean searchBean = getRenderedObject("searchBean");

        request.setAttribute("results", searchBean.search());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

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
        request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

        return mapping.findForward("searchCurricularCourses");
    }

}
