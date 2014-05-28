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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceCoursesBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@Mapping(module = "publico", path = "/searchFileContent", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "search-file-content") })
public class SearchFileContentAction extends FenixDispatchAction {
    public ActionForward prepareSearchForExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourse course = null;

        Site site = SiteMapper.getSite(request);
        if (site != null && site instanceof ExecutionCourseSite) {
            course = ((ExecutionCourseSite) site).getSiteExecutionCourse();
        }

        String executionCourseId = request.getParameter("executionCourseID");
        if (executionCourseId != null) {
            course = (ExecutionCourse) FenixFramework.getDomainObject(executionCourseId);
        }

        SearchDSpaceCoursesBean bean = new SearchDSpaceCoursesBean();
        bean.setCourse(course);
        if (course != null) {
            bean.setExecutionYear(course.getExecutionYear());
            bean.setExecutionPeriod(course.getExecutionPeriod());
        }

        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());

        return mapping.findForward("search");

    }

    public ActionForward searchFileContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchDSpaceCoursesBean bean = getRenderedObject("search");
        bean.search();
        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        request.setAttribute("pageNumber", bean.getPage());

        return mapping.findForward("search");
    }

    public ActionForward changeTimeStamp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchDSpaceCoursesBean bean = getRenderedObject("search");
        RenderUtils.invalidateViewState("executionPeriodField");

        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        request.setAttribute("pageNumber", bean.getPage());

        return mapping.findForward("search");
    }

    public ActionForward moveIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchDSpaceCoursesBean bean = SearchDSpaceCoursesBean.reconstructFromRequest(request);
        bean.search();
        String pageNumber = request.getParameter("pageNumber");
        if (!Strings.isNullOrEmpty(pageNumber)) {
            bean.setPage(Integer.valueOf(pageNumber));
        }
        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        request.setAttribute("pageNumber", bean.getPage());
        return mapping.findForward("search");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, actionForm, request, response);
        ExecutionCourse course = (ExecutionCourse) request.getAttribute("executionCourse");
        OldCmsSemanticURLHandler.selectSite(request, course.getSite());
        return forward;
    }

}
