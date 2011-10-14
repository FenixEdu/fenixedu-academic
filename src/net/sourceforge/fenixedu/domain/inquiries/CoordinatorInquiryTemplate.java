package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class CoordinatorInquiryTemplate extends CoordinatorInquiryTemplate_Base {

    public CoordinatorInquiryTemplate(DateTime begin, DateTime end, Boolean shared) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	validateInquiryPeriod(begin, end);
	setResponsePeriodBegin(begin);
	setResponsePeriodEnd(end);
	setShared(shared);
    }

    public static CoordinatorInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof CoordinatorInquiryTemplate
		    && executionSemester == inquiryTemplate.getExecutionPeriod()) {
		return (CoordinatorInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }

    public static CoordinatorInquiryTemplate getCurrentTemplate() {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof CoordinatorInquiryTemplate && inquiryTemplate.isOpen()) {
		return (CoordinatorInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }
}
