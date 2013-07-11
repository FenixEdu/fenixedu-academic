package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryGroupQuestion extends InquiryGroupQuestion_Base {

    public InquiryGroupQuestion() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setRequired(false);
    }

    public boolean isVisible(StudentInquiryRegistry studentInquiryRegistry) {
        for (QuestionCondition questionCondition : getQuestionConditions()) {
            if (questionCondition instanceof ECTSVisibleCondition) {
                return ((ECTSVisibleCondition) questionCondition).isVisible(studentInquiryRegistry);
            }
        }
        return true;
    }

    public String[] getConditionValues(StudentInquiryRegistry studentInquiryRegistry) {
        for (QuestionCondition questionCondition : getQuestionConditions()) {
            if (questionCondition instanceof ECTSVisibleCondition) {
                return ((ECTSVisibleCondition) questionCondition).getConditionValues(studentInquiryRegistry);
            }
        }
        return null;
    }

    public boolean isScaleGroup() {
        if (getInquiryQuestionHeader() != null && getInquiryQuestionHeader().getScaleHeaders() != null) {
            return true;
        }
        for (InquiryQuestion inquiryQuestion : getInquiryQuestions()) {
            if (inquiryQuestion.isScaleQuestion()) {
                return true;
            }
        }
        return false;
    }

    public boolean isResultGroup(ExecutionSemester executionSemester) {
        return getInquiryBlock().isResultBlock(executionSemester);
    }

    public boolean isCheckbox() {
        for (InquiryQuestion inquiryQuestion : getInquiryQuestions()) {
            if (inquiryQuestion instanceof InquiryCheckBoxQuestion) {
                return true;
            }
        }
        return false;
    }

    public boolean isToPresentStandardResults() {
        for (InquiryQuestion inquiryQuestion : getInquiryQuestions()) {
            if (inquiryQuestion.getPresentResults()) {
                return true;
            }
        }
        return false;
    }

    public int getNumberOfMandatoryQuestions() {
        int count = 0;
        for (InquiryQuestion inquiryQuestion : getInquiryQuestions()) {
            if (inquiryQuestion.getRequired()) {
                count++;
            }
        }
        return count;
    }

    public void delete() {
        for (; !getInquiryQuestions().isEmpty(); getInquiryQuestions().get(0).delete()) {
            ;
        }
        for (; !getQuestionConditions().isEmpty(); getQuestionConditions().get(0).delete()) {
            ;
        }
        if (hasInquiryQuestionHeader()) {
            getInquiryQuestionHeader().delete();
        }
        if (hasResultQuestionHeader()) {
            getResultQuestionHeader().delete();
        }
        setInquiryBlock(null);
        setResultQuestion(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion> getInquiryQuestions() {
        return getInquiryQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryQuestions() {
        return !getInquiryQuestionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.QuestionCondition> getQuestionConditions() {
        return getQuestionConditionsSet();
    }

    @Deprecated
    public boolean hasAnyQuestionConditions() {
        return !getQuestionConditionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasGroupResultType() {
        return getGroupResultType() != null;
    }

    @Deprecated
    public boolean hasRequired() {
        return getRequired() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasResultQuestion() {
        return getResultQuestion() != null;
    }

    @Deprecated
    public boolean hasInquiryBlock() {
        return getInquiryBlock() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestionHeader() {
        return getInquiryQuestionHeader() != null;
    }

    @Deprecated
    public boolean hasResultQuestionHeader() {
        return getResultQuestionHeader() != null;
    }

    @Deprecated
    public boolean hasGroupOrder() {
        return getGroupOrder() != null;
    }

}
