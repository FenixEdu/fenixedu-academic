package org.fenixedu.academic.ui.struts.action.student.enrollment;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.student.Registration;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public abstract class EnrolmentContextHandler {
    private static EnrolmentContextHandler enrolmentContextHandler;

    public static EnrolmentContextHandler getRegisteredEnrolmentContextHandler() {
        if (enrolmentContextHandler != null) {
            return enrolmentContextHandler;
        } else {
            return new DefaultEnrolmentContextHandler();
        }
    }

    public static void registerEnrolmentContextHandler(EnrolmentContextHandler newEnrolmentContextHandler) {
        if (enrolmentContextHandler != null) {
            throw new RuntimeException("Another enrolment context handler is registered. The registered handler is of type: "
                    + enrolmentContextHandler.getClass().getName());
        }
        enrolmentContextHandler = newEnrolmentContextHandler;
    }

    public abstract Optional<String> getReturnURLForStudentInCurricularCourses(HttpServletRequest request,
            Registration registration);

    public abstract Optional<String> getReturnURLForStudentInClasses(HttpServletRequest request, Registration registration);

    public static class DefaultEnrolmentContextHandler extends EnrolmentContextHandler {

        @Override
        public Optional<String> getReturnURLForStudentInCurricularCourses(HttpServletRequest request, Registration registration) {
            return Optional.empty();
        }

        @Override
        public Optional<String> getReturnURLForStudentInClasses(HttpServletRequest request, Registration registration) {
            String link = request.getContextPath() + "/student/showStudentPortal.do";
            String injectedLink =
                    GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), link, request.getSession());
            return Optional.of(injectedLink);

        }

    }
}
