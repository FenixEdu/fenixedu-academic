package net.sourceforge.fenixedu.presentationTier.Action.manager;

import org.fenixedu.bennu.portal.StrutsApplication;

public class ManagerApplications {

    @StrutsApplication(descriptionKey = "title.messages.and.notices", path = "messages-notices",
            titleKey = "title.messages.and.notices", bundle = "ManagerResources", accessGroup = "role(MANAGER)")
    public static class MessagesAndNoticesApp {

    }

    @StrutsApplication(descriptionKey = "title.manager.organizationalStructureManagement", path = "org-nstructure-management",
            titleKey = "title.manager.organizationalStructureManagement", bundle = "ManagerResources",
            accessGroup = "role(MANAGER)")
    public static class OrganizationalStructureManagementApp {

    }

    @StrutsApplication(descriptionKey = "title.teaching.structure", path = "teaching-structure",
            titleKey = "title.teaching.structure", bundle = "ManagerResources", accessGroup = "role(MANAGER)")
    public static class TeachingStructureApp {

    }

    @StrutsApplication(descriptionKey = "title.executions", path = "executions", titleKey = "title.executions",
            bundle = "ManagerResources", accessGroup = "role(MANAGER)")
    public static class ExecutionsManagementApp {

    }

    @StrutsApplication(descriptionKey = "title.people", path = "people-management", titleKey = "title.people",
            bundle = "ManagerResources", accessGroup = "role(MANAGER)")
    public static class PeopleManagementApp {

    }

    @StrutsApplication(descriptionKey = "label.manageFiles", path = "manage-files", titleKey = "label.manageFiles",
            bundle = "ManagerResources", accessGroup = "role(MANAGER)")
    public static class ManageFilesApp {

    }

    @StrutsApplication(descriptionKey = "label.manageFinance", path = "manage-finance", titleKey = "label.manageFinance",
            bundle = "ManagerResources", accessGroup = "role(MANAGER)")
    public static class ManageFinanceApp {

    }

    @StrutsApplication(descriptionKey = "title.support", path = "support", titleKey = "title.support",
            bundle = "ManagerResources", accessGroup = "role(MANAGER)")
    public static class SupportManagementApp {

    }

    @StrutsApplication(descriptionKey = "label.students", path = "students", titleKey = "label.students",
            bundle = "AcademicAdminOffice", accessGroup = "role(MANAGER)")
    public static class StudentsApp {

    }

    @StrutsApplication(descriptionKey = "label.access.control.persistent.groups.management", path = "access-control",
            titleKey = "label.access.control.persistent.groups.management", bundle = "ManagerResources",
            accessGroup = "role(MANAGER)")
    public static class AccessControlApp {

    }
}
