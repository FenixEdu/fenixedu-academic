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
package org.fenixedu.academic.ui.struts.action.department;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberApp.DepartmentMemberMessagingApp;
import org.fenixedu.academic.ui.struts.action.teacher.ProjectSubmissionsManagementDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = DepartmentMemberMessagingApp.class, path = "project-submission", titleKey = "label.showProjects",
        bundle = "ResearcherResources")
@Mapping(path = "/projectSubmissionsManagement", module = "departmentMember")
@Forwards({
        @Forward(name = "viewProjectSubmissionsByGroup", path = "/teacher/evaluation/viewProjectSubmissionsByGroup.jsp"),
        @Forward(name = "showProjects", path = "/departmentMember/showProjects.jsp"),
        @Forward(name = "viewLastProjectSubmissionForEachGroup",
                path = "/teacher/evaluation/viewLastProjectSubmissionForEachGroup.jsp"),
        @Forward(name = "selectiveDownload", path = "/teacher/evaluation/selectiveDownload.jsp") })
public class DepartmentProjectSubmissionDA extends ProjectSubmissionsManagementDispatchAction {

    @EntryPoint
    public ActionForward showProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Department department = AccessControl.getPerson().getTeacher().getDepartment();
        Map<ExecutionCourse, Set<Project>> coursesProjects = new HashMap<ExecutionCourse, Set<Project>>();
        for (Project project : department.getProjectsSet()) {
            for (ExecutionCourse course : project.getAssociatedExecutionCoursesSet()) {
                Set<Project> projects = coursesProjects.get(course);
                if (projects == null) {
                    projects = new HashSet<Project>();
                    coursesProjects.put(course, projects);
                }
                projects.add(project);
            }
        }
        request.setAttribute("coursesProjects", coursesProjects);
        return mapping.findForward("showProjects");
    }

    @Override
    protected ActionForward doForward(HttpServletRequest request, String path) {
        return new ActionForward("path", path, false, "");
    }

    @Override
    protected void propageIds(HttpServletRequest request) {
        // Do nothing
    }

}
