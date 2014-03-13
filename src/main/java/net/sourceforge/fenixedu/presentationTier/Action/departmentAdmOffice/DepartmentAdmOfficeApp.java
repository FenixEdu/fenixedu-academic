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

    @StrutsApplication(bundle = BUNDLE, path = "view", titleKey = "link.group.view.title", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DepartmentAdmOfficeViewApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "teachers", titleKey = "link.group.teacher.title", hint = HINT,
            accessGroup = "role(DEPARTMENT_CREDITS_MANAGER)")
    public static class DepartmentAdmOfficeTeachersApp {
    }

    @StrutsApplication(bundle = "TeacherCreditsSheetResources", path = "credits", titleKey = "label.credits", hint = HINT,
            accessGroup = "role(DEPARTMENT_CREDITS_MANAGER)")
    public static class DepartmentAdmOfficeCreditsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "expectations", titleKey = "link.group.teacherPersonalExpectations.title",
            hint = HINT, accessGroup = "role(DEPARTMENT_CREDITS_MANAGER)")
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
