package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class CurricularCourseInquiryTemplate extends CurricularCourseInquiryTemplate_Base {

    public CurricularCourseInquiryTemplate() {
	super();
    }

    public CurricularCourseInquiryTemplate(DateTime begin, DateTime end) {
	this();
	validateInquiryPeriod(begin, end);
	setResponsePeriodBegin(begin);
	setResponsePeriodEnd(end);
    }

    public static CurricularCourseInquiryTemplate getCurrentTemplate() {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof CurricularCourseInquiryTemplate && inquiryTemplate.isOpen()) {
		return (CurricularCourseInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }
}
