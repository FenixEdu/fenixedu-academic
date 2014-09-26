package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.MergeExecutionCourses;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduQUCContextListener implements ServletContextListener {
    public class InquiriesNotAnswered extends DomainException {
        private static final long serialVersionUID = -105643185383592476L;

        public InquiriesNotAnswered() {
            super("resources.ApplicationResources", "message.student.cannotEnroll.inquiriesNotAnswered");
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MergeExecutionCourses.registerMergeHandler(FenixEduQUCContextListener::copyInquiries);
        ClassEnrollmentAuthorizationFilter.registerCondition(registration -> {
            if (StudentInquiryRegistry.hasInquiriesToRespond(Authenticate.getUser().getPerson().getStudent())) {
                throw new InquiriesNotAnswered();
            }
        });
        FenixFramework.getDomainModel()
                .registerDeletionBlockerListener(
                        Professorship.class,
                        (professorship, blockers) -> {
                            if (!professorship.getInquiryStudentTeacherAnswersSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnyInquiryStudentTeacherAnswers"));
                            }
                            if (!professorship.getInquiryResultsSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnyInquiryResults"));
                            }
                            if (professorship.getInquiryTeacherAnswer() != null) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasInquiryTeacherAnswer"));
                            }
                            if (professorship.getInquiryRegentAnswer() != null) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasInquiryRegentAnswer"));
                            }
                        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private static void copyInquiries(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        for (final StudentInquiryRegistry studentInquiryRegistry : executionCourseFrom.getStudentsInquiryRegistriesSet()) {
            studentInquiryRegistry.setExecutionCourse(executionCourseTo);
        }
        for (final InquiryResult inquiryResult : executionCourseFrom.getInquiryResultsSet()) {
            inquiryResult.setExecutionCourse(executionCourseTo);
        }
        for (final InquiryCourseAnswer inquiryCourseAnswer : executionCourseFrom.getInquiryCourseAnswersSet()) {
            inquiryCourseAnswer.setExecutionCourse(executionCourseTo);
        }
    }
}
