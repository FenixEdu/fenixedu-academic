/*
 * Created on 3/Set/2003
 */
package ServidorAplicacao.Servico.student;

import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.InfoStudentTestQuestionWithInfoQuestion;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.QuestionOption;
import Util.tests.ResponseProcessing;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestQuestionImage implements IService {

    private String path = new String();

    public ReadStudentTestQuestionImage() {
    }

    public String run(String userName, Integer distributedTestId, Integer questionId, Integer imageId,
            String feedbackId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
            if (student == null)
                throw new FenixServiceException();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null)
                throw new FenixServiceException();

            List studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readByStudentAndDistributedTest(student, distributedTest);

            Iterator it = studentTestQuestionList.iterator();
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                if (studentTestQuestion.getKeyQuestion().equals(questionId)) {
                    ParseQuestion parse = new ParseQuestion();
                    InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestion
                            .newInfoFromDomain(studentTestQuestion);
                    try {
                        infoStudentTestQuestion = parse.parseStudentTestQuestion(
                                infoStudentTestQuestion, this.path);
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

                    if (feedbackId != null) {
                        Iterator feedbackit = ((ResponseProcessing) infoStudentTestQuestion
                                .getQuestion().getResponseProcessingInstructions().get(
                                        new Integer(feedbackId).intValue())).getFeedback().iterator();

                        while (feedbackit.hasNext()) {
                            LabelValueBean lvb = (LabelValueBean) feedbackit.next();
                            if (lvb.getLabel().startsWith("image/")) {
                                imgIndex++;
                                if (imgIndex == imageId.intValue())
                                    return lvb.getValue();
                            }
                        }
                    }
                }

            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return null;
    }

}