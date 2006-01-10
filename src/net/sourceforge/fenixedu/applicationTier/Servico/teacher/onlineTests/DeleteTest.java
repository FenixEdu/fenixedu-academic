/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteTest implements IService {

    public void run(final Integer executionCourseId, final Integer testId) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
        final IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();

        Test test = (Test) persistentTest.readByOID(Test.class, testId);
        List<TestQuestion> testQuestionList = test.getTestQuestions();
        for (; !testQuestionList.isEmpty(); deleteTestQuestion(persistentTestQuestion, testQuestionList.get(0)))
            ;
        test.removeTestScope();
        persistentTest.deleteByOID(Test.class, testId);
    }

    private void deleteTestQuestion(final IPersistentTestQuestion persistentTestQuestion, final TestQuestion testQuestion)
            throws ExcepcaoPersistencia {
        testQuestion.removeQuestion();
        testQuestion.removeTest();
        persistentTestQuestion.deleteByOID(TestQuestion.class, testQuestion.getIdInternal());
    }

}