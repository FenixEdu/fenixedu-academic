package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;

public class CoordinatorInquiryTemplate extends CoordinatorInquiryTemplate_Base {

    public CoordinatorInquiryTemplate(DateTime begin, DateTime end, Boolean shared) {
        super();
        init(begin, end);
        setShared(shared);
    }

    public static CoordinatorInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof CoordinatorInquiryTemplate
                    && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (CoordinatorInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static CoordinatorInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof CoordinatorInquiryTemplate && inquiryTemplate.isOpen()) {
                return (CoordinatorInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public void delete() {
        canBeDeleted();
        for (InquiryBlock inquiryBlock : getInquiryBlocksSet()) {
            removeInquiryBlocks(inquiryBlock);
        }
        setExecutionPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void canBeDeleted() {
        for (InquiryBlock inquiryBlock : getInquiryBlocks()) {
            for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
                    for (QuestionAnswer questionAnswer : inquiryQuestion.getQuestionAnswers()) {
                        InquiryCoordinatorAnswer coordinatorAnswer = (InquiryCoordinatorAnswer) questionAnswer.getInquiryAnswer();
                        if (coordinatorAnswer.getExecutionSemester() == getExecutionPeriod()) {
                            throw new DomainException("error.CoordinatorInquiryTemplate.hasGivenAnswers");
                        }
                    }
                }
            }
        }

    }

    @Deprecated
    public boolean hasShared() {
        return getShared() != null;
    }

}
