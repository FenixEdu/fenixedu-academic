/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteExercise implements IService {
    public DeleteExercise() {
    }

    public boolean run(Integer executionCourseId, Integer metadataId, String path)
            throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            IMetadata metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, metadataId,
                    true);
            if (metadata == null)
                throw new InvalidArgumentsServiceException();
            List questionList = persistentSuport.getIPersistentQuestion().readByMetadata(metadata);
            boolean delete = true;
            Iterator questionIt = questionList.iterator();
            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
            IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
                    .getIPersistentStudentTestQuestion();
            while (questionIt.hasNext()) {
                IQuestion question = (IQuestion) questionIt.next();
                List testQuestionList = persistentSuport.getIPersistentTestQuestion().readByQuestion(
                        question);
                Iterator testQuestionIt = testQuestionList.iterator();
                while (testQuestionIt.hasNext())
                    removeTestQuestionFromTest(persistentSuport, (ITestQuestion) testQuestionIt.next());
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

    private void removeTestQuestionFromTest(ISuportePersistente persistentSuport,
            ITestQuestion testQuestion) throws ExcepcaoPersistencia {
        IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();

        ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class,
                testQuestion.getKeyTest(), true);
        if (test == null)
            throw new ExcepcaoPersistencia();
        List testQuestionList = persistentTestQuestion.readByTest(test);
        Integer questionOrder = testQuestion.getTestQuestionOrder();
        if (testQuestionList != null) {
            Iterator it = testQuestionList.iterator();
            while (it.hasNext()) {
                ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
                Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
                if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                    persistentTestQuestion.simpleLockWrite(iterTestQuestion);
                    iterTestQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() - 1));
                }
            }
        }
        persistentTestQuestion.delete(testQuestion);
        test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() - 1));
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }
}