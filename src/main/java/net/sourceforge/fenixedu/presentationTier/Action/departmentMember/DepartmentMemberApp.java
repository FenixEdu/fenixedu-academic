package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = DepartmentMemberApp.BUNDLE, path = "department-member", titleKey = "title.department",
        hint = DepartmentMemberApp.HINT, accessGroup = DepartmentMemberApp.ACCESS_GROUP)
@Mapping(path = "/index", module = "departmentMember", parameter = "/departmentMember/index.jsp")
public class DepartmentMemberApp extends ForwardAction {

    static final String HINT = "Department Member";
    static final String BUNDLE = "DepartmentMemberResources";
    static final String ACCESS_GROUP = "role(DEPARTMENT_MEMBER)";

    @StrutsApplication(bundle = BUNDLE, path = "teacher", titleKey = "label.teacher", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DepartmentMemberTeacherApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "department", titleKey = "title.department", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DepartmentMemberDepartmentApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "accompaniment", titleKey = "title.accompaniment", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DepartmentMemberAccompanimentApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "department-president", titleKey = "title.department.presidency", hint = HINT,
            accessGroup = "departmentPresident")
    public static class DepartmentMemberPresidentApp {
    }

    @StrutsApplication(bundle = "ResearcherResources", path = "messaging", titleKey = "title.unit.communication.section",
            hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DepartmentMemberMessagingApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = DepartmentMemberDepartmentApp.class, path = "teachers", titleKey = "link.departmentTeachers")
    @Mapping(path = "/viewDepartmentTeachers/listDepartmentTeachers", module = "departmentMember")
    public static class ListDepartmentTeachers extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = DepartmentMemberDepartmentApp.class, path = "teacher-service", titleKey = "link.teacherService")
    @Mapping(path = "/viewTeacherService/viewTeacherService", module = "departmentMember")
    public static class ViewTeacherService extends FacesEntryPoint {
    }

}
