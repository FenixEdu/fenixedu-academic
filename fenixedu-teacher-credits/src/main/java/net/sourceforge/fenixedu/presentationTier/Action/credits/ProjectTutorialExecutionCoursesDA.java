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
package org.fenixedu.academic.ui.struts.action.credits;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.credits.util.DepartmentCreditsBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificCreditsApp.class, path = "execution-course-types",
        titleKey = "label.executionCourses.types", bundle = "TeacherCreditsSheetResources")
@Mapping(module = "scientificCouncil", path = "/projectTutorialCourses")
@Forwards(@Forward(name = "showDepartmentExecutionCourses", path = "/credits/showDepartmentExecutionCourses.jsp"))
public class ProjectTutorialExecutionCoursesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        if (departmentCreditsBean == null) {
            departmentCreditsBean = new DepartmentCreditsBean();
            departmentCreditsBean.setAvailableDepartments(Department.readActiveDepartments());
        }
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("showDepartmentExecutionCourses");
    }

    public ActionForward changeExecutionCourseType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        ExecutionCourse executionCourse = FenixFramework.getDomainObject((String) getFromRequest(request, "executionCourseOid"));
        executionCourse.changeProjectTutorialCourse();
        Department department = FenixFramework.getDomainObject((String) getFromRequest(request, "departmentOid"));
        DepartmentCreditsBean departmentCreditsBean =
                new DepartmentCreditsBean(department, new ArrayList<Department>(rootDomainObject.getDepartmentsSet()));
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("showDepartmentExecutionCourses");
    }

}