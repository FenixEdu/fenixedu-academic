/*
 * Created on 5/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteTestQuestion implements IService {

    public void run(Integer executionCourseId, Integer testId, final Integer questionId) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
        if (test == null)
            throw new InvalidArgumentsServiceException();
        ITestQuestion testQuestion = (ITestQuestion) CollectionUtils.find(test.getTestQuestions(), new Predicate() {

            public boolean evaluate(Object arg0) {
                final ITestQuestion testQuestion = (ITestQuestion) arg0;
                return testQuestion.getQuestion().getIdInternal().equals(questionId);
            }
            
        });
        if (testQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }
        
        test.deleteTestQuestion(testQuestion);
//        IQuestion question = testQuestion.getQuestion();
//
//        test.deleteTestQuestion(testQuestion);
//
//        Integer questionOrder = testQuestion.getTestQuestionOrder();
//
////      testQuestion.setTest(null);
////       testQuestion.setQuestion(null);
//        
//        test.removeTestQuestions(testQuestion);
//        
//        
//        question.removeTestQuestions(testQuestion);
//        persistentTestQuestion.deleteByOID(TestQuestion.class, testQuestion.getIdInternal());
//
//        
//
//        List<ITestQuestion> list = test.getTestQuestions();
//        for (ITestQuestion iterTestQuestion : list) {
//            Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
//            if (questionOrder.compareTo(iterQuestionOrder) < 0) {
//                persistentTestQuestion.simpleLockWrite(iterTestQuestion);
//                iterTestQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() - 1));
//            }
//        }


    }
}