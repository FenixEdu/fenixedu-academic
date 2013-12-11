package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;

public class CurricularCourseInquiryTemplate extends CurricularCourseInquiryTemplate_Base {

    public CurricularCourseInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static CurricularCourseInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof CurricularCourseInquiryTemplate && inquiryTemplate.isOpen()) {
                return (CurricularCourseInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static CurricularCourseInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof CurricularCourseInquiryTemplate
                    && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (CurricularCourseInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    @Deprecated
    public boolean hasCourseType() {
        return getCourseType() != null;
    }

}
