package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

public abstract class StudentInquiryTemplate extends StudentInquiryTemplate_Base {

    public static StudentInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentInquiryTemplate && inquiryTemplate.isOpen()) {
                return (StudentInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static List<StudentInquiryTemplate> getInquiryTemplatesByExecutionPeriod(ExecutionSemester executioPeriod) {
        List<StudentInquiryTemplate> studentInquiryTemplates = new ArrayList<StudentInquiryTemplate>();
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentInquiryTemplate && inquiryTemplate.getExecutionPeriod() == executioPeriod) {
                studentInquiryTemplates.add((StudentInquiryTemplate) inquiryTemplate);
            }
        }
        return studentInquiryTemplates;
    }
}
