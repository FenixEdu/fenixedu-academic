/*
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestionWithInfoQuestion;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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