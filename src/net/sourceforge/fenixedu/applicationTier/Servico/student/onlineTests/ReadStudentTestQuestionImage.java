/*
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestQuestionImage extends Service {

    public String run(String userName, Integer distributedTestId, Integer questionId, Integer imageId, String feedbackId, String path)
            throws FenixServiceException, ExcepcaoPersistencia {
        path = path.replace('\\', '/');
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Student student = persistentSuport.getIPersistentStudent().readByUsername(userName);
        if (student == null)
            throw new FenixServiceException();

        DistributedTest distributedTest = (DistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                distributedTestId);
        if (distributedTest == null)
            throw new FenixServiceException();

        List<StudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                student.getIdInternal(), distributedTest.getIdInternal());

        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyQuestion().equals(questionId)) {
                ParseQuestion parse = new ParseQuestion();
                InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
                        .newInfoFromDomain(studentTestQuestion);
                try {
                    infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, path);
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
        return null;
    }

}