package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class StudentTeacherInquiryTemplate extends StudentTeacherInquiryTemplate_Base {

    public StudentTeacherInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static StudentTeacherInquiryTemplate getCurrentTemplate() {
        final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentTeacherInquiryTemplate && inquiryTemplate.isOpen()) {
                return (StudentTeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static StudentTeacherInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentTeacherInquiryTemplate
                    && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (StudentTeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
