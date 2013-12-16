package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;

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
