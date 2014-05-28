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
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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

    @StrutsApplication(bundle = BUNDLE, path = "teachers", titleKey = "link.group.teacher.title", hint = HINT,
            accessGroup = CREDITS_ACCESS_GROUP)
    public static class DepartmentAdmOfficeTeachersApp {
    }

    @StrutsApplication(bundle = "TeacherCreditsSheetResources", path = "credits", titleKey = "label.credits", hint = HINT,
            accessGroup = CREDITS_ACCESS_GROUP)
    public static class DepartmentAdmOfficeCreditsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "expectations", titleKey = "link.group.teacherPersonalExpectations.title",
            hint = HINT, accessGroup = CREDITS_ACCESS_GROUP)
    public static class DepartmentAdmOfficeExpectationsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "students", titleKey = "link.group.students.title", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DepartmentAdmOfficeStudentsApp {
    }

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "listings", titleKey = "label.lists", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DepartmentAdmOfficeListingsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "groups", titleKey = "link.group.managementGroups.title", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DepartmentAdmOfficeGroupsApp {
    }

    @StrutsApplication(bundle = "VigilancyResources", path = "exam-coordination",
            titleKey = "label.navheader.person.examCoordinator", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DepartmentAdmOfficeExamsApp {
    }

    @StrutsApplication(bundle = "ResearcherResources", path = "messaging", titleKey = "title.unit.communication.section",
            hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DepartmentAdmOfficeMessagingApp {
    }

    // Faces Entry Point

    @StrutsFunctionality(app = DepartmentAdmOfficeViewApp.class, path = "teacher-service", titleKey = "link.teacherService")
    @Mapping(path = "/viewTeacherService/viewTeacherService", module = "departmentAdmOffice")
    public static class ViewTeacherService extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = DepartmentAdmOfficeGroupsApp.class, path = "competence-course-management",
            titleKey = "link.competenceCoursesManagementGroup")
    @Mapping(path = "/managementGroups/competenceCoursesManagementGroup", module = "departmentAdmOffice")
    public static class CompetenceCoursesManagementGroup extends FacesEntryPoint {
    }

    // Entry Points

    @StrutsFunctionality(app = DepartmentAdmOfficeExpectationsApp.class, path = "periods", titleKey = "label.periodDefinition")
    @Mapping(path = "/expectationPeriods", module = "departmentAdmOffice",
            parameter = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/defineExpectationPeriods.jsp")
    public static class DefineExpectationPeriods extends ForwardAction {
    }

}
