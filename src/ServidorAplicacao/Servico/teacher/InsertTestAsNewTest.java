/*
 * Created on 26/Ago/2003
 *  
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Test;
import Dominio.TestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class InsertTestAsNewTest implements IService {

    public InsertTestAsNewTest() {
    }

    public Integer run(Integer executionCourseId, Integer oldTestId)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentTest persistentTest = persistentSuport
                    .getIPersistentTest();

            ITest oldTest = new Test(oldTestId);
            oldTest = (ITest) persistentTest.readByOId(oldTest, false);
            if (oldTest == null) throw new InvalidArgumentsServiceException();
            ITest test = new Test();

            test.setTitle(oldTest.getTitle().concat(" (Duplicado)"));
            test.setInformation(oldTest.getInformation());
            test.setNumberOfQuestions(oldTest.getNumberOfQuestions());
            Date date = Calendar.getInstance().getTime();
            test.setCreationDate(date);
            test.setLastModifiedDate(date);
            test.setKeyTestScope(oldTest.getKeyTestScope());
            test.setTestScope(oldTest.getTestScope());
            persistentTest.simpleLockWrite(test);

            IPersistentTestQuestion persistentTestQuestion = persistentSuport
                    .getIPersistentTestQuestion();

            List testQuestionList = persistentTestQuestion.readByTest(oldTest);

            Iterator it = testQuestionList.iterator();
            while (it.hasNext()) {
                ITestQuestion testQuestion = (ITestQuestion) it.next();
                ITestQuestion newTestQuestion = new TestQuestion();
                newTestQuestion.setKeyQuestion(testQuestion.getKeyQuestion());
                newTestQuestion.setQuestion(testQuestion.getQuestion());
                newTestQuestion.setTestQuestionOrder(testQuestion
                        .getTestQuestionOrder());
                newTestQuestion.setTestQuestionValue(testQuestion
                        .getTestQuestionValue());
                newTestQuestion.setCorrectionFormula(testQuestion
                        .getCorrectionFormula());
                newTestQuestion.setKeyTest(test.getIdInternal());
                newTestQuestion.setTest(test);
                persistentTestQuestion.simpleLockWrite(newTestQuestion);
            }
            return test.getIdInternal();
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}