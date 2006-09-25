/*
 * Created on 4/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.util.LabelValueBean;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionImage extends Service {

    public String run(Integer exerciseId, Integer metadataCode, Integer imageId, Integer itemIndex, String path) throws FenixServiceException,
            ExcepcaoPersistencia {

        Metadata metadata = rootDomainObject.readMetadataByOID(metadataCode);
        for (Question question : metadata.getVisibleQuestions()) {
            if (question.getIdInternal().equals(exerciseId)) {
                if (question.getSubQuestions() == null || question.getSubQuestions().size() == 0) {
                    ParseSubQuestion parse = new ParseSubQuestion();
                    try {
                        question = parse.parseSubQuestion(question, path);
                    } catch (ParseQuestionException e) {
                        throw new FenixServiceException();
                    } catch (ParseException e) {
                        throw new FenixServiceException();
                    }
                }
                if (question.getSubQuestions().size() < itemIndex) {
                    return null;
                }
                return question.getSubQuestions().get(itemIndex).getImage(imageId);
            }
        }
        return null;
    }

    public String run(Integer distributedTestId, Integer questionId, String optionShuffle, Integer imageId, String path)
            throws FenixServiceException, ExcepcaoPersistencia {

        Question question = null;
        Test test = rootDomainObject.readTestByOID(distributedTestId);
        for (TestQuestion testQuestion : test.getTestQuestions()) {
            if (testQuestion.getQuestion().getIdInternal().equals(questionId)) {
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
                if (imgIndex == imageId.intValue())
                    return lvb.getValue();
            }
        }
        Iterator optionit = infoStudentTestQuestion.getQuestion().getOptions().iterator();
        while (optionit.hasNext()) {
            List optionContent = ((QuestionOption) optionit.next()).getOptionContent();
            for (int i = 0; i < optionContent.size(); i++) {
                LabelValueBean lvb = (LabelValueBean) optionContent.get(i);
                if (lvb.getLabel().startsWith("image/")) {
                    imgIndex++;
                    if (imgIndex == imageId.intValue())
                        return lvb.getValue();
                }
            }
        }

        // if (feedbackId != null) {
        // Iterator feedbackit = ((ResponseProcessing)
        // infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions().get(
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