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
package org.fenixedu.academic.ui.struts.action.credits.departmentMember;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.credits.util.DepartmentCreditsBean;
import org.fenixedu.academic.ui.struts.action.credits.CreditsReportsDA;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberApp.DepartmentMemberDepartmentApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = DepartmentMemberDepartmentApp.class, path = "export-credits",
        titleKey = "label.executionCourses.types", bundle = "TeacherCreditsSheetResources")
@Mapping(module = "departmentMember", path = "/exportCredits")
public class DepartmentMemberCreditsReportsDA extends CreditsReportsDA {

    @Override
    @EntryPoint
    public ActionForward prepareExportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        User userView = Authenticate.getUser();
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        List<Department> availableDepartments = new ArrayList<Department>();
        availableDepartments.add(userView.getPerson().getTeacher().getDepartment());
        departmentCreditsBean.setAvailableDepartments(availableDepartments);
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("exportDepartmentCourses");
    }
}