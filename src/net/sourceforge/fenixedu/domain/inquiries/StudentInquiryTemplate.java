package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class StudentInquiryTemplate extends StudentInquiryTemplate_Base {

    public StudentInquiryTemplate() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public StudentInquiryTemplate(DateTime begin, DateTime end) {
	this();
	validateInquiryPeriod(begin, end);
	setResponsePeriodBegin(begin);
	setResponsePeriodEnd(end);
    }

    public static StudentInquiryTemplate getCurrentTemplate() {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof StudentInquiryTemplate && inquiryTemplate.isOpen()) {
		return (StudentInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }

    public static List<StudentInquiryTemplate> getInquiryTemplatesByExecutionPeriod(ExecutionSemester executioPeriod) {
	List<StudentInquiryTemplate> studentInquiryTemplates = new ArrayList<StudentInquiryTemplate>();
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof StudentInquiryTemplate && inquiryTemplate.getExecutionPeriod() == executioPeriod) {
		studentInquiryTemplates.add((StudentInquiryTemplate) inquiryTemplate);
	    }
	}
	return studentInquiryTemplates;
    }
}
