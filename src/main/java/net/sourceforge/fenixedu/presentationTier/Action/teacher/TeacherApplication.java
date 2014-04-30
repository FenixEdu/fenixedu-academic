package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import org.fenixedu.bennu.portal.StrutsApplication;

public class TeacherApplication {

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.phds", accessGroup = "role(TEACHER)",
            hint = "Teacher")
    public static class TeacherPhdApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "final-work", titleKey = "link.manage.finalWork",
            accessGroup = "role(TEACHER)", hint = "Teacher")
    public static class TeacherFinalWorkApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "tutor", titleKey = "link.teacher.tutor.operations",
            accessGroup = "role(TEACHER)", hint = "Teacher")
    public static class TeacherTutorApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "teaching", titleKey = "title.teaching",
            accessGroup = "role(TEACHER)", hint = "Teacher")
    public static class TeacherTeachingApp {
    }

}
