package net.sourceforge.fenixedu.presentationTier.Action.student;

import org.fenixedu.bennu.portal.StrutsApplication;

public class StudentApplication {

    @StrutsApplication(descriptionKey = "consult", path = "consult", titleKey = "consult", bundle = "StudentResources",
            accessGroup = "role(STUDENT)", hint = "Student")
    public static class StudentViewApp {

    }

    @StrutsApplication(descriptionKey = "participate", path = "participate", titleKey = "participate",
            bundle = "StudentResources", accessGroup = "role(STUDENT)", hint = "Student")
    public static class ParticipateApp {

    }

    @StrutsApplication(descriptionKey = "submit", path = "submit", titleKey = "submit", bundle = "StudentResources",
            accessGroup = "role(STUDENT)", hint = "Student")
    public static class SubmitApp {

    }

    @StrutsApplication(descriptionKey = "enroll", path = "enroll", titleKey = "enroll", bundle = "StudentResources",
            accessGroup = "role(STUDENT)", hint = "Student")
    public static class EnrollApp {

    }

    @StrutsApplication(descriptionKey = "link.student.seniorTitle", path = "finalists", titleKey = "link.student.seniorTitle",
            bundle = "StudentResources", accessGroup = "role(STUDENT)", hint = "Student")
    public static class FinalistsApp {

    }
}
