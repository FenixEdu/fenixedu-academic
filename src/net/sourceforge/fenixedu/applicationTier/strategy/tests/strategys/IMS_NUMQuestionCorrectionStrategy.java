/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 * 
 */
public class IMS_NUMQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
            List questionCorrectionList = studentTestQuestion.getSubQuestionByItem()
                    .getResponseProcessingInstructions();
            Iterator questionCorrectionIt = questionCorrectionList.iterator();
            for (int i = 0; questionCorrectionIt.hasNext(); i++) {
                ResponseProcessing responseProcessing = (ResponseProcessing) questionCorrectionIt.next();
                if (isCorrectNUM(responseProcessing.getResponseConditions(), new Double(
                        ((ResponseNUM) studentTestQuestion.getResponse()).getResponse()))) {
                    studentTestQuestion.setTestQuestionMark(responseProcessing.getResponseValue());
                    ResponseNUM r = (ResponseNUM) studentTestQuestion.getResponse();
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