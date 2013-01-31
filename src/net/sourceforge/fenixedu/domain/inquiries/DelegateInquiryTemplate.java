package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class DelegateInquiryTemplate extends DelegateInquiryTemplate_Base {

	public DelegateInquiryTemplate(DateTime begin, DateTime end) {
		super();
		init(begin, end);
	}

	public static DelegateInquiryTemplate getCurrentTemplate() {
		final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
		for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
			if (inquiryTemplate instanceof DelegateInquiryTemplate && inquiryTemplate.isOpen()) {
				return (DelegateInquiryTemplate) inquiryTemplate;
			}
		}
		return null;
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
