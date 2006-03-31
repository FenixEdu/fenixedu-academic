/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class DeleteTest extends Service {

    public void run(final Integer executionCourseId, final Integer testId) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        Test test = (Test) persistentObject.readByOID(Test.class, testId);
        List<TestQuestion> testQuestionList = test.getTestQuestions();
        for (; !testQuestionList.isEmpty(); deleteTestQuestion(testQuestionList.get(0)))
            ;
        test.removeTestScope();
        persistentObject.deleteByOID(Test.class, testId);
    }

    private void deleteTestQuestion(final TestQuestion testQuestion)
            throws ExcepcaoPersistencia {
        testQuestion.removeQuestion();
        testQuestion.removeTest();
        persistentObject.deleteByOID(TestQuestion.class, testQuestion.getIdInternal());
    }

}