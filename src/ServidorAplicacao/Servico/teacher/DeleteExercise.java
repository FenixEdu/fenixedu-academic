/*
 * Created on 24/Set/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Metadata;
import Dominio.Test;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class DeleteExercise implements IService {

    public DeleteExercise() {
    }

    public boolean run(Integer executionCourseId, Integer metadataId)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentMetadata persistentMetadata = persistentSuport
                    .getIPersistentMetadata();
            IMetadata metadata = (IMetadata) persistentMetadata.readByOID(
                    Metadata.class, metadataId, true);
            if (metadata == null)
                throw new InvalidArgumentsServiceException();
            List questionList = persistentSuport.getIPersistentQuestion()
                    .readByMetadata(metadata);
            boolean delete = true;
            Iterator questionIt = questionList.iterator();
            IPersistentQuestion persistentQuestion = persistentSuport
                    .getIPersistentQuestion();
            IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
                    .getIPersistentStudentTestQuestion();
            while (questionIt.hasNext()) {
                IQuestion question = (IQuestion) questionIt.next();
                List testQuestionList = persistentSuport
                        .getIPersistentTestQuestion().readByQuestion(question);
                Iterator testQuestionIt = testQuestionList.iterator();
                while (testQuestionIt.hasNext())
                    removeTestQuestionFromTest(persistentSuport,
                            (ITestQuestion) testQuestionIt.next());
                if (persistentStudentTestQuestion.countByQuestion(question) == 0) {
                    persistentQuestion.delete(question);
                } else {
                    persistentQuestion.lockWrite(question);
                    question.setVisibility(new Boolean("false"));
                    delete = false;
                }
            }
            if (delete) {
                persistentQuestion.deleteByMetadata(metadata);
                persistentMetadata.delete(metadata);
            } else {
                persistentMetadata.simpleLockWrite(metadata);
                metadata.setVisibility(new Boolean("false"));
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return true;
    }

    private void removeTestQuestionFromTest(
            ISuportePersistente persistentSuport, ITestQuestion testQuestion)
            throws ExcepcaoPersistencia {
        IPersistentTestQuestion persistentTestQuestion = persistentSuport
                .getIPersistentTestQuestion();
        ITest test = new Test(testQuestion.getKeyTest());
        test = (ITest) persistentSuport.getIPersistentTest().readByOID(
                Test.class, testQuestion.getKeyTest(), true);
        if (test == null)
            throw new ExcepcaoPersistencia();
        List testQuestionList = persistentTestQuestion.readByTest(test);
        Integer questionOrder = testQuestion.getTestQuestionOrder();
        if (testQuestionList != null) {
            Iterator it = testQuestionList.iterator();
            while (it.hasNext()) {
                ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
                Integer iterQuestionOrder = iterTestQuestion
                        .getTestQuestionOrder();
                if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                    persistentTestQuestion.simpleLockWrite(iterTestQuestion);
                    iterTestQuestion.setTestQuestionOrder(new Integer(
                            iterQuestionOrder.intValue() - 1));
                }
            }
        }
        persistentTestQuestion.delete(testQuestion);
        test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions()
                .intValue() - 1));
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }
}