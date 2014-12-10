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
package org.fenixedu.academic.ui.struts.action.departmentAdmOffice;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsApplication(bundle = "DepartmentAdmOfficeResources", path = "department-admin-office",
        titleKey = "label.departmentAdmOffice", hint = "Department Admin Office",
        accessGroup = "role(DEPARTMENT_ADMINISTRATIVE_OFFICE)")
@Mapping(path = "/index", module = "departmentAdmOffice", parameter = "/departmentAdmOffice/index.jsp")
public class DepartmentAdmOfficeApp extends ForwardAction {

    private static final String BUNDLE = "DepartmentAdmOfficeResources";
    private static final String HINT = "Department Admin Office";
    private static final String ACCESS_GROUP = "role(DEPARTMENT_ADMINISTRATIVE_OFFICE)";
    private static final String CREDITS_ACCESS_GROUP =
            "role(DEPARTMENT_CREDITS_MANAGER) & role(DEPARTMENT_ADMINISTRATIVE_OFFICE)";

    @StrutsApplication(bundle = BUNDLE, path = "view", titleKey = "link.group.view.title", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DepartmentAdmOfficeViewApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "expectations", titleKey = "link.group.teacherPersonalExpectations.title",
            hint = HINT, accessGroup = CREDITS_ACCESS_GROUP)
    public static class DepartmentAdmOfficeExpectationsApp {
    }

    // Faces Entry Point

    // Entry Points

    @StrutsFunctionality(app = DepartmentAdmOfficeExpectationsApp.class, path = "periods", titleKey = "label.periodDefinition")
    @Mapping(path = "/expectationPeriods", module = "departmentAdmOffice",
            parameter = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/defineExpectationPeriods.jsp")
    public static class DefineExpectationPeriods extends ForwardAction {
    }

}
