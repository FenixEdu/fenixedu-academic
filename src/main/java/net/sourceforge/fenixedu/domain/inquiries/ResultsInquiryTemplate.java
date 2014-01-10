package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.domain.Bennu;

public class ResultsInquiryTemplate extends ResultsInquiryTemplate_Base {

    public ResultsInquiryTemplate() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static ResultsInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof ResultsInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (ResultsInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
