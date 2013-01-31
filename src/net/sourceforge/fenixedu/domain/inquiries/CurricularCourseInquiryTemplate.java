package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class CurricularCourseInquiryTemplate extends CurricularCourseInquiryTemplate_Base {

	public CurricularCourseInquiryTemplate(DateTime begin, DateTime end) {
		super();
		init(begin, end);
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

	public static CurricularCourseInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
		final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
		for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
			if (inquiryTemplate instanceof CurricularCourseInquiryTemplate
					&& executionSemester == inquiryTemplate.getExecutionPeriod()) {
				return (CurricularCourseInquiryTemplate) inquiryTemplate;
			}
		}
		return null;
	}
}
