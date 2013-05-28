/*
 * Created on 4/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionImage {

    @Service
    public static String run(Integer exerciseId, Integer metadataCode, Integer imageId, Integer feedbackId, Integer itemIndex,
            String path) throws FenixServiceException {
        Question question = AbstractDomainObject.fromExternalId(exerciseId);
        if (question != null) {
            if (question.getSubQuestions() == null || question.getSubQuestions().size() == 0) {
                ParseSubQuestion parse = new ParseSubQuestion();
                try {
                    question = parse.parseSubQuestion(question, path);
                } catch (ParseQuestionException e) {
                    throw new FenixServiceException();
                }
            }
            if (question.getSubQuestions().size() < itemIndex) {
                return null;
            }
            return question.getSubQuestions().get(itemIndex).getImage(imageId, feedbackId);
        }
        return null;
    }

    @Service
    public static String run(Integer distributedTestId, Integer questionId, String optionShuffle, Integer imageId,
            Integer feedbackId, String path) throws FenixServiceException {

        Question question = null;
        Test test = AbstractDomainObject.fromExternalId(distributedTestId);
        for (TestQuestion testQuestion : test.getTestQuestions()) {
            if (testQuestion.getQuestion().getExternalId().equals(questionId)) {
                question = testQuestion.getQuestion();
                break;
            }
        }
        if (question == null) {
            throw new FenixServiceException("Unexisting Question!!!!!");
        }

        InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
        try {
            ParseSubQuestion parse = new ParseSubQuestion();
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
            infoStudentTestQuestion.setOptionShuffle(optionShuffle);
            // infoStudentTestQuestion =
            // parse.parseStudentTestQuestion(infoStudentTestQuestion,
            // path.replace('\\', '/'));
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        Iterator questionit = infoStudentTestQuestion.getQuestion().getQuestion().iterator();
        int imgIndex = 0;
        while (questionit.hasNext()) {
            LabelValueBean lvb = (LabelValueBean) questionit.next();
            if (lvb.getLabel().startsWith("image/")) {
                imgIndex++;
                if (imgIndex == imageId.intValue()) {
                    return lvb.getValue();
                }
            }
        }
        Iterator optionit = infoStudentTestQuestion.getQuestion().getOptions().iterator();
        while (optionit.hasNext()) {
            List optionContent = ((QuestionOption) optionit.next()).getOptionContent();
            for (int i = 0; i < optionContent.size(); i++) {
                LabelValueBean lvb = (LabelValueBean) optionContent.get(i);
                if (lvb.getLabel().startsWith("image/")) {
                    imgIndex++;
                    if (imgIndex == imageId.intValue()) {
                        return lvb.getValue();
                    }
                }
            }
        }

        // if (feedbackId != null) {
        // Iterator feedbackit = ((ResponseProcessing)
        // infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions
        // ().get(
        // new Integer(feedbackId).intValue())).getFeedback().iterator();
        //
        // while (feedbackit.hasNext()) {
        // LabelValueBean lvb = (LabelValueBean) feedbackit.next();
        // if (lvb.getLabel().startsWith("image/")) {
        // imgIndex++;
        // if (imgIndex == imageId.intValue())
        // return lvb.getValue();
        // }
        // }
        // }

        return null;
    }
}