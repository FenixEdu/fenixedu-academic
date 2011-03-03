package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;

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

    public QuestionResultsSummaryBean(InquiryQuestion inquiryQuestion, List<InquiryResult> questionResults) {
	setInquiryQuestion(inquiryQuestion);
	initNumberOfResponses(questionResults);
	initMedian(questionResults);
	initScaleValues(questionResults);
	initAbsoluteScaleValues(questionResults);
	initResultClassification(questionResults);
	initResultComments();
    }

    public QuestionResultsSummaryBean(InquiryQuestion inquiryQuestion, InquiryResult inquiryResult) {
	setInquiryQuestion(inquiryQuestion);
	if (inquiryResult != null) {
	    setResultClassification(inquiryResult.getResultClassification());
	    setQuestionResult(inquiryResult);
	} else {
	    setResultClassification(ResultClassification.GREY);
	}
	//initResultComments(); TODO
    }

    private void initResultComments() {
	if (getQuestionResult() != null) {
	    setResultComments(new ArrayList<InquiryResultComment>());
	    getResultComments().addAll(getQuestionResult().getInquiryResultComments());
	    if (!getQuestionResult().getInquiryResultComments().isEmpty()) { //TODO ir buscar por pessoa!!
		InquiryResultComment resultComment = getQuestionResult().getInquiryResultComments().get(0);
		setEditableComment(resultComment.getComment());
	    }
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
	Collections.sort(getAbsoluteScaleValues(), new BeanComparator("scaleValue"));
    }

    private void initScaleValues(List<InquiryResult> questionResults) {
	setScaleValues(new ArrayList<InquiryResult>());
	for (InquiryResult inquiryResult : questionResults) {
	    if (InquiryResultType.PERCENTAGE.equals(inquiryResult.getResultType())
		    && !StringUtils.isEmpty(inquiryResult.getScaleValue())) {
		getScaleValues().add(inquiryResult);
	    }
	}
	Collections.sort(getScaleValues(), new BeanComparator("scaleValue"));
    }

    public String getPresentationValue() {
	if (getQuestionResult() != null) {
	    if (InquiryResultType.PERCENTAGE.equals(getQuestionResult().getResultType())) {
		Double value = Double.valueOf(getQuestionResult().getValue().replace(",", ".")) * 100;
		return value.toString();
	    }
	    return getQuestionResult().getValue();
	}
	return "";
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
}
