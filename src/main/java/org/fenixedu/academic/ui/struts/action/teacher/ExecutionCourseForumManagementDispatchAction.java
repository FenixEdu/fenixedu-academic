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
package org.fenixedu.academic.ui.struts.action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.messaging.ForunsManagement;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * 
 * @author naat
 * @author pcma
 */
@Mapping(module = "teacher", path = "/executionCourseForumManagement", functionality = ManageExecutionCourseDA.class)
@Forwards({ @Forward(name = "viewForum", path = "/commons/forums/viewForum.jsp"),
        @Forward(name = "viewThread", path = "/commons/forums/viewThread.jsp"),
        @Forward(name = "createThreadAndMessage", path = "/commons/forums/createThreadAndMessage.jsp"),
        @Forward(name = "viewForuns", path = "/teacher/forums/viewExecutionCourseForuns.jsp") })
public class ExecutionCourseForumManagementDispatchAction extends ForunsManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ManageExecutionCourseDA.propageContextIds(request);

        String executionCourseId = (String) request.getAttribute("executionCourseID");
        request.setAttribute("module", "/teacher");
        request.setAttribute("contextPrefix", "/executionCourseForumManagement.do?executionCourseID=" + executionCourseId);
        request.setAttribute("executionCourseId", executionCourseId);

        ActionForward forward = super.execute(mapping, actionForm, request, response);
        return ManageExecutionCourseDA.forward(request, forward.getPath());
    }

    public ActionForward viewForuns(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        request.setAttribute("foruns", executionCourse.getForuns());

        return mapping.findForward("viewForuns");
    }

}