package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class TeacherInquiryTemplate extends TeacherInquiryTemplate_Base {

    public TeacherInquiryTemplate() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static TeacherInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof TeacherInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
		return (TeacherInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }
}
