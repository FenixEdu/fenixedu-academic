/*
 * Created on 23/Set/2004
 *
 */
package ServidorAplicacao.strategy.tests.strategys;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import Util.tests.QuestionType;
import Util.tests.ResponseNUM;
import Util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 *  
 */
public class IMS_NUMQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    public InfoStudentTestQuestion getMark(InfoStudentTestQuestion infoStudentTestQuestion) {
        if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM) {
            List questionCorrectionList = infoStudentTestQuestion.getQuestion()
                    .getResponseProcessingInstructions();
            Iterator questionCorrectionIt = questionCorrectionList.iterator();
            for (int i = 0; questionCorrectionIt.hasNext(); i++) {
                ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt.next();
                if (isCorrectNUM(responseProcessing.getResponseConditions(), new Double(
                        ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse()))) {
                    infoStudentTestQuestion.setTestQuestionMark(responseProcessing.getResponseValue());
                    ResponseNUM r = (ResponseNUM) infoStudentTestQuestion.getResponse();
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