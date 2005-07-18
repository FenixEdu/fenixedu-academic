/*
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestion;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
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

    public String run(String userName, Integer distributedTestId, Integer questionId, Integer imageId, String feedbackId, String path)
            throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
            if (student == null)
                throw new FenixServiceException();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null)
                throw new FenixServiceException();

            List<IStudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readByStudentAndDistributedTest(student.getIdInternal(), distributedTest.getIdInternal());

            for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                if (studentTestQuestion.getKeyQuestion().equals(questionId)) {
                    ParseQuestion parse = new ParseQuestion();
                    InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestion.newInfoFromDomain(studentTestQuestion);
                    try {
                        infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }
                    int imgIndex = 0;
                    for (LabelValueBean lvb : (List<LabelValueBean>) infoStudentTestQuestion.getQuestion().getQuestion()) {
                        if (lvb.getLabel().startsWith("image/")) {
                            imgIndex++;
                            if (imgIndex == imageId.intValue())
                                return lvb.getValue();
                        }
                    }
                    Iterator optionit = infoStudentTestQuestion.getQuestion().getOptions().iterator();

                    while (optionit.hasNext()) {
                        List<LabelValueBean> optionContent = ((QuestionOption) optionit.next()).getOptionContent();
                        for (LabelValueBean lvb : optionContent) {
                            if (lvb.getLabel().startsWith("image/")) {
                                imgIndex++;
                                if (imgIndex == imageId.intValue())
                                    return lvb.getValue();
                            }
                        }
                    }

                    if (feedbackId != null) {
                        for (LabelValueBean lvb : (List<LabelValueBean>) ((ResponseProcessing) infoStudentTestQuestion.getQuestion()
                                .getResponseProcessingInstructions().get(new Integer(feedbackId).intValue())).getFeedback()) {
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