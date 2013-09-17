package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class StudentOtherCycleInquiryTemplate extends StudentOtherCycleInquiryTemplate_Base {

    public StudentOtherCycleInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static StudentOtherCycleInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentOtherCycleInquiryTemplate && inquiryTemplate.isOpen()) {
                return (StudentOtherCycleInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
