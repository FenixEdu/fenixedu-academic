/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class InquiryQuestion extends InquiryQuestion_Base {

    public InquiryQuestion() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRequired(false);
        setAutofit(false);
        setNewRow(false);
        setShowRequiredMark(false);
        setHasClassification(false);
        setPresentResults(true);
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

    public void delete() {
        if (!getInquiryResults().isEmpty()) {
            throw new DomainException("error.inquiryQuestion.can.not.delete.hasAssociatedResults");
        }
        if (!getQuestionAnswers().isEmpty()) {
            throw new DomainException("error.inquiryQuestion.can.not.delete.hasAssociatedAnswers");
        }
        for (; !getQuestionConditions().isEmpty(); getQuestionConditions().iterator().next().delete()) {
            ;
        }
        if (hasInquiryQuestionHeader()) {
            getInquiryQuestionHeader().delete();
        }
        for (InquiryBlock inquiryBlock : getAssociatedBlocks()) {
            removeAssociatedBlocks(inquiryBlock);
        }
        for (InquiryBlock inquiryBlock : getAssociatedResultBlocks()) {
            removeAssociatedResultBlocks(inquiryBlock);
        }
        setCheckboxGroupQuestion(null);
        setDependentQuestionCondition(null);
        setInquiryGroupQuestion(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isScaleQuestion() {
        return ((getInquiryGroupQuestion().getInquiryQuestionHeader() != null && getInquiryGroupQuestion()
                .getInquiryQuestionHeader().getScaleHeaders() != null) || (getInquiryQuestionHeader() != null && getInquiryQuestionHeader()
                .getScaleHeaders() != null));
    }

    public boolean isResultQuestion(ExecutionSemester executionSemester) {
        return getInquiryGroupQuestion().getInquiryBlock().isResultBlock(executionSemester);
    }

    public boolean hasGroupDependentQuestionCondition() {
        return getDependentQuestionCondition() != null && getDependentQuestionCondition().hasInquiryGroupQuestion();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryBlock> getAssociatedBlocks() {
        return getAssociatedBlocksSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedBlocks() {
        return !getAssociatedBlocksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryBlock> getAssociatedResultBlocks() {
        return getAssociatedResultBlocksSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedResultBlocks() {
        return !getAssociatedResultBlocksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer> getQuestionAnswers() {
        return getQuestionAnswersSet();
    }

    @Deprecated
    public boolean hasAnyQuestionAnswers() {
        return !getQuestionAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResult> getInquiryResults() {
        return getInquiryResultsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResults() {
        return !getInquiryResultsSet().isEmpty();
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
    public boolean hasQuestionOrder() {
        return getQuestionOrder() != null;
    }

    @Deprecated
    public boolean hasDependentQuestionCondition() {
        return getDependentQuestionCondition() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAutofit() {
        return getAutofit() != null;
    }

    @Deprecated
    public boolean hasHasClassification() {
        return getHasClassification() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestionHeader() {
        return getInquiryQuestionHeader() != null;
    }

    @Deprecated
    public boolean hasRequired() {
        return getRequired() != null;
    }

    @Deprecated
    public boolean hasShowRequiredMark() {
        return getShowRequiredMark() != null;
    }

    @Deprecated
    public boolean hasLabel() {
        return getLabel() != null;
    }

    @Deprecated
    public boolean hasNewRow() {
        return getNewRow() != null;
    }

    @Deprecated
    public boolean hasInquiryGroupQuestion() {
        return getInquiryGroupQuestion() != null;
    }

    @Deprecated
    public boolean hasToolTip() {
        return getToolTip() != null;
    }

    @Deprecated
    public boolean hasCheckboxGroupQuestion() {
        return getCheckboxGroupQuestion() != null;
    }

    @Deprecated
    public boolean hasPresentResults() {
        return getPresentResults() != null;
    }

}
