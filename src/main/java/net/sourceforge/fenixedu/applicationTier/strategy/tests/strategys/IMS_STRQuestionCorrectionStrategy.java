/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

/**
 * @author Susana Fernandes
 * 
 */
public class IMS_STRQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    @Override
    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
        if ((studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR)
                || (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID && (studentTestQuestion
                        .getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE))) {
            List<ResponseProcessing> questionCorrectionList =
                    studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions();
            for (ResponseProcessing responseProcessing : questionCorrectionList) {
                if (isCorrectSTR(responseProcessing.getResponseConditions(),
                        new String(((ResponseSTR) studentTestQuestion.getResponse()).getResponse()))) {
                    return setStudentTestQuestionResponse(studentTestQuestion, responseProcessing);
                }
            }
            ResponseProcessing responseProcessing =
                    getOtherResponseProcessing(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions());
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
        ResponseSTR r = (ResponseSTR) studentTestQuestion.getResponse();
        r.setResponseProcessingIndex(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions()
                .indexOf(responseProcessing));
        studentTestQuestion.setResponse(r);
        studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
        return studentTestQuestion;
    }
}