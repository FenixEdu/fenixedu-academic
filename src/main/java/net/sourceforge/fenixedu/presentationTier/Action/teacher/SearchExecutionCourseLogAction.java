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
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseLogBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseLog;
import net.sourceforge.fenixedu.domain.ExecutionCourseLog.ExecutionCourseLogTypes;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "teacher", path = "/searchECLog", functionality = ManageExecutionCourseDA.class)
public class SearchExecutionCourseLogAction extends ExecutionCourseBaseAction {

    public ActionForward prepareInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        SearchExecutionCourseLogBean seclb = new SearchExecutionCourseLogBean(executionCourse);
        seclb.setExecutionCourseLogTypes(new ArrayList<ExecutionCourseLogTypes>());

        request.setAttribute("searchBean", seclb);
        request.setAttribute("executionCourse", seclb.getExecutionCourse());

        return forward(request, "/teacher/viewLogSearch.jsp");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        SearchExecutionCourseLogBean seclb = readSearchBean(request, executionCourse);

        searchLogs(seclb);
        request.setAttribute("searchBean", seclb);
        request.setAttribute("executionCourse", seclb.getExecutionCourse());

        prepareAttendsCollectionPages(request, seclb, seclb.getExecutionCourse());
        return forward(request, "/teacher/viewLogSearch.jsp");
    }

    private SearchExecutionCourseLogBean readSearchBean(HttpServletRequest request, ExecutionCourse executionCourse) {
        ExecutionCourse course = getExecutionCourse(request);
        if (course != null) {
            SearchExecutionCourseLogBean seclb = new SearchExecutionCourseLogBean(course);

            String viewPhoto = request.getParameter("viewPhoto");
            if (viewPhoto != null && viewPhoto.equalsIgnoreCase("true")) {
                seclb.setViewPhoto(true);
            } else {
                seclb.setViewPhoto(false);
            }

            String logTypes = request.getParameter("executionCourseLogTypes");
            if (logTypes != null) {
                List<ExecutionCourseLogTypes> list = new ArrayList<ExecutionCourseLogTypes>();
                for (String logType : logTypes.split(":")) {
                    list.add(ExecutionCourseLogTypes.valueOf(logType));
                }
                seclb.setExecutionCourseLogTypes(list);
            }

            String professorships = request.getParameter("professorships");
            if (professorships != null) {
                List<Professorship> list = new ArrayList<Professorship>();
                for (String professorship : professorships.split(":")) {
                    list.add(FenixFramework.<Professorship> getDomainObject(professorship));
                }
                seclb.setProfessorships(list);
            }

            String months = request.getParameter("months");
            if (months != null) {
                List<Month> list = new ArrayList<Month>();
                for (String month : months.split(":")) {
                    list.add(Month.fromInt(Integer.valueOf(month).intValue()));
                }
                seclb.setMonths(list);
            }

            return seclb;
        } else {
            return new SearchExecutionCourseLogBean(executionCourse);
        }
    }

    private void prepareAttendsCollectionPages(HttpServletRequest request, SearchExecutionCourseLogBean seclb,
            ExecutionCourse executionCourse) {
        Collection<ExecutionCourseLog> ecLogs = seclb.getExecutionCourseLogs();
        List<ExecutionCourseLog> listECLogs = new ArrayList<ExecutionCourseLog>(ecLogs);
        Collections.sort(listECLogs, ExecutionCourseLog.COMPARATOR_BY_WHEN_DATETIME);
        int entriesPerPage = 20;
        final CollectionPager<ExecutionCourseLog> pager = new CollectionPager<ExecutionCourseLog>(listECLogs, entriesPerPage);
        request.setAttribute("numberOfPages", (listECLogs.size() / entriesPerPage) + 1);

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
        request.setAttribute("pageNumber", page);

        SearchExecutionCourseLogBean logPagesBean = new SearchExecutionCourseLogBean(executionCourse);

        searchLogs(logPagesBean);

        logPagesBean.setExecutionCourseLogs(pager.getPage(page));
        if (seclb.getViewPhoto()) {
            logPagesBean.setViewPhoto(true);
        }
        request.setAttribute("logPagesBean", logPagesBean);
    }

    // copied from ExecutionCourse, was public
    private void searchLogs(SearchExecutionCourseLogBean bean) {
        final Predicate<ExecutionCourseLog> filter = bean.getFilters();
        final Collection<ExecutionCourseLog> validLogs = new HashSet<ExecutionCourseLog>();
        for (final ExecutionCourseLog log : bean.getExecutionCourse().getExecutionCourseLogsSet()) {
            if (filter.eval(log)) {
                validLogs.add(log);
            }
        }
        bean.setExecutionCourseLogs(validLogs);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SearchExecutionCourseLogBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();
        searchLogs(bean);

        request.setAttribute("searchBean", bean);
        request.setAttribute("executionCourse", bean.getExecutionCourse());

        prepareAttendsCollectionPages(request, bean, bean.getExecutionCourse());

        return forward(request, "/teacher/viewLogSearch.jsp");
    }

}
