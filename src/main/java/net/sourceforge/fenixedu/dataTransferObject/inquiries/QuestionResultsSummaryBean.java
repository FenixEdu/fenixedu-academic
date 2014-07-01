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

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.YearDelegate;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class QuestionResultsSummaryBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private InquiryQuestion inquiryQuestion;
    private InquiryResult numberOfResponses;
    private InquiryResult median;
    private InquiryResult questionResult;
    private ResultClassification resultClassification;
    private List<InquiryResult> scaleValues;
    private List<InquiryResult> absoluteScaleValues;
    private List<InquiryResultComment> resultComments;
    private String editableComment;
    private Person commentPerson;
    private ResultPersonCategory personCategory;

    public QuestionResultsSummaryBean(InquiryQuestion inquiryQuestion, List<InquiryResult> questionResults, Person person,
            ResultPersonCategory personCategory) {
        setInquiryQuestion(inquiryQuestion);
        initNumberOfResponses(questionResults);
        initMedian(questionResults);
        initScaleValues(questionResults);
        initAbsoluteScaleValues(questionResults);
        initResultClassification(questionResults);
        initResultComments(person, personCategory);
    }

    public QuestionResultsSummaryBean(InquiryQuestion inquiryQuestion, InquiryResult inquiryResult) {
        setInquiryQuestion(inquiryQuestion);
        if (inquiryResult != null) {
            setResultClassification(inquiryResult.getResultClassification());
            setQuestionResult(inquiryResult);
        } else {
            setResultClassification(ResultClassification.GREY);
        }
    }

    private void initResultComments(Person person, ResultPersonCategory personCategory) {
        setCommentPerson(person);
        setPersonCategory(personCategory);
        if (person != null && getQuestionResult() != null) {
            setResultComments(new ArrayList<InquiryResultComment>());
            getResultComments().addAll(getQuestionResult().getCommentsWithLowerPermissions(personCategory));
            InquiryResultComment inquiryResultComment = getQuestionResult().getInquiryResultComment(person, personCategory);
            if (inquiryResultComment != null) {
                setEditableComment(inquiryResultComment.getComment());
            }
            Collections.sort(getResultComments(), new BeanComparator("personCategory"));
        }
    }

    private void initResultClassification(List<InquiryResult> questionResults) {
        for (InquiryResult inquiryResult : questionResults) {
            if (inquiryResult.getResultClassification() != null) {
                setResultClassification(inquiryResult.getResultClassification());
                setQuestionResult(inquiryResult);
                break;
            }
        }
    }

    private void initMedian(List<InquiryResult> questionResults) {
        for (InquiryResult inquiryResult : questionResults) {
            if (InquiryResultType.MEDIAN.equals(inquiryResult.getResultType())) {
                setMedian(inquiryResult);
                break;
            }
        }
    }

    private void initNumberOfResponses(List<InquiryResult> questionResults) {
        for (InquiryResult inquiryResult : questionResults) {
            if (InquiryResultType.ABSOLUTE.equals(inquiryResult.getResultType())
                    && StringUtils.isEmpty(inquiryResult.getScaleValue())) {
                setNumberOfResponses(inquiryResult);
                break;
            }
        }
    }

    private void initAbsoluteScaleValues(List<InquiryResult> questionResults) {
        setAbsoluteScaleValues(new ArrayList<InquiryResult>());
        for (InquiryResult inquiryResult : questionResults) {
            if (InquiryResultType.ABSOLUTE.equals(inquiryResult.getResultType())
                    && !StringUtils.isEmpty(inquiryResult.getScaleValue())) {
                getAbsoluteScaleValues().add(inquiryResult);
            }
        }
        Collections.sort(getAbsoluteScaleValues(), InquiryResult.INQUIRY_RESULT_SCALE_VALUES_COMPARATOR);
    }

    private void initScaleValues(List<InquiryResult> questionResults) {
        setScaleValues(new ArrayList<InquiryResult>());
        for (InquiryResult inquiryResult : questionResults) {
            if (InquiryResultType.PERCENTAGE.equals(inquiryResult.getResultType())
                    && !StringUtils.isEmpty(inquiryResult.getScaleValue())) {
                getScaleValues().add(inquiryResult);
            }
        }
        Collections.sort(getScaleValues(), InquiryResult.INQUIRY_RESULT_SCALE_VALUES_COMPARATOR);
    }

    public String getPresentationValue() {
        if (getQuestionResult() != null) {
            if (InquiryResultType.PERCENTAGE.equals(getQuestionResult().getResultType())) {
                Double value = Double.valueOf(getQuestionResult().getValue()) * 100;
                int roundedValue = (int) StrictMath.round(value);
                return String.valueOf(roundedValue);
            }
            return getQuestionResult().getValue();
        }
        return "";
    }

    public static String getMadeCommentHeader(InquiryResultComment inquiryResultComment) {
        switch (inquiryResultComment.getPersonCategory()) {
        case DELEGATE:
            YearDelegate yearDelegate = getYearDelegate(inquiryResultComment);
            return BundleUtil.getString(Bundle.INQUIRIES, "label.commentHeader.delegate",
                    new String[] { yearDelegate.getCurricularYear().getYear().toString(),
                            yearDelegate.getRegistration().getDegree().getSigla() });
        case TEACHER:
            String teacherHeaderLabel = "label.commentHeader.teacher";
            if (inquiryResultComment.getInquiryResult().getProfessorship().getResponsibleFor()) {
                teacherHeaderLabel = "label.commentHeader.teacherResponsible";
            }
            return BundleUtil.getString(Bundle.INQUIRIES, teacherHeaderLabel);
        case REGENT:
            return BundleUtil.getString(Bundle.INQUIRIES, "label.commentHeader.responsible",
                    new String[] { inquiryResultComment.getPerson().getName() });
        default:
            break;
        }
        return null;
    }

    private static YearDelegate getYearDelegate(InquiryResultComment inquiryResultComment) {
        YearDelegate yearDelegate = null;
        ExecutionSemester executionPeriod = inquiryResultComment.getInquiryResult().getExecutionPeriod();
        if (executionPeriod == null) {
            executionPeriod =
                    inquiryResultComment.getInquiryResult().getProfessorship().getExecutionCourse().getExecutionPeriod();
        }
        for (Delegate delegate : inquiryResultComment.getPerson().getStudent().getDelegates()) {
            if (delegate instanceof YearDelegate) {
                if (delegate.isActiveForFirstExecutionYear(executionPeriod.getExecutionYear())) {
                    if (yearDelegate == null
                            || delegate.getDelegateFunction().getEndDate()
                                    .isAfter(yearDelegate.getDelegateFunction().getEndDate())) {
                        yearDelegate = (YearDelegate) delegate;
                    }
                }
            }
        }
        return yearDelegate;
    }

    public InquiryQuestion getInquiryQuestion() {
        return inquiryQuestion;
    }

    private void setInquiryQuestion(InquiryQuestion inquiryQuestion) {
        this.inquiryQuestion = inquiryQuestion;
    }

    public InquiryResult getNumberOfResponses() {
        return numberOfResponses;
    }

    private void setNumberOfResponses(InquiryResult numberOfResponses) {
        this.numberOfResponses = numberOfResponses;
    }

    public InquiryResult getMedian() {
        return median;
    }

    private void setMedian(InquiryResult median) {
        this.median = median;
    }

    public List<InquiryResult> getScaleValues() {
        return scaleValues;
    }

    private void setScaleValues(List<InquiryResult> scaleValues) {
        this.scaleValues = scaleValues;
    }

    public List<InquiryResult> getAbsoluteScaleValues() {
        return absoluteScaleValues;
    }

    private void setAbsoluteScaleValues(List<InquiryResult> absoluteScaleValues) {
        this.absoluteScaleValues = absoluteScaleValues;
    }

    public void setResultClassification(ResultClassification resultClassification) {
        this.resultClassification = resultClassification;
    }

    public ResultClassification getResultClassification() {
        return resultClassification;
    }

    public void setQuestionResult(InquiryResult questionResult) {
        this.questionResult = questionResult;
    }

    public InquiryResult getQuestionResult() {
        return questionResult;
    }

    public List<InquiryResultComment> getResultComments() {
        return resultComments;
    }

    public void setResultComments(List<InquiryResultComment> resultComments) {
        this.resultComments = resultComments;
    }

    public String getEditableComment() {
        return editableComment;
    }

    public void setEditableComment(String editableComment) {
        this.editableComment = editableComment;
    }

    public void setCommentPerson(Person commentPerson) {
        this.commentPerson = commentPerson;
    }

    public Person getCommentPerson() {
        return commentPerson;
    }

    public void setPersonCategory(ResultPersonCategory personCategory) {
        this.personCategory = personCategory;
    }

    public ResultPersonCategory getPersonCategory() {
        return personCategory;
    }
}
