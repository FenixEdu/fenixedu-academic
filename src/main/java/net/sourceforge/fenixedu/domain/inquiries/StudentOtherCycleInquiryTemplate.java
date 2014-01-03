package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class StudentOtherCycleInquiryTemplate extends StudentOtherCycleInquiryTemplate_Base {

    public StudentOtherCycleInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static StudentOtherCycleInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentOtherCycleInquiryTemplate && inquiryTemplate.isOpen()) {
                return (StudentOtherCycleInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
