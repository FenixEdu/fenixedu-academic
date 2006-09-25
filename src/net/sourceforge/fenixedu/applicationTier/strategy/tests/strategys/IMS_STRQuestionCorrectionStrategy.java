/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import java.util.Iterator;
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

    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
        if ((studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR)
                || (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID && (studentTestQuestion
                        .getSubQuestionByItem().getQuestionType().getCardinalityType().getType()
                        .intValue() == CardinalityType.SINGLE))) {
            List questionCorrectionList = studentTestQuestion.getSubQuestionByItem()
                    .getResponseProcessingInstructions();
            Iterator questionCorrectionIt = questionCorrectionList.iterator();
            for (int i = 0; questionCorrectionIt.hasNext(); i++) {
                ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt.next();
                if (isCorrectSTR(responseProcessing.getResponseConditions(), new String(
                        ((ResponseSTR) studentTestQuestion.getResponse()).getResponse()))) {
                    studentTestQuestion.setTestQuestionMark(responseProcessing.getResponseValue());
                    ResponseSTR r = (ResponseSTR) studentTestQuestion.getResponse();
                    r.setResponseProcessingIndex(new Integer(i));
                    studentTestQuestion.setResponse(r);
                    studentTestQuestion.getSubQuestionByItem().setNextItemId(
                            responseProcessing.getNextItem());
                    return studentTestQuestion;
                }
            }
        }
        studentTestQuestion.setTestQuestionMark(new Double(0));
        return studentTestQuestion;
    }
}