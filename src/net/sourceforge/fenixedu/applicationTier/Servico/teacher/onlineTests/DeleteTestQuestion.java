/*
 * Created on 5/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteTestQuestion implements IService {

    public void run(Integer executionCourseId, Integer testId, final Integer questionId) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Test test = (Test) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
        if (test == null)
            throw new InvalidArgumentsServiceException();
        TestQuestion testQuestion = (TestQuestion) CollectionUtils.find(test.getTestQuestions(), new Predicate() {

            public boolean evaluate(Object arg0) {
                final TestQuestion testQuestion = (TestQuestion) arg0;
                return testQuestion.getQuestion().getIdInternal().equals(questionId);
            }

        });
        if (testQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }

        test.deleteTestQuestion(testQuestion);
    }
}