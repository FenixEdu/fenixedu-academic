package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class DelegateInquiryTemplate extends DelegateInquiryTemplate_Base {

    public DelegateInquiryTemplate() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static DelegateInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof DelegateInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
		return (DelegateInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }
}
