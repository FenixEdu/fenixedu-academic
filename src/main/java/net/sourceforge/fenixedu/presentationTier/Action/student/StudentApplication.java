package net.sourceforge.fenixedu.presentationTier.Action.student;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

public class StudentApplication extends ForwardAction {

    static final String HINT = "Student";
    static final String ACCESS_GROUP = "role(STUDENT)";
    static final String BUNDLE = "StudentResources";

    @StrutsApplication(path = "consult", titleKey = "consult", bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentViewApp {
    }

    @StrutsApplication(descriptionKey = "participate", path = "participate", titleKey = "participate", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentParticipateApp {
    }

    @StrutsApplication(descriptionKey = "submit", path = "submit", titleKey = "submit", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentSubmitApp {
    }

    @StrutsApplication(descriptionKey = "enroll", path = "enroll", titleKey = "enroll", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentEnrollApp {
    }

    @StrutsApplication(descriptionKey = "link.student.seniorTitle", path = "finalists", titleKey = "link.student.seniorTitle",
            bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentSeniorsApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = StudentEnrollApp.class, path = "evaluations", titleKey = "link.evaluations.enrolment")
    @Mapping(path = "/enrollment/evaluations/showEvaluations", module = "student")
    public static class ShowStudentEvaluations extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = StudentEnrollApp.class, path = "exams", titleKey = "link.exams.enrolment")
    @Mapping(path = "/enrollment/evaluations/showStudentExams", module = "student",
            parameter = "/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1")
    public static class ShowStudentExams extends ForwardAction {
    }

    @StrutsFunctionality(app = StudentEnrollApp.class, path = "written-tests", titleKey = "link.writtenTests.enrolment")
    @Mapping(path = "/enrollment/evaluations/showStudentWrittenTests", module = "student",
            parameter = "/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2")
    public static class ShowStudentWrittenTests extends ForwardAction {
    }
}
