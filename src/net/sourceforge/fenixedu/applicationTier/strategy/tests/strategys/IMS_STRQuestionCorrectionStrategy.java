/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

/**
 * @author Susana Fernandes
 *  
 */
public class IMS_STRQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    public InfoStudentTestQuestion getMark(InfoStudentTestQuestion infoStudentTestQuestion) {
        if ((infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR)
                || (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID && (infoStudentTestQuestion
                        .getQuestion().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE))) {
            List questionCorrectionList = infoStudentTestQuestion.getQuestion()
                    .getResponseProcessingInstructions();
            Iterator questionCorrectionIt = questionCorrectionList.iterator();
            for (int i = 0; questionCorrectionIt.hasNext(); i++) {
                ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt.next();
                if (isCorrectSTR(responseProcessing.getResponseConditions(), new String(
                        ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse()))) {
                    infoStudentTestQuestion.setTestQuestionMark(responseProcessing.getResponseValue());
                    ResponseSTR r = (ResponseSTR) infoStudentTestQuestion.getResponse();
                    r.setResponseProcessingIndex(new Integer(i));
                    infoStudentTestQuestion.setResponse(r);

                    return infoStudentTestQuestion;
                }
            }
        }
        infoStudentTestQuestion.setTestQuestionMark(new Double(0));
        return infoStudentTestQuestion;
    }
}