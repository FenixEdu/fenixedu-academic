package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class StudentTeacherInquiryTemplate extends StudentTeacherInquiryTemplate_Base {

    public StudentTeacherInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static StudentTeacherInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentTeacherInquiryTemplate && inquiryTemplate.isOpen()) {
                return (StudentTeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static StudentTeacherInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentTeacherInquiryTemplate
                    && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (StudentTeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
