/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteExercise implements IService {

    public void run(Integer executionCourseId, Integer metadataId) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
        IMetadata metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, metadataId, true);
        if (metadata == null)
            throw new InvalidArgumentsServiceException();
        List<IQuestion> questionList = persistentSuport.getIPersistentQuestion().readByMetadata(metadata);
        boolean delete = true;
        IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();
        for (IQuestion question : questionList) {
            List<ITestQuestion> testQuestionList = persistentSuport.getIPersistentTestQuestion().readByQuestion(question.getIdInternal());
            for (ITestQuestion testQuestion : testQuestionList) {
                removeTestQuestionFromTest(persistentSuport, testQuestion);
            }
            if (persistentStudentTestQuestion.countByQuestion(question.getIdInternal()) == 0) {
                persistentQuestion.deleteByOID(Question.class, question.getIdInternal());
            } else {
                persistentQuestion.lockWrite(question);
                question.setVisibility(new Boolean("false"));
                delete = false;
            }
        }
        if (delete) {
            persistentQuestion.deleteByMetadata(metadata);
            persistentMetadata.deleteByOID(Metadata.class, metadata.getIdInternal());
        } else {
            persistentMetadata.simpleLockWrite(metadata);
            metadata.setVisibility(new Boolean("false"));
        }
    }

    private void removeTestQuestionFromTest(ISuportePersistente persistentSuport, ITestQuestion testQuestion) throws ExcepcaoPersistencia {
        IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();

        ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testQuestion.getKeyTest(), true);
        if (test == null)
            throw new ExcepcaoPersistencia();
        List<ITestQuestion> testQuestionList = persistentTestQuestion.readByTest(test.getIdInternal());
        Integer questionOrder = testQuestion.getTestQuestionOrder();
        if (testQuestionList != null) {
            for (ITestQuestion iterTestQuestion : testQuestionList) {
                Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
                if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                    persistentTestQuestion.simpleLockWrite(iterTestQuestion);
                    iterTestQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() - 1));
                }
            }
        }
        persistentTestQuestion.deleteByOID(TestQuestion.class, testQuestion.getIdInternal());
        test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() - 1));
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }
}