/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.domain.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestion implements IService {

    public EditTestQuestion() {
    }

    public boolean run(Integer executionCourseId, Integer testId, Integer testQuestionId,
            Integer questionOrder, Double questionValue, CorrectionFormula formula)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTestQuestion persistentTestQuestion = persistentSuport
                    .getIPersistentTestQuestion();
            ITestQuestion testQuestion = (ITestQuestion) persistentTestQuestion.readByOID(
                    TestQuestion.class, testQuestionId, true);
            if (testQuestion == null)
                throw new InvalidArgumentsServiceException();
            testQuestion.setTestQuestionValue(questionValue);
            testQuestion.setCorrectionFormula(formula);
            if (!questionOrder.equals(new Integer(-2))) {
                IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
                ITest test = (ITest) persistentTest.readByOID(Test.class, testId, false);
                List testQuestionList = persistentTestQuestion.readByTest(test);

                if (questionOrder.equals(new Integer(-1)))
                    questionOrder = new Integer(testQuestionList.size());

                Iterator it = testQuestionList.iterator();
                Integer questionOrderOld = testQuestion.getTestQuestionOrder();
                if (questionOrder.compareTo(questionOrderOld) < 0) {
                    questionOrder = new Integer(questionOrder.intValue() + 1);
                    while (it.hasNext()) {
                        ITestQuestion testQuestionIt = (ITestQuestion) it.next();
                        Integer questionOrderIt = testQuestionIt.getTestQuestionOrder();

                        if (questionOrderIt.compareTo(questionOrder) >= 0
                                && questionOrderIt.compareTo(questionOrderOld) < 0) {
                            persistentTestQuestion.lockWrite(testQuestionIt);
                            testQuestionIt.setTestQuestionOrder(new Integer(
                                    questionOrderIt.intValue() + 1));
                        }
                    }
                } else if (questionOrder.compareTo(questionOrderOld) > 0) {
                    while (it.hasNext()) {
                        ITestQuestion testQuestionIt = (ITestQuestion) it.next();
                        Integer questionOrderIt = testQuestionIt.getTestQuestionOrder();

                        if (questionOrderIt.compareTo(questionOrder) <= 0
                                && questionOrderIt.compareTo(questionOrderOld) > 0) {
                            persistentTestQuestion.lockWrite(testQuestionIt);
                            testQuestionIt.setTestQuestionOrder(new Integer(
                                    questionOrderIt.intValue() - 1));
                        }
                    }
                }
                testQuestion.setTestQuestionOrder(questionOrder);
            }
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}