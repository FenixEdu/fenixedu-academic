package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import org.fenixedu.bennu.portal.StrutsApplication;

public class TeacherApplication {

    private static final String ACCESS_GROUP = "role(TEACHER) | professors";

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.phds", accessGroup = ACCESS_GROUP,
            hint = "Teacher")
    public static class TeacherPhdApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "final-work", titleKey = "link.manage.finalWork",
            accessGroup = ACCESS_GROUP, hint = "Teacher")
    public static class TeacherFinalWorkApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "tutor", titleKey = "link.teacher.tutor.operations",
            accessGroup = ACCESS_GROUP, hint = "Teacher")
    public static class TeacherTutorApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "teaching", titleKey = "title.teaching",
            accessGroup = ACCESS_GROUP, hint = "Teacher")
    public static class TeacherTeachingApp {
    }

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "mobility", titleKey = "label.application.mobility",
            accessGroup = "role(TEACHER)", hint = "Teacher")
    public static class TeacherMobilityApp {
    }

}
