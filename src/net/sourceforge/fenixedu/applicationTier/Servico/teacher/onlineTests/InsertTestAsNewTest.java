/*
 * Created on 26/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class InsertTestAsNewTest extends Service {

    public Integer run(Integer executionCourseId, Integer oldTestId) throws FenixServiceException, ExcepcaoPersistencia {
        Test oldTest = (Test) persistentSupport.getIPersistentTest().readByOID(Test.class, oldTestId);
        if (oldTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        Test test = DomainFactory.makeTest();
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
        test.setTitle(MessageFormat.format(bundle.getString("label.testTitle.duplicated"), new Object[] { oldTest.getTitle() }));
        test.setInformation(oldTest.getInformation());
        Date date = Calendar.getInstance().getTime();
        test.setCreationDate(date);
        test.setLastModifiedDate(date);
        // test.setTestScope(oldTest.getTestScope());
        oldTest.getTestScope().addTests(test);

        List<TestQuestion> testQuestionList = oldTest.getTestQuestions();

        for (TestQuestion testQuestion : testQuestionList) {
            TestQuestion newTestQuestion = DomainFactory.makeTestQuestion();
            newTestQuestion.setQuestion(testQuestion.getQuestion());
            newTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
            newTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
            newTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
            newTestQuestion.setTest(test);
        }
        return test.getIdInternal();
    }
}