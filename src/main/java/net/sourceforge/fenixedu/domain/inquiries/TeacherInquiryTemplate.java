package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class TeacherInquiryTemplate extends TeacherInquiryTemplate_Base {

    public TeacherInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static TeacherInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof TeacherInquiryTemplate && inquiryTemplate.isOpen()) {
                return (TeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static TeacherInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof TeacherInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (TeacherInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
