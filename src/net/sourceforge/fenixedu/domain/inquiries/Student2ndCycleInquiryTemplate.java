package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class Student2ndCycleInquiryTemplate extends Student2ndCycleInquiryTemplate_Base {
    
    public Student2ndCycleInquiryTemplate(DateTime begin, DateTime end) {
        super();
	init(begin, end);
    }

    public static Student2ndCycleInquiryTemplate getCurrentTemplate() {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof Student2ndCycleInquiryTemplate && inquiryTemplate.isOpen()) {
		return (Student2ndCycleInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }

}
