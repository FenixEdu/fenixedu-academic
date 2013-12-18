package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class RegentInquiryTemplate extends RegentInquiryTemplate_Base {

    public RegentInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static RegentInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof RegentInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (RegentInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static RegentInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof RegentInquiryTemplate && inquiryTemplate.isOpen()) {
                return (RegentInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
