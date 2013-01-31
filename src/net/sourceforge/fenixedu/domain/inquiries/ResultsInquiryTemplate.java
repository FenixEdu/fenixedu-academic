package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ResultsInquiryTemplate extends ResultsInquiryTemplate_Base {

	public ResultsInquiryTemplate() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public static ResultsInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
		final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
		for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
			if (inquiryTemplate instanceof ResultsInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
				return (ResultsInquiryTemplate) inquiryTemplate;
			}
		}
		return null;
	}
}
