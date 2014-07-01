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
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberMessagingApp;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ProjectSubmissionsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        final Department department = AccessControl.getPerson().getTeacher().getCurrentWorkingDepartment();
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
