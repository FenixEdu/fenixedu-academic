package net.sourceforge.fenixedu.domain.inquiries;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class InquiryBlock extends InquiryBlock_Base {

    public InquiryBlock() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
        for (; !getInquiryGroupsQuestions().isEmpty(); getInquiryGroupsQuestions().iterator().next().delete()) {
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
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryTemplate> getInquiries() {
        return getInquiriesSet();
    }

    @Deprecated
    public boolean hasAnyInquiries() {
        return !getInquiriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion> getInquiryGroupsQuestions() {
        return getInquiryGroupsQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryGroupsQuestions() {
        return !getInquiryGroupsQuestionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasGroupResultQuestion() {
        return getGroupResultQuestion() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasResultQuestion() {
        return getResultQuestion() != null;
    }

    @Deprecated
    public boolean hasBlockOrder() {
        return getBlockOrder() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestionHeader() {
        return getInquiryQuestionHeader() != null;
    }

}
