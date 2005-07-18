/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
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

    public void run(Integer executionCourseId, Integer testId) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
        IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();
        ITest test = (ITest) persistentTest.readByOID(Test.class, testId);
        List<ITestQuestion> testQuestionList = test.getTestQuestions();
        for (ITestQuestion testQuestion : testQuestionList) {
            testQuestion.removeQuestion();
            testQuestion.removeTest();
            persistentTestQuestion.deleteByOID(TestQuestion.class, testQuestion.getIdInternal());
        }
        test.removeTestScope();
        persistentTest.deleteByOID(Test.class, testId);
    }

}