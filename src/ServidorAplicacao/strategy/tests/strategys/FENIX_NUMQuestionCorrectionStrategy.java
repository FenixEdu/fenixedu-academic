/*
 * Created on 23/Set/2004
 *
 */
package ServidorAplicacao.strategy.tests.strategys;

import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import Util.tests.QuestionType;
import Util.tests.ResponseNUM;
import Util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 *  
 */
public class FENIX_NUMQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    public InfoStudentTestQuestion getMark(InfoStudentTestQuestion infoStudentTestQuestion) {
        Integer fenixCorrectResponseIndex = getFenixCorrectResponseIndex(infoStudentTestQuestion
                .getQuestion().getResponseProcessingInstructions());
        if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM) {
            List correctResponseList = ((ResponseProcessing) infoStudentTestQuestion.getQuestion()
                    .getResponseProcessingInstructions().get(fenixCorrectResponseIndex.intValue()))
                    .getResponseConditions();
            if (isCorrectNUM(correctResponseList, new Double(((ResponseNUM) infoStudentTestQuestion
                    .getResponse()).getResponse()))) {
                infoStudentTestQuestion.setTestQuestionMark(new Double(infoStudentTestQuestion
                        .getTestQuestionValue().doubleValue()));
                ResponseNUM r = (ResponseNUM) infoStudentTestQuestion.getResponse();
                r.setIsCorrect(new Boolean(true));
                infoStudentTestQuestion.setResponse(r);
            } else {
                infoStudentTestQuestion.setTestQuestionMark(new Double(0));
                ResponseNUM r = (ResponseNUM) infoStudentTestQuestion.getResponse();
                r.setIsCorrect(new Boolean(false));
                infoStudentTestQuestion.setResponse(r);
            }
            return infoStudentTestQuestion;
        }

        infoStudentTestQuestion.setTestQuestionMark(new Double(0));
        return infoStudentTestQuestion;
    }
}