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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.common.base.Strings;

public abstract class ExecutionCourseBaseAction extends FenixDispatchAction {

    private static final ActionForward FORWARD = new ActionForward("/executionCourse/executionCourseFrame.jsp");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        propageIds(request);
        return super.execute(mapping, actionForm, request, response);
    }

    protected void propageIds(HttpServletRequest request) {
        propageContextIds(request);
    }

    public static ActionForward forward(HttpServletRequest request, String page) {
        request.setAttribute("teacher$actual$page", page);
        return FORWARD;
    }

    public static void propageContextIds(final HttpServletRequest request) {
        String executionCourseID = request.getParameter("executionCourseID");

        if (Strings.isNullOrEmpty(executionCourseID)) {
            executionCourseID = request.getParameter("objectCode");
        }

        if (executionCourseID != null && executionCourseID.length() > 0) {
            final Professorship professorship = findProfessorship(request, executionCourseID);
            request.setAttribute("professorship", professorship);
            request.setAttribute("executionCourse", professorship.getExecutionCourse());
            request.setAttribute("executionCourseID", executionCourseID);
        }
    }

    protected ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    private static Professorship findProfessorship(final HttpServletRequest request, final String executionCourseID) {
        final Person person = AccessControl.getPerson();
        if (person != null) {
            for (final Professorship professorship : person.getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                if (executionCourse.getExternalId().equals(executionCourseID)) {
                    return professorship;
                }
            }
        }
        throw new RuntimeException("User is not authorized to manage the selected course!");
    }

}
