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
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionImage implements IService {

    private String path = new String();

    public String run(Integer exerciseId, Integer imageId, String path) throws FenixServiceException, ExcepcaoPersistencia {
        this.path = path.replace('\\', '/');
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Question question = (Question) persistentSuport.getIPersistentQuestion().readByOID(Question.class, exerciseId);
        ParseQuestion parse = new ParseQuestion();
        String image;
        try {
            image = parse.parseQuestionImage(question.getXmlFile(), imageId.intValue(), this.path);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return image;
    }

    public String run(Integer distributedTestId, Integer questionId, String optionShuffle, Integer imageId, String path)
            throws FenixServiceException, ExcepcaoPersistencia {
        this.path = path.replace('\\', '/');
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Question question = (Question) persistentSuport.getIPersistentQuestion().readByOID(Question.class, questionId);
        InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
        try {
            ParseQuestion parse = new ParseQuestion();
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
            infoStudentTestQuestion.setOptionShuffle(optionShuffle);
            infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);
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