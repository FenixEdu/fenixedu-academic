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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertTestAsNewTest implements IService {

    public Integer run(Integer executionCourseId, Integer oldTestId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ITest oldTest = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, oldTestId);
        if (oldTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        ITest test = DomainFactory.makeTest();
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
        test.setTitle(MessageFormat.format(bundle.getString("label.testTitle.duplicated"), new Object[] { oldTest.getTitle() }));
        test.setInformation(oldTest.getInformation());
        Date date = Calendar.getInstance().getTime();
        test.setCreationDate(date);
        test.setLastModifiedDate(date);
        // test.setTestScope(oldTest.getTestScope());
        oldTest.getTestScope().addTests(test);

        List<ITestQuestion> testQuestionList = oldTest.getTestQuestions();

        for (ITestQuestion testQuestion : testQuestionList) {
            ITestQuestion newTestQuestion = DomainFactory.makeTestQuestion();
            newTestQuestion.setQuestion(testQuestion.getQuestion());
            newTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
            newTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
            newTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
            newTestQuestion.setTest(test);
        }
        return test.getIdInternal();
    }
}