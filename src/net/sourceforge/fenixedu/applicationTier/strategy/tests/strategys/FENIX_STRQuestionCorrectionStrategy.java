/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

/**
 * @author Susana Fernandes
 *  
 */
public class FENIX_STRQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    public InfoStudentTestQuestion getMark(InfoStudentTestQuestion infoStudentTestQuestion) {
        Integer fenixCorrectResponseIndex = getFenixCorrectResponseIndex(infoStudentTestQuestion
                .getQuestion().getResponseProcessingInstructions());
        if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR) {
            List correctResponseList = ((ResponseProcessing) infoStudentTestQuestion.getQuestion()
                    .getResponseProcessingInstructions().get(fenixCorrectResponseIndex.intValue()))
                    .getResponseConditions();

            if (isCorrectSTR(correctResponseList, new String(((ResponseSTR) infoStudentTestQuestion
                    .getResponse()).getResponse()))) {
                infoStudentTestQuestion.setTestQuestionMark(new Double(infoStudentTestQuestion
                        .getTestQuestionValue().doubleValue()));
                ResponseSTR r = (ResponseSTR) infoStudentTestQuestion.getResponse();
                r.setIsCorrect(new Boolean(true));
                infoStudentTestQuestion.setResponse(r);
            } else {
                infoStudentTestQuestion.setTestQuestionMark(new Double(0));
                ResponseSTR r = (ResponseSTR) infoStudentTestQuestion.getResponse();
                r.setIsCorrect(new Boolean(false));
                infoStudentTestQuestion.setResponse(r);
            }
            return infoStudentTestQuestion;
        }
        infoStudentTestQuestion.setTestQuestionMark(new Double(0));
        return infoStudentTestQuestion;
    }

}