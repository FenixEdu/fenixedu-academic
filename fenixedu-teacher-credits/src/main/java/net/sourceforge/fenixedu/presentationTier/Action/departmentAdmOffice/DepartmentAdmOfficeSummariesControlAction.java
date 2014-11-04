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
package org.fenixedu.academic.ui.struts.action.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeViewApp;
import org.fenixedu.academic.ui.struts.action.directiveCouncil.SummariesControlAction;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = DepartmentAdmOfficeViewApp.class, path = "summaries-control", titleKey = "link.summaries.control",
        bundle = "ApplicationResources")
@Mapping(module = "departmentAdmOffice", path = "/summariesControl")
@Forwards(@Forward(name = "success", path = "/departmentAdmOffice/summariesControl/listTeacherSummariesControl.jsp"))
public class DepartmentAdmOfficeSummariesControlAction extends SummariesControlAction {

    @Override
    protected void readAndSaveAllDepartments(HttpServletRequest request) throws FenixServiceException {

        List<LabelValueBean> departments = new ArrayList<LabelValueBean>();
        final User userView = Authenticate.getUser();
        Person person = userView.getPerson();
        Collection<Department> manageableDepartments = person.getManageableDepartmentCreditsSet();
        for (Department department : manageableDepartments) {
            LabelValueBean bean = new LabelValueBean();
            bean.setLabel(department.getRealName());
            bean.setValue(department.getExternalId().toString());
            departments.add(bean);
        }
        request.setAttribute("departments", departments);
    }

}
