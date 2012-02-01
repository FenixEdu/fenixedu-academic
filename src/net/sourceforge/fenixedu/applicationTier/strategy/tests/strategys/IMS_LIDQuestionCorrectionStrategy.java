/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 * 
 */
public class IMS_LIDQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
	if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
	    if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.MULTIPLE) {
		ResponseProcessing responseProcessing = getLIDResponseProcessing(studentTestQuestion.getSubQuestionByItem()
			.getResponseProcessingInstructions(), ((ResponseLID) studentTestQuestion.getResponse()).getResponse());
		if (responseProcessing != null) {
		    return setStudentTestQuestionResponse(studentTestQuestion, responseProcessing);
		}
	    } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
		ResponseProcessing responseProcessing = getLIDResponseProcessing(studentTestQuestion.getSubQuestionByItem()
			.getResponseProcessingInstructions(), ((ResponseLID) studentTestQuestion.getResponse()).getResponse()[0]);
		if (responseProcessing != null) {
		    return setStudentTestQuestionResponse(studentTestQuestion, responseProcessing);
		}
	    }
	    ResponseProcessing responseProcessing = getOtherResponseProcessing(studentTestQuestion.getSubQuestionByItem()
		    .getResponseProcessingInstructions());
	    if (responseProcessing != null) {
		return setStudentTestQuestionResponse(studentTestQuestion, responseProcessing);
	    }
	}
	studentTestQuestion.setTestQuestionMark(new Double(0));
	return studentTestQuestion;
    }

    private StudentTestQuestion setStudentTestQuestionResponse(StudentTestQuestion studentTestQuestion,
	    ResponseProcessing responseProcessing) {
	studentTestQuestion.setTestQuestionMark(responseProcessing.getResponseValue());
	ResponseLID r = (ResponseLID) studentTestQuestion.getResponse();
	r.setResponseProcessingIndex(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions()
		.indexOf(responseProcessing));
	studentTestQuestion.setResponse(r);
	studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
	return studentTestQuestion;
    }
}