/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestion implements IService {

    public void run(Integer executionCourseId, Integer testId, Integer testQuestionId, Integer questionOrder, Double questionValue,
            CorrectionFormula formula) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();
            ITestQuestion testQuestion = (ITestQuestion) persistentTestQuestion.readByOID(TestQuestion.class, testQuestionId, true);
            if (testQuestion == null)
                throw new InvalidArgumentsServiceException();
            testQuestion.setTestQuestionValue(questionValue);
            testQuestion.setCorrectionFormula(formula);
            if (!questionOrder.equals(new Integer(-2))) {
                IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
                List<ITestQuestion> testQuestionList = persistentTestQuestion.readByTest(testId);
                if (questionOrder.equals(new Integer(-1)))
                    questionOrder = new Integer(testQuestionList.size());
                Integer questionOrderOld = testQuestion.getTestQuestionOrder();
                if (questionOrder.compareTo(questionOrderOld) < 0) {
                    questionOrder = new Integer(questionOrder.intValue() + 1);
                    for (ITestQuestion testQuestionIt : testQuestionList) {
                        Integer questionOrderIt = testQuestionIt.getTestQuestionOrder();

                        if (questionOrderIt.compareTo(questionOrder) >= 0 && questionOrderIt.compareTo(questionOrderOld) < 0) {
                            persistentTestQuestion.lockWrite(testQuestionIt);
                            testQuestionIt.setTestQuestionOrder(new Integer(questionOrderIt.intValue() + 1));
                        }
                    }
                } else if (questionOrder.compareTo(questionOrderOld) > 0) {
                    for (ITestQuestion testQuestionIt : testQuestionList) {
                        Integer questionOrderIt = testQuestionIt.getTestQuestionOrder();

                        if (questionOrderIt.compareTo(questionOrder) <= 0 && questionOrderIt.compareTo(questionOrderOld) > 0) {
                            persistentTestQuestion.lockWrite(testQuestionIt);
                            testQuestionIt.setTestQuestionOrder(new Integer(questionOrderIt.intValue() - 1));
                        }
                    }
                }
                testQuestion.setTestQuestionOrder(questionOrder);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}