/*
 * Created on 5/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Question;
import Dominio.Test;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class DeleteTestQuestion implements IService {

    public DeleteTestQuestion() {
    }

    public boolean run(Integer executionCourseId, Integer testId, Integer questionId)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

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