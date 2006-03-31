/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Susana Fernandes
 */
public class DeleteExercise extends Service {

    public void run(Integer executionCourseId, Integer metadataId) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        IPersistentMetadata persistentMetadata = persistentSupport.getIPersistentMetadata();
        Metadata metadata = (Metadata) persistentObject.readByOID(Metadata.class, metadataId);
        if (metadata == null)
            throw new InvalidArgumentsServiceException();
        List<Question> questionList = persistentSupport.getIPersistentQuestion().readByMetadata(metadata);
        boolean delete = true;
        IPersistentQuestion persistentQuestion = persistentSupport.getIPersistentQuestion();
        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSupport.getIPersistentStudentTestQuestion();
        for (Question question : questionList) {
            List<TestQuestion> testQuestionList = question.getTestQuestions();
            for (TestQuestion testQuestion : testQuestionList) {
                removeTestQuestionFromTest(persistentSupport, testQuestion);
            }
            if (persistentStudentTestQuestion.countByQuestion(question.getIdInternal()) == 0) {
                persistentQuestion.deleteByOID(Question.class, question.getIdInternal());
            } else {
                question.setVisibility(new Boolean("false"));
                delete = false;
            }
        }
        if (delete) {
            persistentQuestion.deleteByMetadata(metadata);
            persistentMetadata.deleteByOID(Metadata.class, metadata.getIdInternal());
        } else {
            metadata.setVisibility(new Boolean("false"));
        }
    }

    private void removeTestQuestionFromTest(ISuportePersistente persistentSupport, TestQuestion testQuestion) throws ExcepcaoPersistencia {
        Test test = testQuestion.getTest();
        if (test == null)
            throw new ExcepcaoPersistencia();
        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
        Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
        Integer questionOrder = testQuestion.getTestQuestionOrder();
        if (testQuestionList != null) {
            for (TestQuestion iterTestQuestion : testQuestionList) {
                Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
                if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                    iterTestQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() - 1));
                }
            }
        }
        persistentObject.deleteByOID(TestQuestion.class, testQuestion.getIdInternal());
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }
}