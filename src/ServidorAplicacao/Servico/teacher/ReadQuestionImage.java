/*
 * Created on 4/Ago/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoQuestion;
import DataBeans.InfoStudentTestQuestion;
import Dominio.IQuestion;
import Dominio.Question;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.QuestionOption;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionImage implements IService {

    private String path = new String();

    public ReadQuestionImage() {
    }

    public String run(Integer exerciseId, Integer imageId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        ISuportePersistente persistentSuport;
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();

            IQuestion question = new Question(exerciseId);

            question = (IQuestion) persistentQuestion.readByOID(Question.class, exerciseId);
            ParseQuestion parse = new ParseQuestion();
            String image;
            try {
                image = parse.parseQuestionImage(question.getXmlFile(), imageId.intValue(), this.path);
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            return image;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    public String run(Integer distributedTestId, Integer questionId, String optionShuffle,
            Integer imageId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IQuestion question = (IQuestion) persistentSuport.getIPersistentQuestion().readByOID(
                    Question.class, questionId);

            ParseQuestion parse = new ParseQuestion();
            InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();

            try {

                infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
                infoStudentTestQuestion.setOptionShuffle(optionShuffle);
                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion,
                        this.path);

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

            //            if (feedbackId != null) {
            //                Iterator feedbackit = ((ResponseProcessing)
            // infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions().get(
            //                        new Integer(feedbackId).intValue())).getFeedback().iterator();
            //
            //                while (feedbackit.hasNext()) {
            //                    LabelValueBean lvb = (LabelValueBean) feedbackit.next();
            //                    if (lvb.getLabel().startsWith("image/")) {
            //                        imgIndex++;
            //                        if (imgIndex == imageId.intValue())
            //                            return lvb.getValue();
            //                    }
            //                }
            //            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return null;
    }
}