/*
 * Created on 26/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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
public class InsertTestAsNewTest implements IService {

    public Integer run(Integer executionCourseId, Integer oldTestId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();

            ITest oldTest = (ITest) persistentTest.readByOID(Test.class, oldTestId);
            if (oldTest == null)
                throw new InvalidArgumentsServiceException();
            ITest test = new Test();
            persistentTest.simpleLockWrite(test);
            test.setTitle(oldTest.getTitle().concat(" (Duplicado)"));
            test.setInformation(oldTest.getInformation());
            test.setNumberOfQuestions(oldTest.getNumberOfQuestions());
            Date date = Calendar.getInstance().getTime();
            test.setCreationDate(date);
            test.setLastModifiedDate(date);
            test.setKeyTestScope(oldTest.getKeyTestScope());
            test.setTestScope(oldTest.getTestScope());

            IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();

            List<ITestQuestion> testQuestionList = persistentTestQuestion.readByTest(oldTestId);

            for (ITestQuestion testQuestion : testQuestionList) {
                ITestQuestion newTestQuestion = new TestQuestion();
                persistentTestQuestion.simpleLockWrite(newTestQuestion);
                newTestQuestion.setKeyQuestion(testQuestion.getKeyQuestion());
                newTestQuestion.setQuestion(testQuestion.getQuestion());
                newTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
                newTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
                newTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
                newTestQuestion.setKeyTest(test.getIdInternal());
                newTestQuestion.setTest(test);
            }
            return test.getIdInternal();
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}