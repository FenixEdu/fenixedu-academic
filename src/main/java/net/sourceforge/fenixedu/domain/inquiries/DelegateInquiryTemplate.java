package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;

public class DelegateInquiryTemplate extends DelegateInquiryTemplate_Base {

    public DelegateInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static DelegateInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof DelegateInquiryTemplate && inquiryTemplate.isOpen()) {
                return (DelegateInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static DelegateInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof DelegateInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (DelegateInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
