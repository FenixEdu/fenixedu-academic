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
package org.fenixedu.academic.ui.struts.action.departmentMember;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.messaging.DepartmentForum;
import org.fenixedu.academic.domain.messaging.Forum;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberApp.DepartmentMemberDepartmentApp;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.messaging.ForunsManagement;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = DepartmentMemberDepartmentApp.class, path = "forum", titleKey = "link.foruns")
@Mapping(path = "/departmentForum", module = "departmentMember")
@Forwards({ @Forward(name = "viewDepartmentForum", path = "/departmentMember/forum/viewDepartmentForum.jsp"),
        @Forward(name = "viewForum", path = "/commons/forums/viewForum.jsp"),
        @Forward(name = "createThreadAndMessage", path = "/commons/forums/createThreadAndMessage.jsp"),
        @Forward(name = "viewThread", path = "/commons/forums/viewThread.jsp") })
public class DepartmentForumDA extends ForunsManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("contextPrefix", "/departmentForum.do");
        request.setAttribute("module", "/departmentMember");
        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {
        List<DepartmentForum> foruns = new ArrayList<DepartmentForum>();
        Person loggedPerson = getLoggedPerson(request);
        for (Department department : rootDomainObject.getDepartmentsSet()) {
            if (belongsPersonWithDepartment(loggedPerson, department)) {
                foruns.add(department.getDepartmentForum());
            }
        }

        if (foruns.size() == 1) {
            request.setAttribute("forum", foruns.iterator().next());
            return viewForum(mapping, form, request, response);
        }

        request.setAttribute("foruns", foruns);
        return mapping.findForward("viewDepartmentForum");
    }

    @Override
    protected Forum getRequestedForum(HttpServletRequest request) {
        String forumId = request.getParameter("forumId");
        if (forumId == null) {
            return (Forum) request.getAttribute("forum");
        }
        return super.getRequestedForum(request);
    }

    private boolean belongsPersonWithDepartment(Person person, Department department) {
        DepartmentForum departmentForum = department.getDepartmentForum();
        if (departmentForum != null) {
            return departmentForum.getDepartmentForumGroup().isMember(person.getUser());
        }
        return false;
    }
}
