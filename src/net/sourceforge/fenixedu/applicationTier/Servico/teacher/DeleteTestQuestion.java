/*
 * Created on 5/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteTestQuestion implements IService {

    public DeleteTestQuestion() {
    }

    public boolean run(Integer executionCourseId, Integer testId, Integer questionId)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId, true);
            if (test == null)
                throw new InvalidArgumentsServiceException();
            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
            IQuestion question = (IQuestion) persistentQuestion.readByOID(Question.class, questionId);
            if (question == null)
                throw new InvalidArgumentsServiceException();

            IPersistentTestQuestion persistentTestQuestion = persistentSuport
                    .getIPersistentTestQuestion();
            List testQuestionList = persistentTestQuestion.readByTest(test);

            ITestQuestion testQuestion = persistentTestQuestion.readByTestAndQuestion(test, question);
            if (testQuestion == null)
                throw new InvalidArgumentsServiceException();

            Integer questionOrder = testQuestion.getTestQuestionOrder();
            if (testQuestionList != null) {
                Iterator it = testQuestionList.iterator();
                while (it.hasNext()) {
                    ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
                    Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();

                    if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                        persistentTestQuestion.simpleLockWrite(iterTestQuestion);
                        iterTestQuestion.setTestQuestionOrder(new Integer(
                                iterQuestionOrder.intValue() - 1));
                    }
                }
            }
            persistentTestQuestion.delete(testQuestion);
            test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() - 1));
            test.setLastModifiedDate(Calendar.getInstance().getTime());
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}