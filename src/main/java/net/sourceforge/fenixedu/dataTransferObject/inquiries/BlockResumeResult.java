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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.lang.StringUtils;

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
            for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
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
            for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
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
        Collection<InquiryBlock> associatedBlocks = inquiryResult.getInquiryQuestion().getAssociatedBlocks();
        if (!inquiryResult.getInquiryQuestion().getAssociatedResultBlocks().isEmpty()) {
            associatedBlocks = new ArrayList<InquiryBlock>();
            for (InquiryBlock inquiryBlock : inquiryResult.getInquiryQuestion().getAssociatedResultBlocks()) {
                for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                    for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
                        associatedBlocks.addAll(inquiryQuestion.getAssociatedBlocks());
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
        return BundleUtil.getString(Bundle.INQUIRIES, getFirstHeaderKey());
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