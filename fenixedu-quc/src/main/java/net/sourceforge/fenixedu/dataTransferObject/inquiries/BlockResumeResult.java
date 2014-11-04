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
package org.fenixedu.academic.dto.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.inquiries.InquiryAnswer;
import org.fenixedu.academic.domain.inquiries.InquiryBlock;
import org.fenixedu.academic.domain.inquiries.InquiryGroupQuestion;
import org.fenixedu.academic.domain.inquiries.InquiryQuestion;
import org.fenixedu.academic.domain.inquiries.InquiryResponseState;
import org.fenixedu.academic.domain.inquiries.InquiryResult;
import org.fenixedu.academic.domain.inquiries.InquiryResultComment;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public abstract class BlockResumeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Person person;
    private ResultPersonCategory personCategory;
    private String firstHeaderKey;
    private String firstPresentationName;
    private Set<InquiryResult> resultBlocks;
    private boolean regentViewHimself;
    private boolean backToResume;

    protected abstract void initResultBlocks();

    protected abstract InquiryAnswer getInquiryAnswer();

    protected abstract int getNumberOfInquiryQuestions();

    protected abstract List<InquiryResult> getInquiryResultsByQuestion(InquiryQuestion inquiryQuestion);

    public List<Integer> getMandatoryIssues() {
        List<Integer> mandatory = new ArrayList<Integer>();
        for (InquiryResult inquiryResult : getResultBlocks()) {
            mandatory.add(mandatory.size(), getNumberOfMandatoryIssues(inquiryResult));
        }
        return mandatory;
    }

    private int getNumberOfMandatoryIssues(InquiryResult inquiryResult) {
        int count = 0;
        Collection<InquiryBlock> associatedBlocks = getAssociatedBlocks(inquiryResult);
        for (InquiryBlock inquiryBlock : associatedBlocks) {
            for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestionsSet()) {
                for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestionsSet()) {
                    List<InquiryResult> inquiryResultsQuestion = getInquiryResultsByQuestion(inquiryQuestion);
                    for (InquiryResult inquiryResultQuestion : inquiryResultsQuestion) {
                        if (isMandatoryComment(inquiryResultQuestion)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private boolean isMandatoryComment(InquiryResult inquiryResultQuestion) {
        if (!isRegentViewHimself()) {
            return inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment();
        } else {
            InquiryResultComment resultComment = inquiryResultQuestion.getInquiryResultComment(getPerson(), getPersonCategory());
            if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()
                    && (resultComment == null || StringUtils.isEmpty(resultComment.getComment()))) {
                return true;
            }
            return false;
        }
    }

    private int getCommentedMandatoryIssues(InquiryResult inquiryResult) {
        int count = 0;
        Collection<InquiryBlock> associatedBlocks = getAssociatedBlocks(inquiryResult);
        for (InquiryBlock inquiryBlock : associatedBlocks) {
            for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestionsSet()) {
                for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestionsSet()) {
                    List<InquiryResult> inquiryResultsQuestion = getInquiryResultsByQuestion(inquiryQuestion);
                    for (InquiryResult inquiryResultQuestion : inquiryResultsQuestion) {
                        InquiryResultComment inquiryResultComment =
                                inquiryResultQuestion != null ? inquiryResultQuestion.getInquiryResultComment(getPerson(),
                                        getPersonCategory()) : null;
                        if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()
                                && inquiryResultComment != null && !StringUtils.isEmpty(inquiryResultComment.getComment())) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public String getCompletionState() {
        return getCompletionStateType().getLocalizedName();
    }

    public InquiryResponseState getCompletionStateType() {
        int mandatoryIssues = 0;
        int mandatoryCommentedIssues = 0;
        for (InquiryResult inquiryResult : getResultBlocks()) {
            mandatoryIssues += getNumberOfMandatoryIssues(inquiryResult);
            mandatoryCommentedIssues += getCommentedMandatoryIssues(inquiryResult);
        }

        InquiryAnswer inquiryAnswer = getInquiryAnswer();
        int numberOfQuestions = getNumberOfInquiryQuestions();

        if ((mandatoryIssues > 0 && mandatoryCommentedIssues == 0 && inquiryAnswer == null)
                || (mandatoryIssues == 0 && inquiryAnswer == null)) {
            return InquiryResponseState.EMPTY;
        } else if (mandatoryIssues - mandatoryCommentedIssues > 0) {
            return InquiryResponseState.INCOMPLETE;
        } else if (inquiryAnswer == null || inquiryAnswer.getNumberOfAnsweredQuestions() < numberOfQuestions) {
            return InquiryResponseState.PARTIALLY_FILLED;
        } else {
            return InquiryResponseState.COMPLETE;
        }
    }

    private Collection<InquiryBlock> getAssociatedBlocks(InquiryResult inquiryResult) {
        Collection<InquiryBlock> associatedBlocks = inquiryResult.getInquiryQuestion().getAssociatedBlocksSet();
        if (!inquiryResult.getInquiryQuestion().getAssociatedResultBlocksSet().isEmpty()) {
            associatedBlocks = new ArrayList<InquiryBlock>();
            for (InquiryBlock inquiryBlock : inquiryResult.getInquiryQuestion().getAssociatedResultBlocksSet()) {
                for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestionsSet()) {
                    for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestionsSet()) {
                        associatedBlocks.addAll(inquiryQuestion.getAssociatedBlocksSet());
                    }
                }
            }
        }
        return associatedBlocks;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ResultPersonCategory getPersonCategory() {
        return personCategory;
    }

    public void setPersonCategory(ResultPersonCategory personCategory) {
        this.personCategory = personCategory;
    }

    public Set<InquiryResult> getResultBlocks() {
        return resultBlocks;
    }

    public void setResultBlocks(Set<InquiryResult> resultBlocks) {
        this.resultBlocks = resultBlocks;
    }

    public void setFirstHeaderKey(String firstHeaderKey) {
        this.firstHeaderKey = firstHeaderKey;
    }

    public String getFirstHeaderKey() {
        return firstHeaderKey;
    }

    public void setFirstPresentationName(String firstPresentationName) {
        this.firstPresentationName = firstPresentationName;
    }

    public String getFirstPresentationName() {
        return firstPresentationName;
    }

    public String getFirstHeaderName() {
        return BundleUtil.getString("resources.InquiriesResources", getFirstHeaderKey());
    }

    public void setRegentViewHimself(boolean regentViewHimself) {
        this.regentViewHimself = regentViewHimself;
    }

    public boolean isRegentViewHimself() {
        return regentViewHimself;
    }

    public void setBackToResume(boolean backToResume) {
        this.backToResume = backToResume;
    }

    public boolean isBackToResume() {
        return backToResume;
    }
}