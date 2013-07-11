package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryBlock extends InquiryBlock_Base {

    public InquiryBlock() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isResultBlock(ExecutionSemester executionSemester) {
        return getInquiry(executionSemester) instanceof ResultsInquiryTemplate;
    }

    public InquiryTemplate getInquiry(ExecutionSemester executionSemester) {
        for (InquiryTemplate inquiryTemplate : getInquiries()) {
            if (inquiryTemplate.getExecutionPeriod() == executionSemester) {
                return inquiryTemplate;
            }
        }
        return null;
    }

    public void delete() {
        for (; !getInquiryGroupsQuestions().isEmpty(); getInquiryGroupsQuestions().get(0).delete()) {
            setRootDomainObject(null);
        }
        if (hasInquiryQuestionHeader()) {
            getInquiryQuestionHeader().delete();
        }
        setResultQuestion(null);
        setGroupResultQuestion(null);
        for (InquiryTemplate inquiryTemplate : getInquiries()) {
            removeInquiries(inquiryTemplate);
        }
        super.deleteDomainObject();
    }
}
