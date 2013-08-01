package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;

public class Student1rstCycleInquiryTemplate extends Student1rstCycleInquiryTemplate_Base {

    public Student1rstCycleInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static Student1rstCycleInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof Student1rstCycleInquiryTemplate && inquiryTemplate.isOpen()) {
                return (Student1rstCycleInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
