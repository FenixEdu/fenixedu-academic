package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class Student2ndCycleInquiryTemplate extends Student2ndCycleInquiryTemplate_Base {

    public Student2ndCycleInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static Student2ndCycleInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof Student2ndCycleInquiryTemplate && inquiryTemplate.isOpen()) {
                return (Student2ndCycleInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

}
