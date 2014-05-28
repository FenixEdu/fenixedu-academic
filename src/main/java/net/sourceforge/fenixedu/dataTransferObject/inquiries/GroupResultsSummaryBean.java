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
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class GroupResultsSummaryBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private InquiryGroupQuestion inquiryGroupQuestion;
    private ResultClassification groupResultClassification;
    private InquiryResult groupTotalResult;
    private List<QuestionResultsSummaryBean> questionsResults = new ArrayList<QuestionResultsSummaryBean>();
    private boolean left;

    public GroupResultsSummaryBean(InquiryGroupQuestion inquiryGroupQuestion, List<InquiryResult> inquiryResults, Person person,
            ResultPersonCategory personCategory) {
        setInquiryGroupQuestion(inquiryGroupQuestion);
        setLeft(true);
        for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
            List<InquiryResult> questionResults = getResultsForQuestion(inquiryResults, inquiryQuestion);
            QuestionResultsSummaryBean resultsSummaryBean = null;
            if (inquiryQuestion.isScaleQuestion()) {
                resultsSummaryBean = new QuestionResultsSummaryBean(inquiryQuestion, questionResults, person, personCategory);
            } else {
                InquiryResult inquiryResult = null;
                if (questionResults.size() > 0) {
                    inquiryResult = questionResults.iterator().next();
                }
                resultsSummaryBean = new QuestionResultsSummaryBean(inquiryQuestion, inquiryResult);
            }
            getQuestionsResults().add(resultsSummaryBean);
        }
        initGroupResultClassification(inquiryResults);
        Collections.sort(getQuestionsResults(), new BeanComparator("inquiryQuestion.questionOrder"));
    }

    private void initGroupResultClassification(List<InquiryResult> inquiryResults) {
        for (InquiryResult inquiryResult : inquiryResults) {
            if (inquiryResult.getInquiryQuestion() != null
                    && inquiryResult.getInquiryQuestion().isResultQuestion(inquiryResult.getExecutionPeriod())
                    && inquiryResult.getInquiryQuestion().getCheckboxGroupQuestion() != null
                    && inquiryResult.getInquiryQuestion().getCheckboxGroupQuestion() == getInquiryGroupQuestion()) {
                setGroupTotalResult(inquiryResult);
                break;
            }
        }
    }

    private List<InquiryResult> getResultsForQuestion(List<InquiryResult> results, InquiryQuestion inquiryQuestion) {
        List<InquiryResult> questionResults = new ArrayList<InquiryResult>();
        for (InquiryResult inquiryResult : results) {
            if (inquiryResult.getInquiryQuestion() == inquiryQuestion) {
                questionResults.add(inquiryResult);
            }
        }
        return questionResults;
    }

    public int getAbsoluteScaleValuesSize() {
        if (getInquiryGroupQuestion().isScaleGroup()) {
            for (QuestionResultsSummaryBean questionResultsSummaryBean : getQuestionsResults()) {
                if (questionResultsSummaryBean.getAbsoluteScaleValues().size() > 0) {
                    return questionResultsSummaryBean.getAbsoluteScaleValues().size();
                }
            }
        }
        return 0;
    }

    public boolean hasClassification() {
        for (QuestionResultsSummaryBean questionResultsSummaryBean : getQuestionsResults()) {
            if (questionResultsSummaryBean.getInquiryQuestion().getHasClassification()) {
                return true;
            }
        }
        return false;
    }

    public QuestionResultsSummaryBean getValidQuestionResult() {
        for (QuestionResultsSummaryBean questionResultsSummaryBean : getQuestionsResults()) {
            if (!ResultClassification.GREY.equals(questionResultsSummaryBean.getResultClassification() != null)) {
                return questionResultsSummaryBean;
            }
        }
        return null;
    }

    public InquiryGroupQuestion getInquiryGroupQuestion() {
        return inquiryGroupQuestion;
    }

    public void setInquiryGroupQuestion(InquiryGroupQuestion inquiryGroupQuestion) {
        this.inquiryGroupQuestion = inquiryGroupQuestion;
    }

    public List<QuestionResultsSummaryBean> getQuestionsResults() {
        return questionsResults;
    }

    public void setQuestionsResults(List<QuestionResultsSummaryBean> questionsResults) {
        this.questionsResults = questionsResults;
    }

    public void setGroupResultClassification(ResultClassification groupResultClassification) {
        this.groupResultClassification = groupResultClassification;
    }

    public ResultClassification getGroupResultClassification() {
        return groupResultClassification;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isLeft() {
        return left;
    }

    public void setGroupTotalResult(InquiryResult groupTotalResult) {
        this.groupTotalResult = groupTotalResult;
    }

    public InquiryResult getGroupTotalResult() {
        return groupTotalResult;
    }
}
