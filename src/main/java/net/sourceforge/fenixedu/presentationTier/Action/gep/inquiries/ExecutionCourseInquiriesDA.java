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
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries.SelectAllExecutionCoursesForInquiries;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepInquiriesApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = GepInquiriesApp.class, path = "define-available-execution-courses",
        titleKey = "link.inquiries.execution.course.define.available.for.evaluation")
@Mapping(module = "gep", path = "/executionCourseInquiries")
@Forwards(@Forward(name = "showExecutionCoursesForInquiries", path = "/gep/inquiries/showExecutionCoursesForInquiries.jsp"))
public class ExecutionCourseInquiriesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionCourseSearchBean executionCourseSearchBean = getRenderedObject();
        if (executionCourseSearchBean == null) {
            executionCourseSearchBean = new ExecutionCourseSearchBean();
            final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
            executionCourseSearchBean.setExecutionPeriod(executionSemester);
        } else {
            final Collection<ExecutionCourse> executionCourses = executionCourseSearchBean.search();
            if (executionCourses != null) {
                request.setAttribute("executionCourses", executionCourses);
            }
            RenderUtils.invalidateViewState("executionCourses");
        }
        request.setAttribute("executionCourseSearchBean", executionCourseSearchBean);
        return mapping.findForward("showExecutionCoursesForInquiries");
    }

    public ActionForward selectAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourseSearchBean executionCourseSearchBean = getRenderedObject();
        SelectAllExecutionCoursesForInquiries.run(executionCourseSearchBean);
        return search(mapping, actionForm, request, response);
    }

    public ActionForward unselectAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SelectAllExecutionCoursesForInquiries.unselectAll((ExecutionCourseSearchBean) getRenderedObject());
        return search(mapping, actionForm, request, response);
    }

}